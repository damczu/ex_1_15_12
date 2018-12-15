package ex_1_15_12;

import com.mysql.jdbc.Driver;

import java.sql.*;
import java.util.Properties;

public class ShopApp {
    static final String DB_URL = "jdbc:mysql://localhost/productsbase";
    static final String USER = "userproduct";
    static final String PASS = "qwerty";


    public static void main(String[] args) {

        // register mysql driver
        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
            //we should not continue
        }

        // establish connection
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* zamiennie
        Properties properties = new Properties();
        properties.setProperty("user",USER);
        properties.setProperty("password", PASS);
        DriverManager.getConnection(DB_URL);
        */

        //query execution
        Statement statement = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE products " + "SET DESCRIPTION = ? WHERE product_id = ?"
            );
            updateProducts(preparedStatement);

            statement = connection.createStatement();
            insertProduct(statement);
            performQuery(statement);
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

            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        //close connection
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void updateProducts(PreparedStatement preparedStatement) {
        try {
            preparedStatement.setString(1, "bla");
            preparedStatement.setInt(2, 1);

            int updated = preparedStatement.executeUpdate();

            System.out.println(updated + " updated products.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertProduct(Statement statement) {
        try {
            int inserted = statement.executeUpdate("INSERT INTO products VALUES (1, '123a', 'first product', null)");
            System.out.println(inserted + " new products added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void performQuery(Statement statement) throws SQLException {
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery("SELECT product_id, name FROM products");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int product_id = resultSet.getInt("product_id");

                System.out.println("Product with id: " + product_id + " and name: " + name + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
