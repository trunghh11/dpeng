package org.openjfx.dpeng.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.Driver;

public class JDBCUtil {
    public static Connection getConnection() {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new Driver());
            String URL = "jdbc:mySQL://localhost:3306/dictionaryDPeng";
            String username = "root";
            String password = "";

            connection = DriverManager.getConnection(URL, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
