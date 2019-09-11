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
        while(time.currentTime < inp_time){
            
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
            }
            else if(time.currentTime  == time.nextBottle1){
                PackingUnit packing = new PackingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown);
                packing.start();
                try {
                    packing.join();
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }               
            }
            else if(time.currentTime  == time.nextBottle2){
                SealingUnit sealing = new SealingUnit(tray,sub,pub,sem,packingSem,sealingSem,godownSem,time,godown);
                sealing.start();
                try {
                    sealing.join();
                } 
                catch(InterruptedException e) {
                    // this part is executed when an exception (in this example InterruptedException) occurs
                }
            }    
            // System.out.println(time.currentTime + " " + time.nextBottle1 + " " + time.nextBottle2);
            time.currentTime = Math.min(time.nextBottle1, time.nextBottle2); 
            System.out.println(time.currentTime);
            System.out.println("Godown: " + godown.bottle1 + " " + godown.bottle2);  
            System.out.println("Packing Unit: " + pub.qBottle1+" " + pub.qBottle2);
            System.out.println("Sealing Unit: " + sub.sealUnitBuffer.size());
            System.out.println("Unfinished Tray: " + tray.b1 + " " + tray.b2);     
        }

        // System.out.println("Godown: " + godown.bottle1 + " " + godown.bottle2);  
        // System.out.println("Packing Unit: " + pub.qBottle1+" " + pub.qBottle2);
        // System.out.println("Sealing Unit: " + sub.sealUnitBuffer.size());
        // System.out.println("Unfinished Tray: " + tray.b1 + " " + tray.b2);
        
    }
}