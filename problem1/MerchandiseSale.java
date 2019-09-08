import java.util.*;

class Order {
    String item;
    int quantity;

    public Order (String item, int quantity){
        this.item = item;
        this.quantity = quantity;
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
    Order order;
    Inventory inventory;
    int order_no;

    public Worker(Order order, Inventory inventory, int order_no){
        this.order = order;
        this.inventory = inventory;
        this.order_no = order_no;
    }

    @Override
    public void run(){
        boolean success = true;
        if(this.order.item.equals("C")){
            synchronized(inventory.caps){
                if(this.order.quantity <= inventory.caps){
                    inventory.caps -= this.order.quantity;
                }
                else success = false;
            }
            synchronized(inventory){
                if(success) System.out.println("Order " + this.order_no + " succesful!");
                else System.out.println("Order " + this.order_no + " failed!");
                inventory.print_inventory();
            }
        }
        if(this.order.item.equals("S")){
            synchronized(inventory.small_shirts){
                if(this.order.quantity <= inventory.small_shirts){
                    inventory.small_shirts -= this.order.quantity;
                }
                else success = false;
            }
            synchronized(inventory){
                if(success) System.out.println("Order " + this.order_no + " succesful!");
                else System.out.println("Order " + this.order_no + " failed!");
                inventory.print_inventory();
            }
        }
        if(this.order.item.equals("M")){
            synchronized(inventory.med_shirts){
                if(this.order.quantity <= inventory.med_shirts){
                    inventory.med_shirts -= this.order.quantity;
                }
                else success = false;
            }
            synchronized(inventory){
                if(success) System.out.println("Order " + this.order_no + " succesful!");
                else System.out.println("Order " + this.order_no + " failed!");
                inventory.print_inventory();
            }
        }
        if(this.order.item.equals("L")){
            synchronized(inventory.large_shirts){
                if(this.order.quantity <= inventory.large_shirts){
                    inventory.large_shirts -= this.order.quantity;
                }
                else success = false;
            }
            synchronized(inventory){
                if(success) System.out.println("Order " + this.order_no + " succesful!");
                else System.out.println("Order " + this.order_no + " failed!");
                inventory.print_inventory();
            }
        }
    }
}

public class MerchandiseSale {
    public static void main(String[] args){
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
        
        Order[] orders = new Order[total_orders];
        for(int i=0; i<total_orders; i++){
            String item = in.next();
            int quantity = in.nextInt();
            orders[i] = new Order(item, quantity);
        }
        for(int i=0; i<total_orders; i+=5){
            for(int j=i; j<total_orders && j<i+5; j++){
                Worker worker = new Worker(orders[j], inventory, j+1);
                Thread thread = new Thread(worker);
                thread.start();
            }
        }
    }
}
