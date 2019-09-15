// Traffic class: model to represent the three traffic lights
public class TrafficLight {
    static int lightCount;                           // Maintains the count of lights created till now
    int lightID;                                     // Unique ID for traffic light
    boolean isGreen;                                 // Is set to true if light is Green, false otherwise
    int waitingCars;                                 // Number of cars waiting currently at the light
    int previousRunTime = -7;                        // Set to the time when the last car left the light
    int lastWaitingTime;                             // Stores the waiting time of the last car in the queue for the light

    // Constructor
    public TrafficLight(){
        this.lightID = ++lightCount;
        this.isGreen = false;
        this.waitingCars = 0;
    }
}