package banking.database;

import banking.Card;
import banking.Main;
import banking.twoMenus.menusMethods.MainMenuMethods;
import banking.twoMenus.menusUserInterfaces.LoggedInMenuUi;
import banking.twoMenus.menusUserInterfaces.MainMenuUi;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Scanner;

public class ReadFromDatabase {

    public static void loggingIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String userProvidedCardNumber = scanner.next();

        // If user has entered "0", then switch off the system immediately
        if (Long.valueOf(userProvidedCardNumber) == 0) {
            Main.loggedOut = true;
            MainMenuMethods.systemExit();
            return;
        }

        System.out.println("Enter your PIN:");
        String userProvidedPin = scanner.next();

        // If user has entered "0", then switch off the system immediately
        if (Integer.valueOf(userProvidedPin) == 0) {
            Main.loggedOut = true;
            MainMenuMethods.systemExit();
            return;
        }

        // Check the correctness of login data here
        if (ReadFromDatabase.loginCredentialsCheck(userProvidedCardNumber, userProvidedPin)) {
            System.out.println("You have successfully logged in!");
            System.out.println("");
            LoggedInMenuUi.loggedInMenu();
        } else {
            System.out.println("Wrong card number or PIN!");
            System.out.println("");
            MainMenuUi.mainMenu();
        }

        scanner.close();
    }

    private static boolean loginCredentialsCheck(String userProvidedCardNumber, String userProvidedPin) {

        boolean credentialsCorrect = false;

        String url = "jdbc:sqlite:" + Main.fileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        Statement credentialsCheckStatement = null;
        ResultSet databaseCardNumberAndPin = null;

        try {
            con = dataSource.getConnection();

            credentialsCheckStatement = con.createStatement();
            String readFromDatabase = "SELECT number, pin FROM card WHERE number = " + userProvidedCardNumber + ";"; // Previously:  WHERE number = " + userProvidedCardNumber + " AND pin = " + userProvidedPin + ";";
            databaseCardNumberAndPin = credentialsCheckStatement.executeQuery(readFromDatabase);

            String pinFromDatabase = "";
            String cardNumberFromDatabase = "";
            while (databaseCardNumberAndPin.next()) {
                cardNumberFromDatabase = databaseCardNumberAndPin.getString("number");
                pinFromDatabase = databaseCardNumberAndPin.getString("pin");
            }

            if (userProvidedCardNumber.equals(cardNumberFromDatabase) && userProvidedPin.equals(pinFromDatabase)) {
                credentialsCorrect = true;
                Card.cardNumber = userProvidedCardNumber;
                Card.pinNumber = userProvidedPin; // This is just for correctness of card data
            }

            /*
            // Temp display:
            System.out.println("User provided card: " + userProvidedCardNumber);
            System.out.println("System card: " + cardNumberFromDatabase);
            System.out.println("User provided card: " + userProvidedPin);
            System.out.println("System card: " + pinFromDatabase);
            // End of temp display
             */

            System.out.println("");

        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            if (credentialsCheckStatement != null) {
                try {
                    credentialsCheckStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (databaseCardNumberAndPin != null) {
                try {
                    databaseCardNumberAndPin.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return credentialsCorrect;
    }

    public static void displayAccountBalance() {

        String url = "jdbc:sqlite:" + Main.fileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        Statement balanceStatement = null;
        ResultSet balanceDisplay = null;
        // Temporary tools:
        Statement statement = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            balanceStatement = con.createStatement();
            String readBalanceSql = "SELECT balance FROM card WHERE number = " + Card.cardNumber + ";";
            balanceDisplay = balanceStatement.executeQuery(readBalanceSql);
            while (balanceDisplay.next()) {
                int balanceAmount = balanceDisplay.getInt("balance");
                Main.accountBalance = balanceAmount; // This value is used during money transfer service- to check the funds availability
            }

            // SELECT and display everything from the table. This is just for info

            statement = con.createStatement();
            rs = statement.executeQuery("SELECT * FROM card;");
            System.out.println("ID\tNumber\t\t\t\tPIN\t\tBalance");

            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("number");
                String pin = rs.getString("pin");
                String balance = rs.getString("balance");
                System.out.println(id + "\t" + number + "\t" + pin + "\t" + balance);

            }
            System.out.println("");

        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            if (balanceStatement != null) {
                try {
                    balanceStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (balanceDisplay != null) {
                try {
                    balanceDisplay.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean cardExists(String recipientCardNumber) {
        boolean cardExists = false;

        String url = "jdbc:sqlite:" + Main.fileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = dataSource.getConnection();

            // Statement creation
            String addIncomeSql = "UPDATE card SET balance = balance +0 WHERE number = ?";

            preparedStatement = con.prepareStatement(addIncomeSql);
            preparedStatement.setString(1, recipientCardNumber);

            // Statement execution
            execution = preparedStatement.executeUpdate();

            /*
            // SELECT and display everything from the table. This is just for info

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM card;");
            System.out.println("ID\tNumber\t\t\t\tPIN\t\tBalance");

            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("number");
                String pin = rs.getString("pin");
                String balance = rs.getString("balance");
                System.out.println(id + "\t" + number + "\t" + pin + "\t" + balance);

            }
            System.out.println("");
             */

        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (execution == 1) { // Test values
            cardExists = true;
        }

        return cardExists;
    }

    public static int execution = 0;

}
