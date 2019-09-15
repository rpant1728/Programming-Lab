import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public class SealingUnitBuffer{
    public Queue<Integer> sealUnitBuffer;   //sealing unit buffer tray
    Semaphore sem;
    public SealingUnitBuffer(Semaphore sem){
        this.sealUnitBuffer= new LinkedList<>();
        this.sem=sem;
    }
}