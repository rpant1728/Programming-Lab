import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;

public class Factory{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);  
        Semaphore sem=new Semaphore(1); 
        Semaphore packingSem=new Semaphore(1);      
        Semaphore sealingSem=new Semaphore(1); 
        Semaphore godownSem= new Semaphore(1);
	    int b1=input.nextInt();
	    int b2=input.nextInt();
        int inp_time =input.nextInt();
        Time time = new Time();
        SealingUnitBuffer sealUnitBuffer = new SealingUnitBuffer(sealingSem);
        Godown godown = new Godown();
        UnfinishedTray unfinishedTray=new UnfinishedTray(b1,b2,sem);
        PackingUnitBuffer pub=new PackingUnitBuffer(packingSem);
        Bottles bottles=new Bottles();
        
        while(time.currentTime <= inp_time){
            int currentTime,wakeUpTimePack,wakeUpTimeSeal;
            currentTime=time.currentTime;
            wakeUpTimePack=time.nextWakeUpPack;
            wakeUpTimeSeal=time.nextWakeUpSeal;
            if(currentTime == wakeUpTimePack && currentTime == wakeUpTimeSeal){

                PackingUnit packing = new PackingUnit(unfinishedTray,sealUnitBuffer,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
                SealingUnit sealing = new SealingUnit(unfinishedTray,sealUnitBuffer,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
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
            }
            else if(currentTime  == wakeUpTimePack){
                PackingUnit packing = new PackingUnit(unfinishedTray,sealUnitBuffer,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
                packing.start();
                try {
                    packing.join();
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }       
            }
            else if(currentTime == wakeUpTimeSeal){    
                SealingUnit sealing = new SealingUnit(unfinishedTray,sealUnitBuffer,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
                sealing.start();
                try {
                    sealing.join();
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }
            }    

            if(time.nextWakeUpPack< time.nextWakeUpSeal){
                time.currentTime=time.nextWakeUpPack;
            } else{
                time.currentTime=time.nextWakeUpSeal;
            }
            
            int godownbottle1,godownbottle2;
            if(bottles.packedbottle1<bottles.sealedbottle1){
                godownbottle1=bottles.packedbottle1;
            }else{
                godownbottle1=bottles.sealedbottle1;
            }
            
            if(bottles.packedbottle2<bottles.sealedbottle2){
                godownbottle2=bottles.packedbottle2;
            }else{
                godownbottle2=bottles.sealedbottle2;
            }

            if(godownbottle1+godownbottle2==b1+b2){
                break;
            }
        }
        int godownbottle1=godown.bottle1,godownbottle2=godown.bottle2;
        int packedBottle1=godownbottle1,packedBottle2=godownbottle2;
        int sealedBottle1,sealedBottle2;
        sealedBottle1=godownbottle1+pub.qBottle1;
        sealedBottle2=godownbottle2+pub.qBottle2;
        while(!sealUnitBuffer.sealUnitBuffer.isEmpty()){
            if(sealUnitBuffer.sealUnitBuffer.peek()==1){
                packedBottle1++;
            }else if(sealUnitBuffer.sealUnitBuffer.peek()==2){
                packedBottle2++;
            }
            sealUnitBuffer.sealUnitBuffer.remove();
        }
        PackingUnit packing = new PackingUnit(unfinishedTray,sealUnitBuffer,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
        SealingUnit sealing = new SealingUnit(unfinishedTray,sealUnitBuffer,pub,sem,packingSem,sealingSem,godownSem,time,godown,bottles);
        if(packing.isSealed){
            if(packing.currentBottle==1){
                sealedBottle1++;
            }else if(packing.currentBottle==2){
                sealedBottle2++;
            }
        }
        if(sealing.isPacked){
            if(packing.currentBottle==1){
                packedBottle1++;
            }else if(packing.currentBottle==2){
                packedBottle2++;
            }
        }

        System.out.println("--------------------------");
        System.out.println("| Bottle1 Packed   |   "+bottles.packedbottle1+" |");
        System.out.println("| Bottle1 Sealed   |   "+bottles.sealedbottle1+" |");
        if(bottles.packedbottle1<bottles.sealedbottle1){
            godownbottle1=bottles.packedbottle1;
        }else{
            godownbottle1=bottles.sealedbottle1;
        }
        System.out.println("| Bottle1 Godown   |   "+godown.bottle1+" |");
        System.out.println("--------------------------");
        System.out.println("| Bottle2 Packed   |   "+bottles.packedbottle2+" |");
        System.out.println("| Bottle2 Sealed   |   "+bottles.sealedbottle2+" |");
        if(bottles.packedbottle2<bottles.sealedbottle2){
            godownbottle2=bottles.packedbottle2;
        }else{
            godownbottle2=bottles.sealedbottle2;
        }
        System.out.println("| Bottle2 Godown   |   "+godown.bottle2+" |");
        System.out.println("--------------------------");
    }
}