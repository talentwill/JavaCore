package core2.chapter07.demo1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateTest {
    public static void main(String[] args) {
        executeUpdate("update t_book set price = 300 where bookid = 1");
        executeUpdate("insert into t_book(bookid, bookname, price) values(4, 'Biology', 90)");
        executeUpdate("delete from t_book where id = 4");
    }

    private static void executeUpdate(String sql) {
        // 1. Register mysql driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL driver registration succeeded.");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL driver registration failed.");
            return;
        }

        // 2. build MySQL connection string
        String url = "jdbc:mysql://localhost:33066/test";
        Connection connection = null;
        try {
            // try to connect to mysql server
            connection = DriverManager.getConnection(url, "root", "123456");

            // create SQL execution statement.
            Statement statement = connection.createStatement();

            // execute sql
            int result = statement.executeUpdate(sql);
            System.out.println(sql + ": " + (result == 1 ? "OK" : "NOK"));

            // close statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
