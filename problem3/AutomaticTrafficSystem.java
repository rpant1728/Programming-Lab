// package problem3;

// import Car;
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.table.*;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class AutomaticTrafficSystem {  
    static JTextArea curr_light =  null;
    static TrafficLight light1 = new TrafficLight();
    static TrafficLight light2 = new TrafficLight();
    static TrafficLight light3 = new TrafficLight();
    static List <Car> Cars = new ArrayList<Car>();

    public static void run_simulation(JTable table, JTextArea curr_light){
        Timer timer = new Timer(); 
        // creating an instance of task to be scheduled 
        TimerTask task = new Worker(light1, light2, light3, table, curr_light, Cars); 
          
        // scheduling the timer instance 
        timer.schedule(task, 100, 2000); 
    }
    public static void main(String[] args) {  
        int lightActive = 1;
        light1.isGreen = true;

        JFrame frame = new JFrame(); 

        JTextField source_text = new JTextField();
        JTextField dest_text = new JTextField();
        JTextField arrival_time = new JTextField();
        if(light1.isGreen) curr_light = new JTextArea("Light 1");
        if(light2.isGreen) curr_light = new JTextArea("Light 2");
        if(light3.isGreen) curr_light = new JTextArea("Light 3");
        curr_light.setBounds(20, 500, 460, 40);

        source_text.setBounds(100, 50, 200, 30);
        dest_text.setBounds(100, 90, 200, 30);
        arrival_time.setBounds(100, 130, 200, 30);

        JButton button = new JButton("Add car");
        button.setBounds(150, 170, 100, 40); 
        JButton button1 = new JButton("Run Simulation");
        button1.setBounds(150, 210, 100, 40); 

        String[] columnNames = {"Vehicle ID", "Source Direction", "Destination Direction", "Status", "Time Remaining"}; 
        JTable table = new JTable(new DefaultTableModel(columnNames, 0));

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String source_dir = source_text.getText();
                String dest_dir = dest_text.getText();
                int time = Integer.parseInt(arrival_time.getText());
                Car car = new Car(source_dir, dest_dir, time);
                if(source_dir.equals("South") && dest_dir.equals("East")){
                    car.departure_time = light1.set_departure_time(car, lightActive);
                }
                else if(source_dir.equals("West") && dest_dir.equals("South")){
                    car.departure_time = light2.set_departure_time(car, lightActive);
                }
                else if(source_dir.equals("East") && dest_dir.equals("West")){
                    car.departure_time = light3.set_departure_time(car, lightActive);
                }
                Cars.add(car);
                System.out.println(car.departure_time);
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                // model.addRow(new String[]{String.valueOf(car.vehicle_id), car.source_direction, car.dest_direction, car.status, String.valueOf(car.departure_time)});
            }
        }); 

        button1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // for(Car car: Cars){
                //     System.out.println(car.departure_time);
                // }
                run_simulation(table, curr_light);
            }
        });

        JPanel panel = new JPanel(); 
        panel.setBounds(20, 550, 460, 50);
        panel.setBackground(Color.red); 
        
        // frame.add(new Panel(){

        // })
        table.setBounds(20, 250, 460, 250);

        frame.add(button); 
        frame.add(button1);
        frame.add(source_text);
        frame.add(dest_text);
        frame.add(arrival_time);
        frame.add(table);
        frame.add(panel);
        
        frame.add(curr_light);
        frame.setSize(1000,1000); 
        frame.setLayout(null); 
        frame.setVisible(true);
    }
}  