package banking.database;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDatabaseAndTable {

    public static void createDatabaseAndTable() {

        SQLiteDataSource dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + banking.Main.fileName;
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = dataSource.getConnection();

            String createTable = "CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER PRIMARY KEY," +
                    "number VARCHAR(16) NOT NULL," +
                    "pin VARCHAR(4) NOT NULL," +
                    "balance INTEGER NOT NULL DEFAULT 0);";
            // Statement creation
            preparedStatement = con.prepareStatement(createTable);
            // Statement execution
            preparedStatement.executeUpdate();

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
        }
    }
}
