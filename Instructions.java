import java.util.Arrays;

public class Instructions {

    public void update(String[] arguments) {
        System.out.println("Executing update" + Arrays.toString(arguments));
    }

    public void add(String[] arguments) {
        System.out.println("Executing add" + Arrays.toString(arguments));
    }

    public void list() {
        System.out.println("Executing list");
    }

    public void search(String[] arguments) {
        System.out.println("Executing search" + Arrays.toString(arguments));
    }

    public void clear() {
        System.out.println("Executing clear");
    }

    public void trend(String[] arguments) {
        System.out.println("Executing trend" + Arrays.toString(arguments));
    }

    public void history(String[] arguments) {
        System.out.println("Executing history" + Arrays.toString(arguments));
    }

    public void listResellers() {
        System.out.println("Executing list resellers");
    }

    public void addReseller(String[] arguments) {
        System.out.println("Executing add reseller" + Arrays.toString(arguments));
    }

    public void deleteReseller(String[] arguments) {
        System.out.println("Executing delete reseller" + Arrays.toString(arguments));
    }

    public void assignPhone(String[] arguments) {
        System.out.println("Executing assign phone" + Arrays.toString(arguments));
    }

    public void deductStock(String[] arguments) {
        System.out.println("Executing deduct stock" + Arrays.toString(arguments));
    }
}
