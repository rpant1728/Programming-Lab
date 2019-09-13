public class Car {
    static int car_count = 0;
    TrafficLight light;
    String sourceDirection, destinationDirection;
    int vehicleID, arrivalTime, departureTime;
    String status = "Init";

    public Car (TrafficLight light, String sourceDirection, String destinationDirection, int time){
        this.light = light;
        this.sourceDirection = sourceDirection;
        this.destinationDirection = destinationDirection;
        this.arrivalTime = time;
        this.vehicleID = car_count++;
    }

    void set_departure_time(int activeLight, int currentTime){
        boolean check = false;
        if(light == null){
            departureTime = 0;
            status = "Passing";
            check = true;
            return;
        } 
        if(light.waitingCars == 0) {
            if(activeLight != light.lightID){
                int difference;
                if(activeLight > light.lightID){
                    difference = 3 + light.lightID - activeLight;
                }
                else difference = light.lightID - activeLight;
                departureTime = difference*60 - currentTime;
            }
            else{
                if(currentTime-light.previousRunTime > 6){
                    if(currentTime < 54){
                        light.previousRunTime = currentTime;
                        departureTime = -1;
                        status = "Passing";
                        check = true;
                    }
                    else{
                        light.previousRunTime = (180-currentTime)%60;
                        departureTime = 180-currentTime;
                    }
                }
                else{
                    if(light.previousRunTime <= 48){
                        departureTime = 6 - (currentTime - light.previousRunTime);
                    }
                    else{
                        departureTime = 180 - currentTime;
                    }
                }
            }
            light.lastWaitingTime = departureTime + 6;
            if(!check) light.waitingCars++; 
        }
        else {
            int waitingTime = light.lastWaitingTime;
            if(light.lastWaitingTime%60 > 54) waitingTime += 180 - light.lastWaitingTime%60;
            else if (light.lastWaitingTime != 0 && light.lastWaitingTime%60 == 0) waitingTime += 120;
            departureTime = waitingTime;
            light.lastWaitingTime = departureTime + 6;        
            light.waitingCars++;
        }
    }
}