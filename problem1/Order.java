// Order class: Stores all details pertaining to an order
public class Order {
    static int order_count = 0;     // Maintains a count of total orders created till now.
    String item;                    // Item ordered in ordered (Small Shirt/Cap/etc.)
    int quantity;                   // Number of items in order
    int id;                         // Unique order id.

    // Constructor
    public Order (String item, int quantity){
        this.item = item;
        this.quantity = quantity;
        this.id = ++order_count;
    }
}