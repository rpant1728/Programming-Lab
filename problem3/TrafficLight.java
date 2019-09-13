public class TrafficLight {
    static int lightCount;
    int lightID;
    boolean isGreen;
    int waitingCars, previousRunTime = -7, lastWaitingTime;

    public TrafficLight(){
        this.lightID = ++lightCount;
        this.isGreen = false;
        this.waitingCars = 0;
    }
}