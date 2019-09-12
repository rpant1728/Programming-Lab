public class TrafficLight {
    static int light_count;
    int light_id;
    boolean isGreen;
    int waiting_cars, prev_time = -7, last_waiting_time;

    public TrafficLight(){
        this.light_id = ++light_count;
        this.isGreen = false;
        this.waiting_cars = 0;
    }
}