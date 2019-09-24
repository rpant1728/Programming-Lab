import java.util.Scanner;

// Main class
public class MerchandiseSale {
    public static void main(String[] args){
        // Number of threads which will run the input simultaneously.
        int NUM_THREADS = 100;

        // Input scanner object for command line input.
        Scanner input = new Scanner(System.in);
        boolean check = true;
        int num_caps = 0, num_small_shirts = 0, num_med_shirts = 0, num_large_shirts = 0, total_orders = 0;

        // Take the inventory as user input.
        while(check){
            System.out.println("Enter number of caps in inventory: ");
            num_caps = input.nextInt();
            if(num_caps < 0) {
                System.out.println("Invalid input! Number of caps should be >= 0. Please enter again.");
            }
            else check = false;
        }
        check = true;

        while(check){
            System.out.println("Enter number of small shirts in inventory: ");
            num_small_shirts = input.nextInt();
            if(num_small_shirts < 0) {
                System.out.println("Invalid input! Number of small shirts should be >= 0. Please enter again.");
            }
            else check = false;
        }
        check = true;

        while(check){
            System.out.println("Enter number of medium shirts in inventory: ");
            num_med_shirts = input.nextInt();
            if(num_med_shirts < 0) {
                System.out.println("Invalid input! Number of medium shirts should be >= 0. Please enter again.");
            }
            else check = false;
        }
        check = true;

        while(check){
            System.out.println("Enter number of large shirts in inventory: ");
            num_large_shirts = input.nextInt();
            if(num_large_shirts < 0) {
                System.out.println("Invalid input! Number of large shirts should be >= 0. Please enter again.");
            }
            else check = false;
        }
        check = true;
        

        // Create an inventory object.
        Inventory inventory = new Inventory(num_caps, num_small_shirts, num_med_shirts, num_large_shirts);

        // Take the number of orders as input.
        while(check){
            System.out.println("Number of students ordering: ");
            total_orders = input.nextInt();
            if(total_orders < 0) {
                System.out.println("Invalid input! Total orders should be >= 0. Please enter again.");
            }
            else check = false;
        }
        check = true;

        // Creating an array of arrays where each array represents the orders that will be processed by a thread.
        // Thus, a total of NUM_THREAD such arrays.
        int size = (int) Math.ceil(((float) total_orders)/((float)NUM_THREADS));
        Order[][] orders = new Order[NUM_THREADS][size];

        int j = 0;
        // Distribute orders equally amongst the threads.
        for(int i=0; i<total_orders; i++){
            String item = "";
            int quantity = 0;
            while(check){
                System.out.print((i+1) + " ");
                item = input.next();
                quantity = input.nextInt(); 
                if(!item.equals("C") && !item.equals("S") && !item.equals("M") && !item.equals("L")) {
                    System.out.println("Invalid input! Order item can be C, S, M, L only. Please enter again.");
                }
                else if(quantity <= 0){
                    System.out.println("Invalid input! Order quantity should be >= 0. Please enter again.");
                }
                else check = false;
            }
            check = true;
            
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
