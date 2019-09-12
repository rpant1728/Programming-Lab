public class Car {
    static int car_count = 0;
    TrafficLight light;
    String source_direction, dest_direction;
    int vehicle_id, arrival_time, departure_time;
    String status = "Init";

    public Car (TrafficLight light, String source_dir, String dest_dir, int time){
        this.light = light;
        source_direction = source_dir;
        dest_direction = dest_dir;
        arrival_time = time;
        vehicle_id = car_count++;
    }

    void set_departure_time(int lightActive, int leftTime){
        boolean check = false;
        if(light == null){
            departure_time = 0;
            status = "Passing";
            check = true;
            return;
        }
        if(light.waiting_cars == 0) {
            if(lightActive != light.light_id){
                int diff;
                if(lightActive > light.light_id){
                    diff = 3 + light.light_id - lightActive;
                }
                else diff = light.light_id - lightActive;
                departure_time = diff*60-leftTime;
            }
            else{
                if(leftTime-light.prev_time > 6){
                    if(leftTime < 54){
                        light.prev_time = leftTime;
                        departure_time = -1;
                        status = "Passing";
                        check = true;
                    }
                    else{
                        light.prev_time = (180-leftTime)%60;
                        departure_time = 180-leftTime;
                    }
                }
                else{
                    if(light.prev_time <= 48){
                        departure_time = 6 - (leftTime - light.prev_time);
                    }
                    else{
                        departure_time = 180 - leftTime;
                    }
                }
            }
            light.last_waiting_time = departure_time + 6;
            if(!check) light.waiting_cars++; 
        }
        else {
            int waiting_time = light.last_waiting_time;
            if(light.last_waiting_time%60 > 54) waiting_time += 180 - light.last_waiting_time%60;
            else if (light.last_waiting_time != 0 && light.last_waiting_time%60 == 0) waiting_time += 120;
            departure_time = waiting_time;
            light.last_waiting_time = departure_time + 6;        
            light.waiting_cars++;
        }
    }
}