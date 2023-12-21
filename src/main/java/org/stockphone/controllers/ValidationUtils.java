package org.stockphone.controllers;

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

    public boolean validAddResellerArguments(String[] arguments) {
        if (arguments.length != 2) {
            System.out.println("Invalid number of arguments");
            return false;
        }
        try {
            Integer.parseInt(arguments[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid reseller id argument");
            return false;
        }
        return true;
    }

    public boolean validDeleteResellerArguments(String[] arguments) {
        if (arguments.length != 1) {
            System.out.println("Invalid number of arguments");
            return false;
        }
        try {
            int result = Integer.parseInt(arguments[0]);
            if (result < 0) {
                System.out.println("Reseller id must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid reseller id argument");
            return false;
        }
        return true;
    }

    public boolean validAssignPhoneArguments(String[] arguments) {
        if (arguments.length != 4) {
            System.out.println("Invalid number of arguments");
            return false;
        }
        try {
            Integer.parseInt(arguments[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid reseller id argument");
            return false;
        }
        try {
            int result = Integer.parseInt(arguments[3]);
            if (result < 0) {
                System.out.println("Invalid quantity. Quantity must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity argument");
            return false;
        }
        return true;
    }
}
