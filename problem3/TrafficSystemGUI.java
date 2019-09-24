import java.awt.BorderLayout;
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


// Main class which implements the GUI and spawns a timer thread which is scheduled to run each second
public class TrafficSystemGUI {  
    static TrafficLight lightSE = new TrafficLight();    // 3 traffic light objects for the three conflicting paths
    static TrafficLight lightWS = new TrafficLight();
    static TrafficLight lightEW = new TrafficLight();
    static List <Car> Cars = new ArrayList<Car>();       // List of all arrived cars
    static boolean isClicked = false;                    // Specifies if the "Run Simulation" has been clicked yet
    static JTable vehicleTable, lightTable;              // GUI tables for displaying vehicle and light status
    static ReentrantLock lock = new ReentrantLock();     // Lock for synchronization

    // Begins simulation of the traffic light system
    public static void run_simulation(JTable vehicleTable, JTable lightTable, Queue <Car> new_cars, ReentrantLock lock){
        Timer timer = new Timer(); 
        // Creating an instance of task to be scheduled 
        TimerTask task = new Worker(lightSE, lightWS, lightEW, vehicleTable, lightTable, Cars, new_cars, lock); 
          
        // Scheduling the timer instance 
        timer.schedule(task, 100, 1000); 
    }
    public static void main(String[] args) {  
        // Initialize queue to store newly arriving cars after simulation has begun
        Queue <Car> new_cars = new LinkedList <Car>();
        // Set the South-East traffic light as Green initially
        int activeLight = 1;
        lightSE.isGreen = true;

        // Create main UI frame
        JFrame frame = new JFrame("Automatic Traffic Light System"); 

        // Create and set positions for user input labels
        JLabel sourceLabel = new JLabel();
        JLabel destinationLabel = new JLabel();
        JLabel arrivalLabel = new JLabel();

        sourceLabel.setText("Source: ");
        destinationLabel.setText("Destination: ");
        arrivalLabel.setText("Arrival Time: ");

        sourceLabel.setBounds(250, 50, 100, 30);
        destinationLabel.setBounds(250, 90, 100, 30);
        arrivalLabel.setBounds(250, 130, 100, 30);

        // Create and set positions for user input textboxes
        JTextField sourceText = new JTextField(15);
        JTextField destText = new JTextField(15);
        JTextField arrivalTime = new JTextField(15);

        sourceText.setBounds(350, 50, 200, 30);
        destText.setBounds(350, 90, 200, 30);
        arrivalTime.setBounds(350, 130, 200, 30);

        // Add buttons to add cars and run simulation
        JButton addCarButton = new JButton("Add car");
        JButton runSimulationButton = new JButton("Run Simulation");

        addCarButton.setBounds(350, 180, 100, 40); 
        runSimulationButton.setBounds(300, 230, 200, 40); 

        // Create panels to display vehicle and traffic light status tables
        JPanel vehiclePanel = new JPanel();
        JPanel lightPanel = new JPanel();

        // Create vehicle status table
        String[] vehicleTableColumns = {"Vehicle ID", "Source", "Destination", "Status", "Time Left"}; 
        vehicleTable = new JTable(new DefaultTableModel(vehicleTableColumns, 0));

        // Create light status table
        String[] lightTableColumns = {"Light", "State", "Time"}; 
        lightTable = new JTable(new DefaultTableModel(lightTableColumns, 0));
        DefaultTableModel lightModel = (DefaultTableModel) lightTable.getModel();

        // Initialize traffic lights' status table
        lightModel.addRow(new String[]{"Light 1", "Green", "60"});
        lightModel.addRow(new String[]{"Light 2", "Red", "-"});
        lightModel.addRow(new String[]{"Light 3", "Red", "-"});

        // Add callback function for when the "Add car" button is clicked
        addCarButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                // Get user input for directions and arrival time
                String sourceDirection = sourceText.getText();
                String destDirection = destText.getText();

                // If input is invalid, return
                if(!sourceDirection.equals("N") && !sourceDirection.equals("E") && !sourceDirection.equals("W") && !sourceDirection.equals("S")){
                    return;
                }
                if(!destDirection.equals("N") && !destDirection.equals("E") && !destDirection.equals("W") && !destDirection.equals("S")){
                    return;
                }
                // Clear input fields
                // sourceText.setText("");
                // destText.setText("");
                int currentTime = 0;

                // If simulation  not running currently
                if(!isClicked) {
                    currentTime = Integer.parseInt(arrivalTime.getText());
                    if(currentTime < 0) System.out.println("Invalid Input! Arrival time should be greater than zero.");
                    // arrivalTime.setText("");
                }

                //Initialize new Car object
                Car car = null;
                
                // If simulation has not begun yet
                if(!isClicked){
                    // Create a car object and associate traffic light with its travel directions
                    if(sourceDirection.equals("S") && destDirection.equals("E")){
                        car = new Car(lightSE, sourceDirection, destDirection, currentTime);
                    }

                    else if(sourceDirection.equals("W") && destDirection.equals("S")){
                        car = new Car(lightWS, sourceDirection, destDirection, currentTime);
                    }

                    else if(sourceDirection.equals("E") && destDirection.equals("W")){
                        car = new Car(lightEW, sourceDirection, destDirection, currentTime);
                    }

                    else{
                        car = new Car(null, sourceDirection, destDirection, currentTime);
                        car.departureTime = 0;
                    }
                    // Update array of all cars arrived till now
                    Cars.add(car);

                }
                // If simulation is running
                else{
                    // Create a car object and associate traffic light with its travel directions
                    if(sourceDirection.equals("S") && destDirection.equals("E")){
                        car = new Car(lightSE, sourceDirection, destDirection, 0);
                    }

                    else if(sourceDirection.equals("W") && destDirection.equals("S")){
                        car = new Car(lightWS, sourceDirection, destDirection, 0);
                    }

                    else if(sourceDirection.equals("E") && destDirection.equals("W")){
                        car = new Car(lightEW, sourceDirection, destDirection, 0);
                    }

                    else{
                        car = new Car(null, sourceDirection, destDirection, 0);
                        car.departureTime = 0;
                    }

                    // Acquire lock to ensure synchronization while updating the queue shared by the Main thread and the TimerThread
                    lock.lock(); 
                    try{ 
                        // Add new car to queue
                        new_cars.add(car); 
                    } 
                    catch(Exception e){ 
                        e.printStackTrace(); 
                    } 
                    finally{
                        //  Release lock
                        lock.unlock(); 
                    } 
                }          
            }
        }); 

        // Add callback function to "Run Simulation" button
        runSimulationButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                // Begin traffic light system simulation
                run_simulation(vehicleTable, lightTable, new_cars, lock);
                isClicked = true;
                // Disable appropriate UI components
                arrivalTime.setEnabled(false);
                runSimulationButton.setEnabled(false);
            }
        });

        // Set position of UI tables
        vehiclePanel.add(new JScrollPane(vehicleTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        vehiclePanel.setBounds(100, 300, 600, 500);        

        lightPanel.setBounds(100, 825, 600, 75);
        lightPanel.add(new JScrollPane(lightTable), BorderLayout.CENTER);

        // Add UI components to parent frame
        frame.add(sourceLabel);
        frame.add(destinationLabel);
        frame.add(arrivalLabel);
        frame.add(sourceText);
        frame.add(destText);
        frame.add(arrivalTime);
        frame.add(addCarButton); 
        frame.add(runSimulationButton);
        frame.add(vehiclePanel);
        frame.add(lightPanel);
 
        // Set frame layout
        frame.setSize(800,1000); 
        frame.setLayout(null); 
        frame.setVisible(true);
    }
}  