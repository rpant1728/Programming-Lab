// package problem3;

public class Car {
    static int car_count = 0;
    TrafficLight light;
    String source_direction, dest_direction;
    int vehicle_id, arrival_time, departure_time;
    String status = "Waiting";

    public Car (TrafficLight light, String source_dir, String dest_dir, int time){
        this.light = light;
        source_direction = source_dir;
        destination_dire
        arrival_time = time;
        vehicle_id = car_count++;
    }

    void set_departure_time(int lightActive, int timeLeft){
        if(lightActive == this.light_id){
            car.departure_time = 6*(light.waiting_cars % 10) + 180*(light.waiting_cars / 10);
        }
        else{
            int diff;
            if(lightActive > car.light.light_id){
                diff = 3 + car.light.light_id - lightActive;
            }
            else diff = car.light.light_id - lightActive;
            car.departure_time = diff*60 + 6*(light.waiting_cars % 10) + 180*(light.waiting_cars / 10);
        }
        
        this.light.waiting_cars++;
    }
}