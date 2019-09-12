import java.awt.event.*;  
import javax.swing.*;
import javax.swing.table.*;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock; 
import java.util.concurrent.CopyOnWriteArrayList;

public class AutomaticTrafficSystem {  
    static JTextArea curr_light =  null;
    static TrafficLight light1 = new TrafficLight();
    static TrafficLight light2 = new TrafficLight();
    static TrafficLight light3 = new TrafficLight();
    static List <Car> Cars = new ArrayList<Car>();
    static boolean clicked = false;
    static JTable table, table1;


    public static void run_simulation(JTable table, JTable table1, JTextArea curr_light, Queue <Car> new_cars){
        Timer timer = new Timer(); 
        // creating an instance of task to be scheduled 
        TimerTask task = new Worker(light1, light2, light3, table, table1, curr_light, Cars, new_cars); 
          
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

        source_text.setBounds(180, 50, 200, 30);
        dest_text.setBounds(180, 90, 200, 30);
        arrival_time.setBounds(180, 130, 200, 30);

        JButton button = new JButton("Add car");
        button.setBounds(230, 170, 100, 40); 
        JButton button1 = new JButton("Run Simulation");
        button1.setBounds(230, 210, 100, 40); 

        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        String[] columns = {"Vehicle ID", "Source Direction", "Destination Direction", "Status", "Time Remaining"}; 
        table = new JTable(new DefaultTableModel(columns, 0));

        String[] columns1 = {"Light", "State", "Time"}; 
        table1 = new JTable(new DefaultTableModel(columns1, 0));
        DefaultTableModel model1 = (DefaultTableModel) table1.getModel();
        model1.addRow(new String[]{"Light 1", "Green", "60"});
        model1.addRow(new String[]{"Light 2", "Red", "-"});
        model1.addRow(new String[]{"Light 3", "Red", "-"});

        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String source_dir = source_text.getText();
                String dest_dir = dest_text.getText();
                int time = 0;
                if(!clicked) time = Integer.parseInt(arrival_time.getText());;
                Car car = null;
                if(!clicked){
                    if(source_dir.equals("South") && dest_dir.equals("East")){
                        car = new Car(light1, source_dir, dest_dir, time);
                    }
                    else if(source_dir.equals("West") && dest_dir.equals("South")){
                        car = new Car(light2, source_dir, dest_dir, time);
                    }
                    else if(source_dir.equals("East") && dest_dir.equals("West")){
                        car = new Car(light3, source_dir, dest_dir, time);
                    }
                    else{
                        car = new Car(null, source_dir, dest_dir, time);
                        car.departure_time = 0;
                    }
                    Cars.add(car);

                }
                else{
                    if(source_dir.equals("South") && dest_dir.equals("East")){
                        car = new Car(light1, source_dir, dest_dir, 0);
                    }
                    else if(source_dir.equals("West") && dest_dir.equals("South")){
                        car = new Car(light2, source_dir, dest_dir, 0);
                    }
                    else if(source_dir.equals("East") && dest_dir.equals("West")){
                        car = new Car(light3, source_dir, dest_dir, 0);
                    }
                    else{
                        car = new Car(null, source_dir, dest_dir, 0);
                        car.departure_time = 0;
                    }
                    synchronized(new_cars){
                        new_cars.add(car);
                    }
                }          
            }
        }); 

        button1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                run_simulation(table, table1, curr_light, new_cars);
                clicked = true;
                arrival_time.setEnabled(false);
                button1.setEnabled(false);
            }
        });

        panel.setBounds(100, 250, 600, 250);
        panel.add(new JScrollPane(table));

        panel1.setBounds(100, 550, 600, 75);
        panel1.add(new JScrollPane(table1));

        frame.add(button); 
        frame.add(button1);
        frame.add(source_text);
        frame.add(dest_text);
        frame.add(arrival_time);
        frame.add(panel);
        frame.add(panel1);
 
        frame.setSize(1000,1000); 
        frame.setLayout(null); 
        frame.setVisible(true);
    }
}  