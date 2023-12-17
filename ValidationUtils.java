public class ValidationUtils {

    public boolean validSearchArguments(String[] arguments) {
        if (arguments.length != 2) {
            System.out.println("Invalid number of arguments");
            return false;
        }
        return true;
    }
    public boolean validAddArguments(String[] arguments) {
        if (arguments.length != 3) {
            System.out.println("Invalid number of arguments");
            return false;
        }
        try {
            Integer.parseInt(arguments[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid stock argument");
            return false;
        }
        return true;
    }
}
