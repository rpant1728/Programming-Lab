// package problem3;

public class Car {
    static int car_count = 0;
    String source_direction, dest_direction;
    int vehicle_id, arrival_time, departure_time;
    String status = "Waiting";

    public Car (String source_dir, String dest_dir, int time){
        source_direction = source_dir;
        dest_direction = dest_dir;
        arrival_time = time;
        vehicle_id = car_count++;
    }
}