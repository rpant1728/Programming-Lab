import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class BottleType1{
    boolean isPacked;
    boolean isSealed;
    int state;
}
class BottleType2{
    boolean isPacked;
    boolean isSealed;
    int state;
}
class PackagingUnit extends Thread{
    boolean isfree;
    int currbottle_type;
    boolean isSealed;
    int unfinished_type;
    int sealed_type;       ///type of last processed sealed bottle
    int start_time;
    int buffer1;
    int buffer2;

    PackagingUnit(boolean isfree,int unfinished_type,int bottleType,int start_time,int currbottle_type,boolean isSealed){
        this.currbottle_type=currbottle_type;
        this.isSealed=isSealed;
        this.isfree=isfree;
        this.unfinished_type=unfinished_type;
        this.sealed_type=bottleType;
        this.start_time=start_time;
        this.buffer1=0;
        this.buffer2=0;
    }
    void update(boolean isfree,int unfinished_type,int sealed_type,int start_time,int currbottle_type,boolean isSealed){
        this.isfree=isfree;
        this.unfinished_type=unfinished_type;
        this.sealed_type=sealed_type;
        this.start_time=start_time;
        this.currbottle_type=currbottle_type;
        this.isSealed=isSealed;
    }
    @Override
    public void run(){
        if(this.isSealed){
            //increase number of bottles in godown
            if(this.buffer1==0&&this.buffer2==0){
                
            }if(this.buffer1==0){
                
            }

        }
        

        
    }
}
class SealingUnit extends Thread{
    
    boolean isfree;
    int start_time;
    int unfinished_type;  //type of bottle last processed from unfinished tray
    SealingUnit(boolean isfree,int unfinished_type,int start_time){
        this.isfree=isfree;
        this.unfinished_type=unfinished_type;
        this.start_time=start_time;
    }
}
class UnfinishedTray{
    int bottle_type1;
    int bottle_type2;
    UnfinishedTray(int bottle_type1,int bottle_type2){
        this.bottle_type1=bottle_type1;
        this.bottle_type2=bottle_type2;
    }
}
class PackingSystem{
    
    
    public static void main(String[] args){
        Scanner myObj = new Scanner(System.in);        
	    int b1=myObj.nextInt();
	    int b2=myObj.nextInt();
	    int time =myObj.nextInt();
        System.out.println("Bottle type1 " + b1+"\nBottle type2 " + b2+"\ntime :  " + time);  
        PackagingUnit p = new PackagingUnit(true,0,0,-10,0,false);
        SealingUnit s = new SealingUnit(true,0,0);
        UnfinishedTray t= new UnfinishedTray( b1 ,b2 );
        Queue<Integer> sealtrayBuffer = new LinkedList<>(),packTrayBufferB1 = new LinkedList<>(),packTrayBufferB2 = new LinkedList<>();
        int counter;
        counter=0;
        while(counter<time){
            if(counter-p.start_time>=2 && p.isfree){
                if(packTrayBufferB1.isEmpty()&&packTrayBufferB2.isEmpty()){
                    if(t.bottle_type1!=0&&p.unfinished_type!=1){
                        t.bottle_type1--;
                        p.update(false,1, p.sealed_type, counter,1,false);
                    }
                    else if(t.bottle_type2!=0&&p.unfinished_type!=2){
                        t.bottle_type2--;
                        p.update(false,2, p.sealed_type, counter,2,false);                        
                    }
                }else if(!packTrayBufferB1.isEmpty()&&!packTrayBufferB2.isEmpty()){
                    if(p.sealed_type!=1){
                        packTrayBufferB1.remove();
                        p.update(false,p.unfinished_type, 1, counter,1,true);                        
                    }else{
                        packTrayBufferB2.remove();
                        p.update(false,p.unfinished_type, 2, counter,2,true);                        
                    }
                }else if(!packTrayBufferB1.isEmpty()){
                    packTrayBufferB1.remove();
                    p.update(false,p.unfinished_type, 1, counter,1,true);
                }else{
                    packTrayBufferB2.remove();
                    p.update(false,p.unfinished_type, 2, counter,2,true);
                }
            }
            counter++;
            System.out.print(t.bottle_type1+t.bottle_type2);
            // System.out.print(p);
            // System.out.print(t);
        }

    }
}