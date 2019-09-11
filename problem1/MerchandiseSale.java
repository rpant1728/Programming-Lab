import java.util.*;

class Order {
    static int order_count = 0;
    String item;
    int quantity, id;

    public Order (String item, int quantity){
        this.item = item;
        this.quantity = quantity;
        this.id = ++this.order_count;
    }
}

class Inventory {
    public static Integer small_shirts;
    public static Integer med_shirts;
    public static Integer large_shirts;
    public static Integer caps;

    public Inventory (int c, int s, int m, int l){
        this.caps = c;
        this.small_shirts = s;
        this.med_shirts = m;
        this.large_shirts = l;
    }

    public void print_inventory(){
        System.out.println("  C  |  S  |  M  |  L  ");
        System.out.printf("  %d  |  %d  |  %d  |  %d  ", this.caps, this.small_shirts, this.med_shirts, this.large_shirts);
        System.out.println();
    }
}

class Worker implements Runnable {
    Inventory inventory;
    Order[] orders;

    public Worker(Inventory inventory, Order[] orders){
        this.inventory = inventory;
        this.orders = orders;
    }

    @Override
    public void run(){
        for(int i=0; i<orders.length; i++){
            if(orders[i] == null) break;
            boolean success = true;
            if(this.orders[i].item.equals("C")){
                synchronized(inventory.caps){
                    if(this.orders[i].quantity <= inventory.caps){
                        inventory.caps -= this.orders[i].quantity;
                    }
                    else success = false;
                }
                synchronized(inventory){
                    if(success) System.out.println("Order " + this.orders[i].id + " succesful!");
                    else System.out.println("Order " + this.orders[i].id + " failed!");
                    inventory.print_inventory();
                }
            }
            else if(this.orders[i].item.equals("S")){
                synchronized(inventory.small_shirts){
                    if(this.orders[i].quantity <= inventory.small_shirts){
                        inventory.small_shirts -= this.orders[i].quantity;
                    }
                    else success = false;
                }
                synchronized(inventory){
                    if(success) System.out.println("Order " + this.orders[i].id + " succesful!");
                    else System.out.println("Order " + this.orders[i].id + " failed!");
                    inventory.print_inventory();
                }
            }
            else if(this.orders[i].item.equals("M")){
                synchronized(inventory.med_shirts){
                    if(this.orders[i].quantity <= inventory.med_shirts){
                        inventory.med_shirts -= this.orders[i].quantity;
                    }
                    else success = false;
                }
                synchronized(inventory){
                    if(success) System.out.println("Order " + this.orders[i].id + " succesful!");
                    else System.out.println("Order " + this.orders[i].id + " failed!");
                    inventory.print_inventory();
                }
            }
            else if(this.orders[i].item.equals("L")){
                synchronized(inventory.large_shirts){
                    if(this.orders[i].quantity <= inventory.large_shirts){
                        inventory.large_shirts -= this.orders[i].quantity;
                    }
                    else success = false;
                }
                synchronized(inventory){
                    if(success) System.out.println("Order " + this.orders[i].id + " succesful!");
                    else System.out.println("Order " + this.orders[i].id + " failed!");
                    inventory.print_inventory();
                }
            }
        }
    }
}

public class MerchandiseSale {
    public static void main(String[] args){
        int NUM_THREADS = 5;

        Scanner in = new Scanner(System.in);
        System.out.println("Enter number of caps in inventory: ");
        int num_caps = in.nextInt();
        System.out.println("Enter number of small shirts in inventory: ");
        int num_small_shirts = in.nextInt();
        System.out.println("Enter number of medium shirts in inventory: ");
        int num_med_shirts = in.nextInt();
        System.out.println("Enter number of large shirts in inventory: ");
        int num_large_shirts = in.nextInt();

        Inventory inventory = new Inventory(num_caps, num_small_shirts, num_med_shirts, num_large_shirts);

        System.out.println("Number of students ordering: ");
        int total_orders = in.nextInt();
        
        int size = (int) Math.ceil(((float) total_orders)/((float)NUM_THREADS));
        Order[][] orders = new Order[NUM_THREADS][size];

        int j = 0;
        for(int i=0; i<total_orders; i++){
            String item = in.next();
            int quantity = in.nextInt(); 
            orders[j][i/NUM_THREADS] = new Order(item, quantity);
            j = (j+1) % NUM_THREADS;
        }

        for(int k=0; k<total_orders && k<NUM_THREADS; k++){
            Worker worker = new Worker(inventory, orders[k]);
            Thread thread = new Thread(worker);
            thread.start();
        }
    }
}
