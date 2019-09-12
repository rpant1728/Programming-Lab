public class Inventory {
    public static Integer small_shirts;
    public static Integer med_shirts;
    public static Integer large_shirts;
    public static Integer caps;

    public Inventory (int c, int s, int m, int l){
        caps = c;
        small_shirts = s;
        med_shirts = m;
        large_shirts = l;
    }

    public void print_inventory(){
        System.out.println("  C  |  S  |  M  |  L  ");
        System.out.printf("  %d  |  %d  |  %d  |  %d  ", caps, small_shirts, med_shirts, large_shirts);
        System.out.println();
    }
}