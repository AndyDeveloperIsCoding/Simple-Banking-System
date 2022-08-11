package banking.twoMenus.menusUserInterfaces;

import banking.Main;
import banking.database.ReadFromDatabase;
import banking.twoMenus.menusMethods.MainMenuMethods;

import java.util.Scanner;

public class MainMenuUi {

    public static void mainMenu() {

        Scanner scanner = new Scanner(System.in);
        while (Main.loggedOut == false) {

            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");

            String userChoice = scanner.next();

            switch (userChoice) {
                case "1":
                    MainMenuMethods.createAccount();
                    break;
                case "2":
                    ReadFromDatabase.loggingIn();
                    break;
                case "0":
                    MainMenuMethods.systemExit();
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }

        }
        scanner.close();
    }

}

