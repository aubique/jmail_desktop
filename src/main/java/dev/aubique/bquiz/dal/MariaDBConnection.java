package dev.aubique.bquiz.dal;

import org.mariadb.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connect to MariaDB implementation
 */
public class MariaDBConnection implements ConnectionConfigurable {
    private final String DB_NAME = "MariaDB";
    private final String HOST = "jdbc:mariadb://localhost:3306/devcolibri";
    private final String USER = "idea";
    private final String PASS = "idea";
    DriverManager driver;

    MariaDBConnection() {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            System.out.println("Driver isn't instanced");
        }
    }

    /**
     * Get a connection with preset URL, Username, Password
     *
     * @return Active connection with DB
     */
    @Override
    public Connection getConnection() {
        try {
//            driver.registerDriver(new Driver());
            return DriverManager.getConnection(HOST, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connection to " + DB_NAME, e);
        }
    }
}
