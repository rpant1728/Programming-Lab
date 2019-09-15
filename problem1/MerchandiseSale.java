import java.util.Scanner;

// Main class
public class MerchandiseSale {
    public static void main(String[] args){
        // Number of threads which will run the input simultaneously.
        int NUM_THREADS = 5;

        // Input scanner object for command line input.
        Scanner input = new Scanner(System.in);

        // Take the inventory as user input.
        System.out.println("Enter number of caps in inventory: ");
        int num_caps = input.nextInt();
        System.out.println("Enter number of small shirts in inventory: ");
        int num_small_shirts = input.nextInt();
        System.out.println("Enter number of medium shirts in inventory: ");
        int num_med_shirts = input.nextInt();
        System.out.println("Enter number of large shirts in inventory: ");
        int num_large_shirts = input.nextInt();

        // Create an inventory object.
        Inventory inventory = new Inventory(num_caps, num_small_shirts, num_med_shirts, num_large_shirts);

        // Take the number of orders as input.
        System.out.println("Number of students ordering: ");
        int total_orders = input.nextInt();
        
        // Creating an array of arrays where each array represents the orders that will be processed by a thread.
        // Thus, a total of NUM_THREAD such arrays.
        int size = (int) Math.ceil(((float) total_orders)/((float)NUM_THREADS));
        Order[][] orders = new Order[NUM_THREADS][size];

        int j = 0;
        // Distribute orders equally amongst the threads.
        for(int i=0; i<total_orders; i++){
            String item = input.next();
            int quantity = input.nextInt(); 
            orders[j][i/NUM_THREADS] = new Order(item, quantity);
            j = (j+1) % NUM_THREADS;
        }

        input.close();

        // Create and run threads. Each thread will run roughly the same number of orders sequentially.
        for(int k=0; k<total_orders && k<NUM_THREADS; k++){
            Worker worker = new Worker(inventory, orders[k]);
            Thread thread = new Thread(worker);
            thread.start();
        }
    }
}
