import java.awt.event.*;  
import javax.swing.*;
import javax.swing.table.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

enum Directions {
    North,
    East,
    West,
    South
}

enum State {
    WAITING,
    PASSING,
    LEFT
}

class Car {
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

    // public Car (Directions source_dir, Directions dest_dir, int time){
    //     source_direction = source_dir;
    //     dest_direction = dest_dir;
    //     arrival_time = time;
    //     vehicle_id = car_count++;
    // }
}

class TrafficLight {
    boolean green;
    public Queue <Car> waiting_cars = new LinkedList <Car>();

    void add_to_queue(Car car){
        
    }
}

public class AutomaticTrafficSystem {  
    // static List <Car> vehicles = new ArrayList<>();
    public static void main(String[] args) {  
        TrafficLight light1 = new TrafficLight();
        TrafficLight light2 = new TrafficLight();
        TrafficLight light3 = new TrafficLight();

        JFrame frame = new JFrame(); 

        JTextField source_text = new JTextField();
        JTextField dest_text = new JTextField();

        source_text.setBounds(100, 50, 200, 30);
        dest_text.setBounds(100, 90, 200, 30);

        JButton button = new JButton("Add car");
        button.setBounds(150, 130, 100, 40); 

        String[] columnNames = {"Vehicle ID", "Source Direction", "Destination Direction", "Status", "Time Remaining"}; 
        JTable table = new JTable(new DefaultTableModel(columnNames, 0));

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String source_dir = source_text.getText();
                String dest_dir = dest_text.getText();
                Car car = new Car(source_dir, dest_dir, 0);
                if(source_dir.equals("South") && dest_dir.equals("East")){
                    light1.waiting_cars.add(car);
                }
                else if(source_dir.equals("West") && dest_dir.equals("South")){
                    light2.waiting_cars.add(car);
                }
                else if(source_dir.equals("East") && dest_dir.equals("West")){
                    light3.waiting_cars.add(car);
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new String[]{String.valueOf(car.vehicle_id), car.source_direction, car.dest_direction, car.status, String.valueOf(0)});
            }
        }); 
        table.setBounds(20, 200, 460, 250);

        frame.add(button); 
        frame.add(source_text);
        frame.add(dest_text);
        frame.add(table);
                
        frame.setSize(500,500); 
        frame.setLayout(null); 
        frame.setVisible(true);
    }  
}  