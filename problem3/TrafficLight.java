// package problem3;

public class TrafficLight {
    static int light_count;
    int light_id;
    boolean isGreen;
    int waiting_cars;

    public TrafficLight(){
        this.light_id = ++light_count;
        this.isGreen = false;
    }
}