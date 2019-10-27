import java.util.concurrent.*;
public class UnfinishedTray{
    public int b1;      //number of unfinished bottle of type 1
    public int b2;      //number of unfinished bottle of type 2
    Semaphore sem;
    public UnfinishedTray(int bottle_type1,int bottle_type2,Semaphore sem){
        this.b1=bottle_type1;
        this.b2=bottle_type2;
        this.sem=sem;
    }
}