import java.util.Scanner;

public class MerchandiseSale {
    public static void main(String[] args){
        int NUM_THREADS = 5;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter number of caps in inventory: ");
        int num_caps = input.nextInt();
        System.out.println("Enter number of small shirts in inventory: ");
        int num_small_shirts = input.nextInt();
        System.out.println("Enter number of medium shirts in inventory: ");
        int num_med_shirts = input.nextInt();
        System.out.println("Enter number of large shirts in inventory: ");
        int num_large_shirts = input.nextInt();

        Inventory inventory = new Inventory(num_caps, num_small_shirts, num_med_shirts, num_large_shirts);

        System.out.println("Number of students ordering: ");
        int total_orders = input.nextInt();
        
        int size = (int) Math.ceil(((float) total_orders)/((float)NUM_THREADS));
        Order[][] orders = new Order[NUM_THREADS][size];

        int j = 0;
        for(int i=0; i<total_orders; i++){
            String item = input.next();
            int quantity = input.nextInt(); 
            orders[j][i/NUM_THREADS] = new Order(item, quantity);
            j = (j+1) % NUM_THREADS;
        }

        input.close();

        for(int k=0; k<total_orders && k<NUM_THREADS; k++){
            Worker worker = new Worker(inventory, orders[k]);
            Thread thread = new Thread(worker);
            thread.start();
        }
    }
}
