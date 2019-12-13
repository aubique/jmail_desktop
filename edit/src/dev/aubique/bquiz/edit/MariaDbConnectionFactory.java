package dev.aubique.bquiz.edit;

import org.mariadb.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connect to Database
 */
public class MariaDbConnectionFactory {
    protected static final String HOST = "jdbc:mariadb://localhost:3306/devcolibri";
    protected static final String USER = "idea";
    protected static final String PASS = "idea";

    /**
     * Get a connection with preset configuration
     *
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(HOST, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connection to MariaDB", e);
        }
    }
}
