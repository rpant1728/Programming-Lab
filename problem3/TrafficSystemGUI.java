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

public class TrafficSystemGUI {  
    static TrafficLight lightSE = new TrafficLight();
    static TrafficLight lightWS = new TrafficLight();
    static TrafficLight lightEW = new TrafficLight();
    static List <Car> Cars = new ArrayList<Car>();
    static boolean isClicked = false;
    static JTable vehicleTable, lightTable;


    public static void run_simulation(JTable vehicleTable, JTable lightTable, Queue <Car> new_cars){
        Timer timer = new Timer(); 
        // creating an instance of task to be scheduled 
        TimerTask task = new Worker(lightSE, lightWS, lightEW, vehicleTable, lightTable, Cars, new_cars); 
          
        // scheduling the timer instance 
        timer.schedule(task, 100, 1000); 
    }
    public static void main(String[] args) {  
        Queue <Car> new_cars = new LinkedList <Car>();
        int activeLight = 1;
        lightSE.isGreen = true;

        JFrame frame = new JFrame(); 
        JTextField sourceText = new JTextField();
        JTextField destText = new JTextField();
        JTextField arrivalTime = new JTextField();

        sourceText.setBounds(180, 50, 200, 30);
        destText.setBounds(180, 90, 200, 30);
        arrivalTime.setBounds(180, 130, 200, 30);

        JButton addCarButton = new JButton("Add car");
        addCarButton.setBounds(230, 170, 100, 40); 
        JButton runSimulationButton = new JButton("Run Simulation");
        runSimulationButton.setBounds(230, 210, 100, 40); 

        JPanel vehiclePanel = new JPanel();
        JPanel lightPanel = new JPanel();
        String[] vehicleTableColumns = {"Vehicle ID", "Source Direction", "Destination Direction", "Status", "Time Remaining"}; 
        vehicleTable = new JTable(new DefaultTableModel(vehicleTableColumns, 0));

        String[] lightTableColumns = {"Light", "State", "Time"}; 
        lightTable = new JTable(new DefaultTableModel(lightTableColumns, 0));
        DefaultTableModel lightModel = (DefaultTableModel) lightTable.getModel();
        lightModel.addRow(new String[]{"Light 1", "Green", "60"});
        lightModel.addRow(new String[]{"Light 2", "Red", "-"});
        lightModel.addRow(new String[]{"Light 3", "Red", "-"});

        addCarButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                String sourceDirection = sourceText.getText();
                String destinationDirection = destText.getText();
                int currentTime = 0;
                if(!isClicked) currentTime = Integer.parseInt(arrivalTime.getText());;
                Car car = null;
                
                if(!isClicked){
                    if(sourceDirection.equals("South") && destinationDirection.equals("East")){
                        car = new Car(lightSE, sourceDirection, destinationDirection, currentTime);
                    }
                    else if(sourceDirection.equals("West") && destinationDirection.equals("South")){
                        car = new Car(lightWS, sourceDirection, destinationDirection, currentTime);
                    }
                    else if(sourceDirection.equals("East") && destinationDirection.equals("West")){
                        car = new Car(lightEW, sourceDirection, destinationDirection, currentTime);
                    }
                    else{
                        car = new Car(null, sourceDirection, destinationDirection, currentTime);
                        car.departureTime = 0;
                    }
                    Cars.add(car);

                }
                else{
                    if(sourceDirection.equals("South") && destinationDirection.equals("East")){
                        car = new Car(lightSE, sourceDirection, destinationDirection, 0);
                    }
                    else if(sourceDirection.equals("West") && destinationDirection.equals("South")){
                        car = new Car(lightWS, sourceDirection, destinationDirection, 0);
                    }
                    else if(sourceDirection.equals("East") && destinationDirection.equals("West")){
                        car = new Car(lightEW, sourceDirection, destinationDirection, 0);
                    }
                    else{
                        car = new Car(null, sourceDirection, destinationDirection, 0);
                        car.departureTime = 0;
                    }
                    synchronized(new_cars){
                        new_cars.add(car);
                    }
                }          
            }
        }); 

        runSimulationButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                run_simulation(vehicleTable, lightTable, new_cars);
                isClicked = true;
                arrivalTime.setEnabled(false);
                runSimulationButton.setEnabled(false);
            }
        });

        vehiclePanel.setBounds(100, 250, 600, 250);
        vehiclePanel.add(new JScrollPane(vehicleTable));

        lightPanel.setBounds(100, 550, 600, 75);
        lightPanel.add(new JScrollPane(lightTable));

        frame.add(addCarButton); 
        frame.add(runSimulationButton);
        frame.add(sourceText);
        frame.add(destText);
        frame.add(arrivalTime);
        frame.add(vehiclePanel);
        frame.add(lightPanel);
 
        frame.setSize(1000,1000); 
        frame.setLayout(null); 
        frame.setVisible(true);
    }
}  