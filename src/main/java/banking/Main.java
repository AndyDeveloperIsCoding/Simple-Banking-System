package banking;

import banking.database.CreateDatabaseAndTable;
import banking.twoMenus.menusUserInterfaces.MainMenuUi;

public class Main {
    public static void main(String[] args) {

        // Read database name from Main class arguments
        fileName = args[1];
        // Create database at the time of running the program
        CreateDatabaseAndTable.createDatabaseAndTable();

        //Launch User Interface
        MainMenuUi.mainMenu();
    }

    public static String fileName = "";
    // public static String cardNumber;
    // public static String pinNumber;
    public static double accountBalance;
    public static boolean loggedOut = false;
}