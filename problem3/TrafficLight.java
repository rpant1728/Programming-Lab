// package problem3;

public class TrafficLight {
    static int light_count;
    int light_id;
    boolean isGreen;
    int waiting_cars;
    // static public List<Car> waiting_cars = new CopyOnWriteArrayList<Car>();

    public TrafficLight(){
        this.light_id = ++light_count;
        this.isGreen = false;
    }

    int set_departure_time(Car car, int lightActive){
        if(lightActive == this.light_id){
            car.departure_time = 6*(waiting_cars % 10) + 180*(waiting_cars / 10);
            // System.out
        }
        // else if(lightActive){
        //     car.departure_time = 6*(waiting_cars.size() % 10) + 18*waiting_cars.size();
        // }
        this.waiting_cars++;
        System.out.print(this.light_id + " " + lightActive + " " + car.departure_time);
        return car.departure_time;
    }
}