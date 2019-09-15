import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;
// Main Factory Class which executes the functionalities of PackingUnit and Sealing Unit
public class Factory{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);     
        Semaphore unfinishedSem=new Semaphore(1);             //unfinised tray seamphore
        Semaphore packingSem=new Semaphore(1);                //packing unit semaphore
        Semaphore sealingSem=new Semaphore(1);                //sealing unit semaphore
        Semaphore godownSem= new Semaphore(1);                //godown semaphore
	    int bottle1=input.nextInt();                          // input bottle1
	    int bottle2=input.nextInt();                          // input bottle2
        int inp_time =input.nextInt();                        // input time
        Time time = new Time();
        //sealing buffer tray
        SealingUnitBuffer sealUnitBuffer = new SealingUnitBuffer(sealingSem);   
        Godown godown = new Godown();
        //unfinished  tray
        UnfinishedTray unfinishedTray=new UnfinishedTray(bottle1,bottle2,unfinishedSem);
        //packing unit buffer tray
        PackingUnitBuffer pub=new PackingUnitBuffer(packingSem);
        Bottles bottles=new Bottles();
    
        while(time.currentTime <= inp_time){
                    
            int currentTime;        //current time
            int wakeUpTimePack;     //wake up time of packing machine
            int wakeUpTimeSeal;     //wake up time of sealing machine
            currentTime=time.currentTime;
            wakeUpTimePack=time.nextWakeUpPack;
            wakeUpTimeSeal=time.nextWakeUpSeal;
            //if current time is equal to wake up time of both sealing unit and packing unit 
            if(currentTime == wakeUpTimePack && currentTime == wakeUpTimeSeal){
                //create packing unit object
                PackingUnit packing = new PackingUnit(unfinishedTray,sealUnitBuffer,pub,unfinishedSem,packingSem,sealingSem,godownSem,time,godown,bottles);
                //create sealing unit object
                SealingUnit sealing = new SealingUnit(unfinishedTray,sealUnitBuffer,pub,unfinishedSem,packingSem,sealingSem,godownSem,time,godown,bottles);
                sealing.start();    //start sealing machine thread
                packing.start();    //start packing machine thread
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
            //if current time is equal to wake up time of packing machine
            else if(currentTime  == wakeUpTimePack){
                PackingUnit packing = new PackingUnit(unfinishedTray,sealUnitBuffer,pub,unfinishedSem,packingSem,sealingSem,godownSem,time,godown,bottles);
                packing.start();
                try {
                    packing.join();
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }       
            }
            //if current time is equal to wake up time of sealing machine
            else if(currentTime == wakeUpTimeSeal){    
                SealingUnit sealing = new SealingUnit(unfinishedTray,sealUnitBuffer,pub,unfinishedSem,packingSem,sealingSem,godownSem,time,godown,bottles);
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
            // if all bottles are in godown 
            if(godown.bottle1+godown.bottle2==bottle1+bottle2){
                break;
            }

        }
        //current state of bottle after given time 
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
        PackingUnit packing = new PackingUnit(unfinishedTray,sealUnitBuffer,pub,unfinishedSem,packingSem,sealingSem,godownSem,time,godown,bottles);
        SealingUnit sealing = new SealingUnit(unfinishedTray,sealUnitBuffer,pub,unfinishedSem,packingSem,sealingSem,godownSem,time,godown,bottles);
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