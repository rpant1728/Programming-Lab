public class Worker implements Runnable {
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
                synchronized(Inventory.caps){
                    if(this.orders[i].quantity <= Inventory.caps){
                        Inventory.caps -= this.orders[i].quantity;
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
                synchronized(Inventory.small_shirts){
                    if(this.orders[i].quantity <= Inventory.small_shirts){
                        Inventory.small_shirts -= this.orders[i].quantity;
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
                synchronized(Inventory.med_shirts){
                    if(this.orders[i].quantity <= Inventory.med_shirts){
                        Inventory.med_shirts -= this.orders[i].quantity;
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
                synchronized(Inventory.large_shirts){
                    if(this.orders[i].quantity <= Inventory.large_shirts){
                        Inventory.large_shirts -= this.orders[i].quantity;
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