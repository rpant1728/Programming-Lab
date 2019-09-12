import javax.swing.*;
import javax.swing.table.*;

import java.util.List;
import java.util.TimerTask;
import java.util.Queue;

public class Worker extends TimerTask {
    public static TrafficLight light1, light2, light3;
    public static JTable table, table1;
    public static JTextArea curr_light;
    public static List <Car> cars;
    public static Integer time = 0, activeLight = 1;
    public static Queue <Car> new_cars;
    public static int count = 0;

    public Worker(TrafficLight light1, TrafficLight light2, TrafficLight light3, JTable table, JTable table1, JTextArea curr_light, List <Car> cars, Queue <Car> new_cars){
        this.light1 = light1;
        this.light2 = light2;
        this.light3 = light3;
        this.table = table;
        this.table1 = table1;
        this.curr_light = curr_light;
        this.cars = cars;
        this.new_cars = new_cars;
    }

    public void updateRows(int time){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        DefaultTableModel model1 = (DefaultTableModel) table1.getModel();
        // model.addRow(new String[]{String.valueOf(1), "bnj", "hbnjkm", "hbjnkm", String.valueOf(time)});
        // for (int i = 0; i < model.getRowCount(); i++){
        //     // int val = Integer.parseInt(table.getValueAt(i, 4));
        //     model.setValueAt(String.valueOf(count+1), i, 4);
        //     count++;
        // }
        // model.addRow(new String[]{String.valueOf(car.vehicle_id), car.source_direction, car.dest_direction, car.status, String.valueOf(car.departure_time)});
        synchronized(model){
            // for(Car car: this.cars){
            //     if(car.arrival_time == this.time){
            //         model.addRow(new String[]{String.valueOf(car.vehicle_id), car.source_direction, car.dest_direction, car.status, String.valueOf(car.departure_time)});
            //     }
            // }
            // for (int i = 0; i < model.getRowCount(); i++){
            //     System.out.println(table.getValueAt(i, 3) + " " + table.getValueAt(i, 4));
            //     // if(table.getValueAt(i, 3) == "Passed") model.setValueAt("Passed", i, 3);
            //     String new_value = "";
            //     int dep_time = this.cars.get(i).departure_time;
            //     if(dep_time < 0) new_value = "-";
            //     else new_value = String.valueOf(dep_time);
            //     model.setValueAt(new_value, i, 4);
            //     model.setValueAt(this.cars.get(i).status, i, 3);
            // }
            for(int i=0; i<3; i++){
                int curr_time = Integer.parseInt((String) model1.getValueAt(i, 2));
                model1.setValueAt(String.valueOf(curr_time-1), i, 2);
            }
        }
    }

    public void run() { 
        int i = 0;
        DefaultTableModel model1 = (DefaultTableModel) table1.getModel();
        if(this.time % 60 == 0 && this.time != 0){
            if(this.activeLight == 1){
                this.activeLight = 2;
                light1.isGreen = false;
                light2.isGreen = true;
                model1.setValueAt("Red", 0, 1);
                model1.setValueAt("180", 0, 2);
                model1.setValueAt("Green", 1, 1);
                curr_light.setText("Light 2");
            }
            else if(this.activeLight == 2){
                this.activeLight = 3;
                light2.isGreen = false;
                light3.isGreen = true;
                model1.setValueAt("Red", 1, 1);
                model1.setValueAt("180", 1, 2);
                model1.setValueAt("Green", 2, 1);
                curr_light.setText("Light 3");
            }
            else if(this.activeLight == 3){
                this.activeLight = 1;
                light3.isGreen = false;
                light1.isGreen = true;
                model1.setValueAt("Red", 2, 1);
                model1.setValueAt("180", 2, 2);
                model1.setValueAt("Green", 0, 1);
                curr_light.setText("Light 1");
            }
        }
        this.time++;
        if(new_cars.size() != 0){
            Car car = new_cars.peek();
            car.arrival_time = this.time;
            car.set_departure_time(activeLight, this.time%60);
            this.cars.add(car);
            synchronized(new_cars){
                new_cars.remove();
            }
        }
        System.out.println(this.time);
        // int change = -1, j = 0;
        for(Car car: cars){
            if(car.status == "Waiting"){
                if(car.departure_time == 0){
                    car.status = "Passing";
                }
                car.light.waiting_cars--;
                car.departure_time--;
            }
            else if(car.status == "Passing"){
                if(car.departure_time == -6){
                    car.status = "Passed";
                    // change = j;
                    continue;
                }
                car.departure_time--;
            }
            // j++;
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
        // updateRows(change);
        updateRows(time);
    } 
}