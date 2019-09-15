public class Car {
    static int car_count = 0;                      // Maintains count of total cars arrived till now
    TrafficLight light;                            // Light at which the car arrives
    String sourceDirection, destinationDirection;  // Directions that car takes at the trisection
    int vehicleID;                                 // Unique ID for the vehicle
    int arrivalTime, departureTime;                // Time at which  the vehicle arrived and time at which it is scheduled to leave
    String status = "Init";                        // Current status of car. Can be "Init", "Waiting", "Passing" or "Passed"


    // Constructor
    public Car (TrafficLight light, String sourceDirection, String destinationDirection, int time){
        this.light = light;
        this.sourceDirection = sourceDirection;
        this.destinationDirection = destinationDirection;
        this.arrivalTime = time;
        this.vehicleID = car_count++;
    }

    // Sets the departure time for a vehicle
    void set_departure_time(int activeLight, int currentTime){
        boolean check = false;

        // If car is destined for a direction not governed by a traffic light
        if(light == null){
            // Allow car to pass immediately
            departureTime = 0;
            status = "Passing";
            check = true;
            return;
        } 

        // If there are no cars currently waiting at the light
        if(light.waitingCars == 0) {
            // If signal currently active is different from the car's signal
            if(activeLight != light.lightID){
                // Compute the offset to add to waiting time
                int difference;
                if(activeLight > light.lightID){
                    difference = 3 + light.lightID - activeLight;
                }
                else difference = light.lightID - activeLight;
                // Schedule departure at the time when the light next becomes Green
                departureTime = difference*60 - currentTime;
            }
            else{
                // Find how much time has elapsed since the last car has run to check it if it has passed
                if(currentTime-light.previousRunTime > 6){
                    // If another car can be scheduled before the signal becomes Red, allow it to pass
                    if(currentTime < 54){
                        light.previousRunTime = currentTime;
                        departureTime = -1;
                        status = "Passing";
                        check = true;
                    }
                    // Else schedule it to pass when the signal becomes Green again
                    else{
                        light.previousRunTime = (180-currentTime)%60;
                        departureTime = 180-currentTime;
                    }
                }
                // If another car is still passing
                else{
                    // If another car can be scheduled before the signal becomes Red, schedule it to pass when the
                    // passing car has passed
                    if(light.previousRunTime <= 48){
                        departureTime = 6 - (currentTime - light.previousRunTime);
                    }
                    // Else schedule it to leave when the signal next becomes Green
                    else{
                        departureTime = 180 - currentTime;
                    }
                }
            }
            // Update the last waiting car time
            light.lastWaitingTime = departureTime + 6;
            if(!check) light.waitingCars++; 
        }
        else {
            int waitingTime = light.lastWaitingTime;
            // If last waiting car won't allow for another car to pass before the signal turns "Red"
            // schedule departure when the light next turns Green
            if(light.lastWaitingTime%60 > 54){
                waitingTime += 180 - light.lastWaitingTime%60;
            }
            else if (light.lastWaitingTime != 0 && light.lastWaitingTime%60 == 0) {
                waitingTime += 120;
            }
            // Set departure time and update last waiting car time to the time when this car would have passed
            departureTime = waitingTime;
            light.lastWaitingTime = departureTime + 6;        
            light.waitingCars++;
        }
    }
}