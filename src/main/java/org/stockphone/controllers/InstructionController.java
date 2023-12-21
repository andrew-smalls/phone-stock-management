package org.stockphone.controllers;

public class InstructionController {

    private final Instructions instructions;

    public InstructionController() {
        instructions = new Instructions();
    }

    public InstructionController(Instructions instructions) {
        this.instructions = instructions;
    }

    public void executeOption(String option, String[] arguments) {
        if (option == null) {
            System.out.println("Unprocessable option. Skipped.");
        } else if ("list".equals(option)) {
            instructions.list();
        } else if ("add".equals(option)) {
            instructions.add(arguments);
        } else if ("update".equals(option)) {
            instructions.update(arguments);
        } else if ("search".equals(option)) {
            instructions.search(arguments);
        } else if ("clear".equals(option)) {
            instructions.clear();
        } else if ("trend".equals(option)) {
            instructions.trend(); // TODO: specify this doesn't take arguments in project description
        } else if ("history".equals(option)) {
            instructions.history();
        } else if ("list_resellers".equals(option)) {
            instructions.listResellers();
        } else if ("add_reseller".equals(option)) {
            instructions.addReseller(arguments);
        } else if ("delete_reseller".equals(option)) {
            instructions.deleteReseller(arguments);
        } else if ("assign_phone".equals(option)) {
            instructions.assignPhone(arguments);
        } else if ("deduct_stock".equals(option)) {
            instructions.deductStock(arguments);
        } else {
            System.out.println("Unprocessable option. Skipped: " + option);
        }
    }
}

