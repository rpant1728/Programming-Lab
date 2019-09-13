import javax.swing.*;
import javax.swing.table.*;

import java.util.List;
import java.util.TimerTask;
import java.util.Queue;

public class Worker extends TimerTask {
    public static TrafficLight lightSE, lightWS, lightEW;
    public static JTable vehicleTable, lightTable;
    public static List <Car> cars;
    public static Integer currentTime = 0, activeLight = 1;
    public static Queue <Car> new_cars;
    public static int count = 0;
    // static ReentrantLock lock;

    public Worker(TrafficLight lightSE, TrafficLight lightWS, TrafficLight lightEW, JTable vehicleTable, 
            JTable lightTable, List <Car> cars, Queue <Car> new_cars){
        this.lightSE = lightSE;
        this.lightWS = lightWS;
        this.lightEW = lightEW;
        this.vehicleTable = vehicleTable;
        this.lightTable = lightTable;
        this.cars = cars;
        this.new_cars = new_cars;
    }

    public void updateRows(){
        DefaultTableModel vehicleModel = (DefaultTableModel) vehicleTable.getModel();
        DefaultTableModel lightModel = (DefaultTableModel) lightTable.getModel();

        synchronized(vehicleModel){
            for(Car car: this.cars){
                if(car.light == null){
                    if(car.arrivalTime == this.currentTime){
                        car.status = "Passing";
                        vehicleModel.addRow(new String[]{String.valueOf(car.vehicleID), car.sourceDirection, 
                            car.destinationDirection, car.status, String.valueOf(car.departureTime)});
                        car.departureTime = -1;
                    }
                    else if(car.status == "Passing"){
                        if(car.departureTime == -6) car.status = "Passed";
                        else car.departureTime--;
                    }
                }
                else{
                    if(car.arrivalTime == this.currentTime){
                        car.status = "Waiting";
                        car.set_departure_time(activeLight, currentTime%60);
                        vehicleModel.addRow(new String[]{String.valueOf(car.vehicleID), car.sourceDirection,
                             car.destinationDirection, car.status, String.valueOf(car.departureTime)});
                    }
                    else if(car.status == "Waiting"){
                        if(car.departureTime == 0){
                            car.light.previousRunTime = this.currentTime%60;
                            car.status = "Passing";
                            car.light.waitingCars--;
                        }
                        car.departureTime--;
                    }
                    else if(car.status == "Passing"){
                        if(car.departureTime == -6) car.status = "Passed";
                        else car.departureTime--;
                    }
                }
            }
            for (int i = 0; i < vehicleModel.getRowCount(); i++){
                String newValue = "";
                if(this.cars.get(i).departureTime < 0) newValue = "-";
                else newValue = String.valueOf(this.cars.get(i).departureTime);
                vehicleModel.setValueAt(newValue, i, 4);
                vehicleModel.setValueAt(this.cars.get(i).status, i, 3);
            }
            int i = activeLight - 1;
            int curr_time = Integer.parseInt((String) lightModel.getValueAt(i, 2));
            lightModel.setValueAt(String.valueOf(curr_time-1), i, 2);
        }
    }

    public void run() { 
        int i = 0;
        DefaultTableModel lightModel = (DefaultTableModel) lightTable.getModel();
        if(currentTime == 0) lightModel.setValueAt("61", 0, 2);
        if(this.currentTime % 60 == 0 && this.currentTime != 0){
            if(this.activeLight == 1){
                this.activeLight = 2;
                lightSE.isGreen = false;
                lightWS.isGreen = true;
                lightModel.setValueAt("Red", 0, 1);
                lightModel.setValueAt("Green", 1, 1);
                lightModel.setValueAt("-", 0, 2);
                lightModel.setValueAt("61", 1, 2);
            }
            else if(this.activeLight == 2){
                this.activeLight = 3;
                lightWS.isGreen = false;
                lightEW.isGreen = true;
                lightModel.setValueAt("Red", 1, 1);
                lightModel.setValueAt("Green", 2, 1);
                lightModel.setValueAt("-", 1, 2);
                lightModel.setValueAt("61", 2, 2);
            }
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
        if(lightSE.lastWaitingTime > 0) lightSE.lastWaitingTime--;
        if(lightWS.lastWaitingTime > 0) lightWS.lastWaitingTime--;
        if(lightEW.lastWaitingTime > 0) lightEW.lastWaitingTime--;

        if(new_cars.size() != 0){
            while(new_cars.size() != 0){
                Car car = new_cars.peek();
                car.arrivalTime = this.currentTime;
                this.cars.add(car);
    
                synchronized(new_cars){
                    new_cars.remove();
                }
            }
        }
        updateRows();
        this.currentTime++;
    } 
}