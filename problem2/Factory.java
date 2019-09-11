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
        while(time.currentTime <= inp_time){
            
            if(time.currentTime == time.nextBottle1 && time.currentTime  == time.nextBottle2){
                PackingUnit packing = new PackingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown);
                SealingUnit sealing = new SealingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown);
                packing.start();
                sealing.start();

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
                // System.out.println("In Packing Machine : "+packing.currentBottle+" isSealed "+packing.isSealed);
                // System.out.println("In Sealing Machine : "+sealing.currentBottle+" ispacked "+sealing.isPacked);
               
            }
            else if(time.currentTime  == time.nextBottle1){
                PackingUnit packing = new PackingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown);
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
            else if(time.currentTime  == time.nextBottle2){
                SealingUnit sealing = new SealingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown);
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
            // System.out.println(time.currentTime + " " + time.nextBottle1 + " " + time.nextBottle2);
            // System.out.println(time.currentTime);
            // System.out.println("In Packing Machine : "+packing.currentBottle+" isSealed "+packing.isSealed);
            // System.out.println("In Packing Machine : "+sealing.currentBottle+" isSealed "+packing.isPacked);
            // System.out.println("Godown: " + godown.bottle1 + " " + godown.bottle2);  
            // System.out.println("Packing Unit: " + pub.qBottle1+" " + pub.qBottle2);
            // System.out.println("Sealing Unit: " + sub.sealUnitBuffer.peek()+" size "+ sub.sealUnitBuffer.size());
            // System.out.println("Unfinished Tray: " + tray.b1 + " " + tray.b2); 
            // System.out.println("----------------------------------");
            time.currentTime = Math.min(time.nextBottle1, time.nextBottle2); 
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
        PackingUnit packing = new PackingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown);
        SealingUnit sealing = new SealingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown);
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
        System.out.println("Packed "+pb1);
        System.out.println("Sealed "+sb1);
        System.out.println("Godown "+gb1);
        // System.out.println("Packed: "+);  
        System.out.println("Bottle2");
        System.out.println("Packed "+pb2);
        System.out.println("Sealed "+sb2);
        System.out.println("Godown "+gb2);
    }
}