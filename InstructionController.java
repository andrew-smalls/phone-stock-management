public class InstructionController {

    private final Instructions instructions = new Instructions();

    public void executeOption(String option, String[] arguments) {
        switch(option)
        {
            case "list":
                instructions.list();
                break;
            case "add":
                instructions.add(arguments);
                break;
            case "update":
                instructions.update(arguments);
                break;
            case "search":
                instructions.search(arguments);
                break;
            case "clear":
                instructions.clear();
                break;
            case "trend":
                instructions.trend(); //TODO: specify this doesn't take arguments in project description
                break;
            case "history":
                instructions.history();
                break;
            case "list_resellers":
                instructions.listResellers();
                break;
            case "add_reseller":
                instructions.addReseller(arguments);
                break;
            case "delete_reseller":
                instructions.deleteReseller(arguments);
                break;
            case "assign_phone":
                instructions.assignPhone(arguments);
                break;
            case "deduct_stock":
                instructions.deductStock(arguments);
                break;
            default:
                System.out.println("Unprocessable option. Skipped: " + option);
        }
    }
}

