package myPackage;
import java.util.concurrent.*;
public class PackingUnit extends Thread{
    public static int currentBottle=0;
    public static boolean isSealed=false;   //is current bottle sealed or not
    static int lastUnfinished=0; //last type of bottle packed from unfinshed tray
    static int lastQueue=0;      //last type of bottle packed from qu'sray
    UnfinishedTray unfinishedTray;
    Semaphore semUnfinished,packingSem,sealingSem,godownSem;
    Time timer;
    SealingUnitBuffer sealBuffer;
    PackingUnitBuffer packBuffer;
    Godown godown;
    Bottles bottles;

    public PackingUnit(UnfinishedTray u,SealingUnitBuffer s,PackingUnitBuffer p,Semaphore sem,Semaphore packingSem,Semaphore sealingSem,Semaphore godownSem,Time timer,Godown godown,Bottles bottles){
        this.unfinishedTray=u;
        this.sealBuffer=s;
        this.packBuffer=p;
        this.semUnfinished=sem;
        this.packingSem=packingSem;
        this.sealingSem=sealingSem;
        this.godownSem=godownSem;
        this.timer=timer;
        this.godown=godown;
        this.bottles=bottles;
    }        
    public void update(boolean sealed,int bottle,int lastUnfinished,int lastQueue){
        this.isSealed=sealed;
        this.currentBottle=bottle;
        this.lastUnfinished=lastUnfinished;
        this.lastQueue=lastQueue;
    }
    @Override
    public void run(){
        // first update the output according to current data
        int c=this.currentBottle;
        if(this.isSealed){
            if(this.currentBottle==1){
                this.bottles.packedbottle1++;
            }else{
                this.bottles.packedbottle2++;
            }
        }else if(this.currentBottle!=0){
            System.out.println("current"+ this.timer.currentTime);
            if(this.sealBuffer.sealUnitBuffer.size()<2){
                try{
                    this.sealingSem.acquire();
                    this.sealBuffer.sealUnitBuffer.add(this.currentBottle);
                    if(this.currentBottle==1){
                        this.bottles.packedbottle1++;
                    }
                    else{
                        this.bottles.packedbottle2++;
                    }
                } catch (InterruptedException exc) { 
                    System.out.println(exc); 
                }
                this.sealingSem.release();
            }else{
                System.out.println("in"+this.timer.nextBottle2+ " "+this.timer.nextBottle1);
                // if(this.timer.nextBottle2>this.timer.nextBottle1)
                int t=this.timer.nextBottle2;
                    this.timer.nextBottle1=t;
                    
                // else{
                //     this.timer.nextBottle1=this.timer.nextBottle1+2;
                //     this.timer.nextBottle2=this.timer.nextBottle2+3;
                // }
                
                return;
            }
        }
        if(this.packBuffer.qBottle1==0&&this.packBuffer.qBottle2==0){
            
            if((this.lastUnfinished!=1||this.unfinishedTray.b2==0)&&this.unfinishedTray.b1>0){
                try{
                    this.semUnfinished.acquire();
                    this.unfinishedTray.b1--;
                }catch(InterruptedException exc) { 
                    System.out.println(exc); 
                } 

                this.semUnfinished.release();
                update(false,1,1,this.lastQueue);
            }else if((this.lastUnfinished!=2||this.unfinishedTray.b1==0)&&this.unfinishedTray.b2>0){
                try{
                    this.semUnfinished.acquire();
                    this.unfinishedTray.b2--;
                }catch(InterruptedException exc) { 
                    System.out.println(exc); 
                }
                this.semUnfinished.release();
                update(false,2,2,this.lastQueue);
            }else{
                update(false,0,this.lastUnfinished,this.lastQueue);
                // if(this.timer.nextBottle1==this.timer.nextBottle2){
                //     this.timer.nextBottle1++;
                // }else{
                    int t=this.timer.nextBottle2;
                    this.timer.nextBottle1=t;
                // }
                return;
            }
        }else if((this.lastQueue!=1||this.packBuffer.qBottle2==0)&&this.packBuffer.qBottle1>0){
            try{
                this.packingSem.acquire();
            
                this.packBuffer.qBottle1--;
            }catch (InterruptedException exc) { 
                System.out.println(exc); 
            } 
            this.packingSem.release();
            update(true,1,this.lastUnfinished,1);
        }else if((this.lastQueue!=2||this.packBuffer.qBottle1==0)&&this.packBuffer.qBottle2>0){
            try{
                this.packingSem.acquire();
                this.packBuffer.qBottle2--;
            }catch (InterruptedException exc) { 
                System.out.println(exc); 
            } 
            this.packingSem.release();

            update(true,2,this.lastUnfinished,2);
        }
        else{
            int t=this.timer.nextBottle2;
            this.timer.nextBottle1=t;
             return;
        }
        this.timer.nextBottle1=this.timer.nextBottle1+2;
    }   
}
