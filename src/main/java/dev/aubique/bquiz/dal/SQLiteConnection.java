package dev.aubique.bquiz.dal;

import org.mariadb.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connect to SQLite Database file
 */
public class SQLiteConnection implements ConnectionConfigurable {
    private static final String DB_NAME = "SQLite";
    private static final String HOST = "jdbc:sqlite:";

    /**
     * Get a connection with preset SQLite filepath
     *
     * @return Active connection with DB
     */
    @Override
    public Connection getConnection() {
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(HOST);
        } catch (SQLException e) {
            throw new RuntimeException("Error connection to " + DB_NAME, e);
        }
    }
}
