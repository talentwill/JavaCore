package core2.chapter07.demo1;

import java.sql.*;

public class SelectTest {
    public static void main(String[] args) {
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
            String queryBooks = "select bookid, bookname, price from t_book order by bookid";
            ResultSet resultSet = statement.executeQuery(queryBooks);

            // traverse results
            while (resultSet.next()) {
                int bookid = resultSet.getInt(1);
                String bookname = resultSet.getString(2);
                int price = resultSet.getInt("price");
                System.out.println(bookid + "," + bookname + "," + price);
            }
            // close statement
            resultSet.close();
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
