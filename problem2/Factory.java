import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import myPackage.*;
import java.util.concurrent.*;

public class Factory{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);  
        Semaphore sem=new Semaphore(1); 
        Semaphore packingSem=new Semaphore(1);      
        Semaphore sealingSem=new Semaphore(1); 
        Semaphore godownSem= new Semaphore(1);
        // Semaphore sem=new Semaphore(1); 

	    int b1=input.nextInt();
	    int b2=input.nextInt();
        int inp_time =input.nextInt();
        Time time = new Time();
        SealingUnitBuffer sub = new SealingUnitBuffer(sealingSem);
        Godown godown = new Godown();
        UnfinishedTray tray=new UnfinishedTray(b1,b2,sem);
        PackingUnitBuffer pub=new PackingUnitBuffer(packingSem);
        Bottles bottles=new Bottles();
        while(time.currentTime <= inp_time){
            int ct,t1,t2;
            ct=time.currentTime;
            t1=time.nextBottle1;
            t2=time.nextBottle2;
            if(ct == t1 && ct == t2){
                System.out.println("a");
                PackingUnit packing = new PackingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
                SealingUnit sealing = new SealingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
                sealing.start();
                packing.start();
                try {
                    sealing.join();
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }
                
                
                try {
                    packing.join();                    
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }
                System.out.println("In Packing Machine : "+packing.currentBottle+" isSealed "+packing.isSealed);
                System.out.println("In Sealing Machine : "+sealing.currentBottle+" ispacked "+sealing.isPacked);
               
            }
            else if(ct  == t1){
                System.out.println("b");
                
                PackingUnit packing = new PackingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
                packing.start();

                // System.out.println("In Packing Machine : "+sealing.currentBottle+" isSealed "+packing.isPacked);
                try {
                    packing.join();
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }
                        
                // System.out.println("In Packing Machine : "+packing.currentBottle+" isSealed "+packing.isSealed);
            }
            else if(ct == t2){
                System.out.println("c");
                SealingUnit sealing = new SealingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
                sealing.start();

            // System.out.println("In Packing Machine : "+packing.currentBottle+" isSealed "+packing.isSealed);
                try {
                    sealing.join();
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }
                // System.out.println("In Sealing Machine : "+sealing.currentBottle+" ispacked "+sealing.isPacked);

            }
            else {
                System.out.println(time.currentTime);
                System.out.println(time.nextBottle1+" "+ time.nextBottle2);  
               
            }    
            System.out.println(time.currentTime);
            // System.out.println("In Packing Machine : "+packing.currentBottle+" isSealed "+packing.isSealed);
            // System.out.println("In Packing Machine : "+sealing.currentBottle+" isSealed "+packing.isPacked);
            // System.out.println("Godown: " + godown.bottle1 + " " + godown.bottle2);  
            System.out.println("Packing Unit: " + pub.qBottle1+" " + pub.qBottle2);
            System.out.println("Sealing Unit: " + sub.sealUnitBuffer.peek()+" size "+ sub.sealUnitBuffer.size());
            System.out.println("Unfinished Tray: " + tray.b1 + " " + tray.b2); 
            System.out.println("----------------------------------");
            if(time.nextBottle1< time.nextBottle2){
                time.currentTime=time.nextBottle1;
            } else{
                time.currentTime=time.nextBottle2;
            }
            // System.out.println("hbjk");
            int gb1,gb2;
            if(bottles.packedbottle1<bottles.sealedbottle1){
                gb1=bottles.packedbottle1;
            }else{
                gb1=bottles.sealedbottle1;
            }
            if(bottles.packedbottle2<bottles.sealedbottle2){
                gb2=bottles.packedbottle2;
            }else{
                gb2=bottles.sealedbottle2;
            }
            if(gb1+gb2==b1+b2){
                break;
            }
        }
        int gb1=godown.bottle1,gb2=godown.bottle2;
        int pb1=gb1,pb2=gb2;
        int sb1,sb2;
        sb1=gb1+pub.qBottle1;
        sb2=gb2+pub.qBottle2;
        while(!sub.sealUnitBuffer.isEmpty()){
            if(sub.sealUnitBuffer.peek()==1){
                pb1++;
            }else if(sub.sealUnitBuffer.peek()==2){
                pb2++;
            }
            sub.sealUnitBuffer.remove();
        }
        PackingUnit packing = new PackingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
        SealingUnit sealing = new SealingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
        if(packing.isSealed){
            if(packing.currentBottle==1){
                sb1++;
            }else if(packing.currentBottle==2){
                sb2++;
            }
        }
        if(sealing.isPacked){
            if(packing.currentBottle==1){
                pb1++;
            }else if(packing.currentBottle==2){
                pb2++;
            }
        }
       
        System.out.println("Bottle1");
        System.out.println("Packed "+bottles.packedbottle1);
        System.out.println("Sealed "+bottles.sealedbottle1);
        if(bottles.packedbottle1<bottles.sealedbottle1){
            gb1=bottles.packedbottle1;
        }else{
            gb1=bottles.sealedbottle1;
        }
        System.out.println("Godown "+gb1);
        System.out.println("Bottle2");
        System.out.println("Packed "+bottles.packedbottle2);
        System.out.println("Sealed "+bottles.sealedbottle2);
        if(bottles.packedbottle2<bottles.sealedbottle2){
            gb2=bottles.packedbottle2;
        }else{
            gb2=bottles.sealedbottle2;
        }
        System.out.println("Godown "+gb2);
    }
}