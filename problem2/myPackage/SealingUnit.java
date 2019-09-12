package myPackage;
import java.util.concurrent.*;
public class SealingUnit extends Thread{
    // int nextTime;
    // int qBottle1;       
    // int qBottle2;       //count of bottle in queue
    public  static int currentBottle=0;
    public static boolean isPacked=false;   //is current bottle sealed or not
    static int lastUnfinished=0; //last type of bottle packed from unfinshed tray
    // int lastQueue;      //last type of bottle packed from qu'sray
    UnfinishedTray unfinishedTray;
    SealingUnitBuffer sealBuffer;
    PackingUnitBuffer packBuffer;

    Time timer;
    Godown godown;
    Bottles bottles;
    Semaphore semUnfinished,packingSem,sealingSem,godownSem;
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
    public void update(boolean isPacked,int currentBottle,int lastUnfinished){
        this.isPacked=isPacked;
        this.lastUnfinished=lastUnfinished;
        this.currentBottle=currentBottle;
    }
    @Override
    public void run(){
        //update packing buffer
        int c=this.currentBottle;
        
        if(this.isPacked==true){
            if(this.currentBottle==1){
                this.bottles.sealedbottle1++;
            }else{    
                this.bottles.sealedbottle2++;
            }             
        }else if(this.currentBottle!=0){
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
                try{
                    this.semUnfinished.acquire();
                    this.unfinishedTray.b2--;
                }catch(InterruptedException exc) { 
                    System.out.println(exc); 
                }
                this.semUnfinished.release();
                update(false,2,2);
            }else if((this.lastUnfinished!=1||this.unfinishedTray.b2==0)&&this.unfinishedTray.b1>0){
                try{
                    this.semUnfinished.acquire();
                    this.unfinishedTray.b1--;
                }catch(InterruptedException exc) { 
                    System.out.println(exc); 
                }
                this.semUnfinished.release();
                update(false,1,1);
            }
            else{
                update(false,0,this.lastUnfinished);
                this.timer.nextBottle2=this.timer.nextBottle2+1;
                return;
            }
        }else{
            int front=this.sealBuffer.sealUnitBuffer.peek();
            try{
                this.sealingSem.acquire();
            
                this.sealBuffer.sealUnitBuffer.remove();
            }catch (InterruptedException exc) { 
                System.out.println(exc); 
            } 
            this.sealingSem.release();
            update(true,front,this.lastUnfinished);
        }
        this.timer.nextBottle2=this.timer.nextBottle2+3;
    }
}