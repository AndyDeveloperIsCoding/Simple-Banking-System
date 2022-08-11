package banking.database;

import banking.Card;
import banking.Main;
import banking.twoMenus.menusUserInterfaces.LoggedInMenuUi;
import banking.twoMenus.menusUserInterfaces.MainMenuUi;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Scanner;

public class WriteToDatabase {

    public static void addCardToDatabase() {

        String url = "jdbc:sqlite:" + Main.fileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatement = null;
        // Temporary tools:
        Statement statement = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            // Statement creation
            String makeAnEntry = "INSERT INTO card (number, pin) VALUES (?, ?)";

            preparedStatement = con.prepareStatement(makeAnEntry);
            preparedStatement.setString(1, Card.cardNumber);
            preparedStatement.setString(2, Card.pinNumber);

            // Statement execution
            preparedStatement.executeUpdate();


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

        } catch (SQLException e) {
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

        // Cleaning up the card details after it is created and added to the database
        Card.cardNumber = "";
        Card.pinNumber = "";
    }


    public static void addIncome(Scanner scanner) {
        System.out.println("Enter income:");
        int incomeToAdd = Integer.valueOf(scanner.next());

        String url = "jdbc:sqlite:" + Main.fileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatement = null;
        // Temporary tools:
        Statement statement = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            // Statement creation
            String addIncomeSql = "UPDATE card SET balance = balance +" + incomeToAdd + " WHERE number = ?";

            preparedStatement = con.prepareStatement(addIncomeSql);
            preparedStatement.setString(1, Card.cardNumber);

            // Statement execution
            preparedStatement.executeUpdate();


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
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Income was added!");
        System.out.println("");
        LoggedInMenuUi.loggedInMenu();

    }

    public static void doTransfer(Scanner scanner) {

        System.out.println("Enter card number:");
        String recipientCardNumber = scanner.next();

        // Check if the provided recipient card number is the payer's card number.
        // If not- check a) if the recipient card exists in the database and b) if the recipient card number meets the Luhn algorithm requirements

        if (recipientCardNumber.equals(Card.cardNumber)) {
            System.out.println("You can't transfer money to the same account!");
            LoggedInMenuUi.loggedInMenu();
        } else if (!Card.checkLuhnRules(recipientCardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            LoggedInMenuUi.loggedInMenu();
        } else if (!ReadFromDatabase.cardExists(recipientCardNumber)) {
            System.out.println("Such a card does not exist");
            LoggedInMenuUi.loggedInMenu();
        } else {
            System.out.println("Enter how much money you want to transfer:");
        }

        int amountToTransfer = Integer.valueOf(scanner.next());

        ReadFromDatabase.displayAccountBalance();
        if (Main.accountBalance >= amountToTransfer) {
            return;
        } else {
            System.out.println("Not enough money!");
            System.out.println("");
            LoggedInMenuUi.loggedInMenu();
        }

        String url = "jdbc:sqlite:" + Main.fileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatementSubtract = null;
        PreparedStatement preparedStatementAdd = null;
        // Temporary tools:
        Statement statement = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            // Statement creation
            String subtractFromCardSql = "UPDATE card SET balance = balance -" + amountToTransfer + " WHERE number = ?";
            String addToCardSql = "UPDATE card SET balance = balance +" + amountToTransfer + " WHERE number = ?";

            preparedStatementSubtract = con.prepareStatement(subtractFromCardSql);
            preparedStatementSubtract.setString(1, Card.cardNumber);

            preparedStatementAdd = con.prepareStatement(addToCardSql);
            preparedStatementAdd.setString(1, recipientCardNumber);

            // Statement execution
            preparedStatementSubtract.executeUpdate();
            preparedStatementAdd.executeUpdate();


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
            if (preparedStatementSubtract != null) {
                try {
                    preparedStatementSubtract.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatementAdd != null) {
                try {
                    preparedStatementAdd.close();
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
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Success!");
        System.out.println("");
        LoggedInMenuUi.loggedInMenu();

    }

    public static void closeAccount() {

        String url = "jdbc:sqlite:" + Main.fileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatement = null;
        // Temporary tools
        Statement statement = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            // Statement creation
            String closeAccountSql = "DELETE FROM card WHERE number = ?";

            preparedStatement = con.prepareStatement(closeAccountSql);
            preparedStatement.setString(1, Card.cardNumber);

            // Statement execution
            preparedStatement.executeUpdate();


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

        System.out.println("The account has been closed!");
        System.out.println("");
        MainMenuUi.mainMenu();

    }

}
