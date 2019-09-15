// Worker class: An extension to a thread. The run() function runs whenever a thread is started.
public class Worker implements Runnable {
    Inventory inventory;           // An instance of the current inventory.
    Order[] orders;                // A list of orders that the thraed has to process.

    // Constructor
    public Worker(Inventory inventory, Order[] orders){
        this.inventory = inventory;
        this.orders = orders;
    }

    // Prints order status and inventory
    public void print_inventory(boolean success, int order_id){
        // Get exclusive access on the entire inventory.
        synchronized(inventory){
            // Print order status and inventory.
            if(success) System.out.println("Order " + order_id + " succesful!");
            else System.out.println("Order " + order_id + " failed!");
            inventory.print_inventory();
        }
    }

    // Runs whenever a thread is started
    @Override
    public void run(){
        // Processing all orders one by one
        for(int i=0; i<orders.length; i++){
            boolean success = true;          // Is true if order successfully completed.

            if(orders[i] == null) break;

            // If order is for caps
            if(this.orders[i].item.equals("C")){
                // Get exclusive access on the count of caps in the inventory.
                synchronized(Inventory.caps){
                    // If sufficient number of caps present in inventory, subtract from the inventory to complete the order
                    if(this.orders[i].quantity <= Inventory.caps){
                        Inventory.caps -= this.orders[i].quantity;
                    }
                    // Else the order has failed
                    else success = false;
                }
                print_inventory(success, this.orders[i].id);
            }
            // If order is for small shirts
            else if(this.orders[i].item.equals("S")){
                // Get exclusive access on the count of small shirts in the inventory.
                synchronized(Inventory.small_shirts){
                    if(this.orders[i].quantity <= Inventory.small_shirts){
                        Inventory.small_shirts -= this.orders[i].quantity;
                    }
                    else success = false;
                }
                print_inventory(success, this.orders[i].id);
            }
            // If order is for medium shirts
            else if(this.orders[i].item.equals("M")){
                // Get exclusive access on the count of medium shirts in the inventory.
                synchronized(Inventory.med_shirts){
                    if(this.orders[i].quantity <= Inventory.med_shirts){
                        Inventory.med_shirts -= this.orders[i].quantity;
                    }
                    else success = false;
                }
                print_inventory(success, this.orders[i].id);
            }
            // If order is for large shirts
            else if(this.orders[i].item.equals("L")){
                // Get exclusive access on the count of large shirts in the inventory.
                synchronized(Inventory.large_shirts){
                    if(this.orders[i].quantity <= Inventory.large_shirts){
                        Inventory.large_shirts -= this.orders[i].quantity;
                    }
                    else success = false;
                }
                print_inventory(success, this.orders[i].id);
            }
        }
    }
}