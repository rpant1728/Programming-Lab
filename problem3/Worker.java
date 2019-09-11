import javax.swing.*;
import javax.swing.table.*;

import java.util.List;
import java.util.TimerTask;

public class Worker extends TimerTask {
    public static TrafficLight light1, light2, light3;
    public static JTable table;
    public static JTextArea curr_light;
    public static List <Car> cars;
    public static Integer time = 0, activeLight = 1;

    public void updateRows(){
        for (int i = 0; i < table.getRowCount(); i++){
            String new_value = "";
            int dep_time = this.cars.get(i).departure_time;
            if(dep_time < 0) new_value = "-";
            else new_value = String.valueOf(dep_time);
            table.setValueAt(new_value, i, 4);
            table.setValueAt(this.cars.get(i).status, i, 3);
        }
    }

    public Worker(TrafficLight light1, TrafficLight light2, TrafficLight light3, JTable table, JTextArea curr_light, List <Car> cars){
        this.light1 = light1;
        this.light2 = light2;
        this.light3 = light3;
        this.table = table;
        this.curr_light = curr_light;
        this.cars = cars;
    }

    public void run() { 
        int i = 0;
        if(this.time % 60 == 0 && this.time != 0){
            if(this.activeLight == 1){
                this.activeLight = 2;
                light1.isGreen = false;
                light2.isGreen = true;
                curr_light.setText("Light 2");
            }
            else if(this.activeLight == 2){
                this.activeLight = 3;
                light2.isGreen = false;
                light3.isGreen = true;
                curr_light.setText("Light 3");
            }
            else if(this.activeLight == 3){
                this.activeLight = 1;
                light3.isGreen = false;
                light1.isGreen = true;
                curr_light.setText("Light 1");
            }
        }
        this.time++;
        System.out.println(this.time);
        for(Car car: cars){
            if(car.status == "Waiting"){
                if(car.departure_time == 0){
                    car.status = "Passing";
                }
                car.departure_time--;
            }
            else if(car.status == "Passing"){
                if(car.departure_time == -6){
                    car.status = "Passed";
                    continue;
                }
                car.departure_time--;
            }
        }
        // if(this.activeLight == 1){
        //     if(light1.waiting_cars.size() != 0){
        //         for (Car car : light1.waiting_cars) { 		
        //             car.departure_time--;
        //             if(car.status == "Waiting" && car.departure_time == 0){
        //                 car.status = "Passing";
        //             }
        //             else if(car.status == "Passing"){
        //                 if(car.departure_time > -6) car.departure_time--;
        //                 else light1.waiting_cars.remove(i);
        //             }
        //             // System.out.println(light1.waiting_cars.size());
        //             i++;
        //         }
        //     }
        // }
        // else if(activeLight == 2){
        //     if(light2.waiting_cars.size() != 0){
        //         for (Car car : light2.waiting_cars) { 		
        //             car.departure_time--;
        //             if(car.status == "Waiting" && car.departure_time == 0){
        //                 car.status = "Passing";
        //             }
        //             else if(car.status == "Passing"){
        //                 if(car.departure_time > -6) car.departure_time--;
        //                 else light2.waiting_cars.remove(i);
        //             }
        //             i++;
        //         }
        //     }
        // }
        // else{
        //     for (Car car : light3.waiting_cars) { 		
        //         car.departure_time--;
        //         if(car.status == "Waiting" && car.departure_time == 0){
        //             car.status = "Passing";
        //         }
        //         else if(car.status == "Passing"){
        //             if(car.departure_time > -6) car.departure_time--;
        //             else light3.waiting_cars.remove(i);
        //         }
        //         i++;
        //     }
        // }
        updateRows();
    } 
}