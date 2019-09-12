public class Order {
    static int order_count = 0;
    String item;
    int quantity, id;

    public Order (String item, int quantity){
        this.item = item;
        this.quantity = quantity;
        this.id = ++order_count;
    }
}