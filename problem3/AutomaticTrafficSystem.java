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
    static boolean clicked = false;

    public static void run_simulation(DefaultTableModel model, JTextArea curr_light, Queue <Car> new_cars){
        Timer timer = new Timer(); 
        // creating an instance of task to be scheduled 
        TimerTask task = new Worker(light1, light2, light3, model, curr_light, Cars, new_cars); 
          
        // scheduling the timer instance 
        timer.schedule(task, 100, 1000); 
    }
    public static void main(String[] args) {  
        Queue <Car> new_cars = new LinkedList <Car>();
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

        String[] columns = {"Vehicle ID", "Source Direction", "Destination Direction", "Status", "Time Remaining"}; 
        JTable table = new JTable(new DefaultTableModel(columns, 0));

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String source_dir = source_text.getText();
                String dest_dir = dest_text.getText();
                int time = 0;
                if(clicked) time = Integer.parseInt(arrival_time.getText());;
                Car car;
                if(source_dir.equals("South") && dest_dir.equals("East")){
                    car = new Car(light1, time);
                    car.departure_time = light1.set_departure_time(car, lightActive);
                }
                else if(source_dir.equals("West") && dest_dir.equals("South")){
                    car = new Car(light2, time);
                    car.departure_time = light2.set_departure_time(car, lightActive);
                }
                else if(source_dir.equals("East") && dest_dir.equals("West")){
                    car = new Car(light3, time);
                    car.departure_time = light3.set_departure_time(car, lightActive);
                }
                if(clicked){
                    synchronized(new_cars){
                        new_cars.add(new Car(source_dir, dest_dir, 0));
                    }
                }
                else{
                    Cars.add(car);
                }
                // DefaultTableModel model = (DefaultTableModel) table.getModel();
                // model.setColumnIdentifiers(columns);                
            }
        }); 

        button1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // for(Car car: Cars){
                //     System.out.println(car.departure_time);
                // }
                run_simulation(model, curr_light, new_cars);
                clicked = true;
                arrival_time.setEnabled(false);
                button1.setEnabled(false);
            }
        });

        // JPanel panel = new JPanel(); 
        // panel.setBounds(20, 550, 460, 50);
        // panel.setBackground(Color.red); 
        
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