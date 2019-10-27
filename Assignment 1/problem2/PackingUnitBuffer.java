import java.util.concurrent.*;
public class PackingUnitBuffer{
    public int qBottle1;    //numbers of bottle of type 1 in queue
    public int qBottle2;    //numbers of bottle of type 2 in queue
    Semaphore sem;          
    public PackingUnitBuffer(Semaphore sem){
        this.qBottle1=0;
        this.qBottle2=0;
        this.sem=sem;
    }
}