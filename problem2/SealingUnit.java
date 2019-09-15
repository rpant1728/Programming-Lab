import java.util.concurrent.*;
public class SealingUnit extends Thread{

    public  static int currentBottle=0;
    public static boolean isPacked=false;   //is current bottle sealed or not
    static int lastUnfinished=0;        //last type of bottle packed from unfinshed tray
    UnfinishedTray unfinishedTray;      //Unfinished tray object
    SealingUnitBuffer sealBuffer;       //Sealing unit buffer tray object
    PackingUnitBuffer packBuffer;       //Packing unit buffer tray object

    Time timer;
    Godown godown;
    Bottles bottles;
    Semaphore semUnfinished,packingSem,sealingSem,godownSem;
    //Constructor to initialise
    public SealingUnit(UnfinishedTray u,SealingUnitBuffer s,PackingUnitBuffer p,Semaphore sem,Semaphore packingSem,Semaphore sealingSem,Semaphore godownSem,Time timer,Godown godown,Bottles bottles){
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
    //update current state of sealing machine
    public void update(boolean isPacked,int currentBottle,int lastUnfinished){
        this.isPacked=isPacked;
        this.lastUnfinished=lastUnfinished;
        this.currentBottle=currentBottle;
    }
    @Override
    public void run(){
        
        int c=this.currentBottle;
        //if current bottle is packed send it to godown
        if(this.isPacked==true){
            //acquire lock on godown object
            try{
                this.godownSem.acquire();
                if(this.currentBottle==1){
                    this.godown.bottle1++;
                    this.bottles.sealedbottle1++;
                }else{
                    this.godown.bottle2++;
                    this.bottles.sealedbottle2++;
                }
            } catch (InterruptedException exc) { 
                System.out.println(exc); 
            }
            this.godownSem.release();           
        }else if(this.currentBottle!=0){    //update packing unit buffer 
            //acquire lock on godown object
            try{
                this.packingSem.acquire();
                if(c==1){
                    this.packBuffer.qBottle1++;
                    this.bottles.sealedbottle1++;
                }else{
                    this.packBuffer.qBottle2++;
                    this.bottles.sealedbottle2++;
                }
            }catch(InterruptedException exc) {
                System.out.println(exc); 
            }
            this.packingSem.release();
        }
        if(this.sealBuffer.sealUnitBuffer.isEmpty()){
            if((this.lastUnfinished!=2||this.unfinishedTray.b1==0)&&this.unfinishedTray.b2>0){
                //acquire lock on unfinished object
                try{
                    this.semUnfinished.acquire();
                    if(this.unfinishedTray.b2>0)
                        this.unfinishedTray.b2--;
                    else{
                        update(false,0,this.lastUnfinished);
                        this.timer.nextWakeUpSeal=this.timer.nextWakeUpPack;
                        return;
                    }                    
                }catch(InterruptedException exc) { 
                    System.out.println(exc); 
                }
                this.semUnfinished.release();
                update(false,2,2);
            }else if((this.lastUnfinished!=1||this.unfinishedTray.b2==0)&&this.unfinishedTray.b1>0){
                //acquire lock on unfinished object
                try{
                    this.semUnfinished.acquire();
                    if(this.unfinishedTray.b1>0)
                        this.unfinishedTray.b1--;
                    else{
                        update(false,0,this.lastUnfinished);
                        this.timer.nextWakeUpSeal=this.timer.nextWakeUpPack;
                        return;
                    }
                }catch(InterruptedException exc) { 
                    System.out.println(exc); 
                }
                this.semUnfinished.release();
                update(false,1,1);
            }
            else{   // if sealing unit buffer is empty and unfinised tray is empty
                update(false,0,this.lastUnfinished);
                this.timer.nextWakeUpSeal=this.timer.nextWakeUpPack;
                return;
            }
        }else{      //remove from sealing buffer and send it sealing unit
            int front=this.sealBuffer.sealUnitBuffer.peek();
            // acquire lock on sealing buffer
            try{
                this.sealingSem.acquire();
            
                this.sealBuffer.sealUnitBuffer.remove();
            }catch (InterruptedException exc) { 
                System.out.println(exc); 
            } 
            this.sealingSem.release();
            update(true,front,this.lastUnfinished);
        }
        this.timer.nextWakeUpSeal=this.timer.nextWakeUpSeal+3;
    }
}