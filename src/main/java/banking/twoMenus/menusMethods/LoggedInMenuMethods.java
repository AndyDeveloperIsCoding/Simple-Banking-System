package banking.twoMenus.menusMethods;

import banking.Card;

public class LoggedInMenuMethods {

    public static void logOut() {
        // Cleaning up the login credentials
        Card.cardNumber = "";
        Card.pinNumber = "";

        System.out.println("Bye!");
    }


}
