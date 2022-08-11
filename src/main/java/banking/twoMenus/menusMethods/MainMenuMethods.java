package banking.twoMenus.menusMethods;

import banking.Card;
import banking.Main;
import banking.database.WriteToDatabase;

public class MainMenuMethods {

    public static void createAccount() {

        Card.generateCard();

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(Card.cardNumber);

        System.out.println("Your card PIN:");
        Card.generatePin();
        System.out.println("");

        // Enter tha card details to the database
        WriteToDatabase.addCardToDatabase();
    }

    public static void systemExit() {
        Main.loggedOut = true;
        System.out.println("You have successfully logged out");
        return;
    }


}
