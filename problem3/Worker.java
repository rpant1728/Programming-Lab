import javax.swing.*;
import javax.swing.table.*;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Queue;


// Class Worker: scheduled to run every second, updates GUI
public class Worker extends TimerTask {
    public static TrafficLight lightSE, lightWS, lightEW;     
    public static JTable vehicleTable, lightTable;             // Table showing current staus of vehicles and lights
    public static List <Car> cars;                             // List of all vehicles
    public static Integer currentTime = 0, activeLight = 1;    // Store the time elapsed and light currently Green
    public static Queue <Car> new_cars;                        // Stores newly entered cars via the GUI in the previous second
    public static ReentrantLock lock;                          // Lock over the queue of newly entered cars for synchronization

    // Constructor
    public Worker(TrafficLight lightSE, TrafficLight lightWS, TrafficLight lightEW, JTable vehicleTable, 
            JTable lightTable, List <Car> cars, Queue <Car> new_cars, ReentrantLock lock){

        this.lightSE = lightSE;
        this.lightWS = lightWS;
        this.lightEW = lightEW;
        this.vehicleTable = vehicleTable;
        this.lightTable = lightTable;
        this.cars = cars;
        this.new_cars = new_cars;
        this.lock = lock;

    }

    // Methood to fetch new cars that may have arrived in the past second
    public void getNewCars(){

        // Acquire lock to ensure synchronization while updating the queue shared by the Main thread and the TimerThread
        lock.lock(); 
        try{ 
            // If any new cars have arrived in the last second, then update the cars arrived till now 
            if(new_cars.size() != 0){
                while(new_cars.size() != 0){
                    // Get the front of the queue and update array of arrived cars
                    Car car = new_cars.peek();
                    car.arrivalTime = this.currentTime;
                    this.cars.add(car);

                    // Remove car from queue
                    new_cars.remove();
                }
            } 
        } 
        catch(Exception e){ 
            e.printStackTrace(); 
        } 
        finally{
            // Unlock the lock
            lock.unlock(); 
        } 

    }

    // Update the lights and display their status in the lights' table
    public void updateLightStatus(){

        DefaultTableModel lightModel = (DefaultTableModel) lightTable.getModel();
        if(currentTime == 0) lightModel.setValueAt("61", 0, 2);

        // Update light currently Green every 60 seconds and update lights table accordingly
        if(this.currentTime % 60 == 0 && this.currentTime != 0){
            // If light 1 is Green, make it Red and make light 2 Green, update light table accordingly
            if(this.activeLight == 1){
                this.activeLight = 2;
                lightSE.isGreen = false;
                lightWS.isGreen = true;
                lightModel.setValueAt("Red", 0, 1);
                lightModel.setValueAt("Green", 1, 1);
                lightModel.setValueAt("-", 0, 2);
                lightModel.setValueAt("61", 1, 2);
            }

            // If light 2 is Green, make it Red and make light 3 Green, update light table accordingly
            else if(this.activeLight == 2){
                this.activeLight = 3;
                lightWS.isGreen = false;
                lightEW.isGreen = true;
                lightModel.setValueAt("Red", 1, 1);
                lightModel.setValueAt("Green", 2, 1);
                lightModel.setValueAt("-", 1, 2);
                lightModel.setValueAt("61", 2, 2);
            }

            // If light 3 is Green, make it Red and make light 1 Green, update light table accordingly
            else if(this.activeLight == 3){
                this.activeLight = 1;
                lightEW.isGreen = false;
                lightSE.isGreen = true;
                lightModel.setValueAt("Red", 2, 1);
                lightModel.setValueAt("Green", 0, 1);
                lightModel.setValueAt("61", 0, 2);
                lightModel.setValueAt("-", 2, 2);
            }

        }

        // Decrement the last waiting time of each light by 1.
        // Last waiting time for a light is the the waiting time of the last waiting car for that light.
        if(lightSE.lastWaitingTime > 0) lightSE.lastWaitingTime--;
        if(lightWS.lastWaitingTime > 0) lightWS.lastWaitingTime--;
        if(lightEW.lastWaitingTime > 0) lightEW.lastWaitingTime--;

    }

    // Update the states of the cars 
    public void updateCarStatus(){

        DefaultTableModel vehicleModel = (DefaultTableModel) vehicleTable.getModel();

        for(Car car: this.cars){
            // If car is destined for a direction not governed by a traffic light
            if(car.light == null){
                // If it is time for the car to arrive, let it pass directly
                if(car.arrivalTime == this.currentTime){
                    car.status = "Passing";
                    // Add car to vehicles' table
                    vehicleModel.addRow(new String[]{String.valueOf(car.vehicleID), car.sourceDirection, 
                        car.destinationDirection, car.status, String.valueOf(car.departureTime)});
                    car.departureTime = -1;
                }

                // Allow the car to pass for 6 seconds, after which set it's status to "Passed"
                else if(car.status == "Passing"){
                    if(car.departureTime == -6) car.status = "Passed";
                    else car.departureTime--;
                }
            }

            else{
                // If it is time for the car to arrive, change car's status to "Waiting"
                if(car.arrivalTime == this.currentTime){
                    car.status = "Waiting";
                    // Schedule the departure of the car and add it to vehicles' table
                    car.set_departure_time(activeLight, currentTime%60);
                    vehicleModel.addRow(new String[]{String.valueOf(car.vehicleID), car.sourceDirection,
                        car.destinationDirection, car.status, String.valueOf(car.departureTime)});
                }

                // If car is already in "Waiting" state
                else if(car.status == "Waiting"){
                    // If it is time for it to leave, change its state to "Passing"
                    if(car.departureTime == 0){
                        // Update the last run time for the traffic light
                        car.light.previousRunTime = this.currentTime%60;
                        car.status = "Passing";
                        car.light.waitingCars--;
                    }
                    car.departureTime--;
                }

                // Allow the car to pass for 6 seconds, after which set it's status to "Passed"
                else if(car.status == "Passing"){
                    if(car.departureTime == -6) car.status = "Passed";
                    else car.departureTime--;
                }
            }
        }

    }

    // Update the tables with latest information
    public void updateGUI(){

        DefaultTableModel vehicleModel = (DefaultTableModel) vehicleTable.getModel();
        DefaultTableModel lightModel = (DefaultTableModel) lightTable.getModel();

        for (int i = 0; i < vehicleModel.getRowCount(); i++){
            String newValue = "";

            // If car's status is "Passing" or "Passed"
            if(this.cars.get(i).departureTime < 0){
                newValue = "-";
            }

            else {
                newValue = String.valueOf(this.cars.get(i).departureTime);
            }

            // Display latest departure time and car status
            vehicleModel.setValueAt(newValue, i, 4);
            vehicleModel.setValueAt(this.cars.get(i).status, i, 3);
        }

        // Update currently active light and time left after which it turns Red
        int index = activeLight - 1;
        int curr_time = Integer.parseInt((String) lightModel.getValueAt(index, 2));
        lightModel.setValueAt(String.valueOf(curr_time-1), index, 2);

    }

    // Function which executes every second
    public void run() { 
        getNewCars();
        updateLightStatus();
        updateCarStatus();
        updateGUI();
        // Update the time elapsed counter
        this.currentTime++;
    } 
}