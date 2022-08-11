package banking.twoMenus.menusUserInterfaces;

import banking.Main;
import banking.database.ReadFromDatabase;
import banking.database.WriteToDatabase;
import banking.twoMenus.menusMethods.LoggedInMenuMethods;
import banking.twoMenus.menusMethods.MainMenuMethods;

import java.util.Scanner;

public class LoggedInMenuUi {

    public static void loggedInMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");

        Scanner secondScanner = new Scanner(System.in);
        String userChoiceMainMenu = secondScanner.next();

        switch (userChoiceMainMenu) {
            case "1":
                ReadFromDatabase.displayAccountBalance();
                System.out.println("");
                System.out.println("Balance: " + Main.accountBalance);
                System.out.println("");
                loggedInMenu();
                break;
            case "2":
                WriteToDatabase.addIncome(secondScanner);
                break;
            case "3":
                WriteToDatabase.doTransfer(secondScanner);
                break;
            case "4":
                WriteToDatabase.closeAccount();
                break;
            case "5":
                LoggedInMenuMethods.logOut();
                break;
            case "0":
                MainMenuMethods.systemExit();
                break;
            default:
                System.out.println("Unknown command");
                break;
        }

        secondScanner.close();
    }
}
