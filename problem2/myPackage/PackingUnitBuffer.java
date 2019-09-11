package myPackage;
import java.util.concurrent.*;
public class PackingUnitBuffer{
    public int qBottle1;
    public int qBottle2;
    Semaphore sem;
    public PackingUnitBuffer(Semaphore sem){
        this.qBottle1=0;
        this.qBottle2=0;
        this.sem=sem;
    }
}