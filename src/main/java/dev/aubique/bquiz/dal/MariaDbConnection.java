package dev.aubique.bquiz.dal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class MariaDbConnection {
    private static final String configFileName = "config.xml";
    private static final String DB_NAME;
    private static final String HOST;
    private static final String USER;
    private static final String PASS;
    private static Properties settings;

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        settings = new Properties();
        try (
                InputStream resourcesConfig = loader.getResourceAsStream(configFileName)
        ) {
            settings.loadFromXML(resourcesConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DB_NAME = settings.getProperty("database", null);
        HOST = settings.getProperty("url", null);
        USER = settings.getProperty("username", null);
        PASS = settings.getProperty("password", null);
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(HOST, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connection to " + DB_NAME, e);
        }
    }

    /**
     * Pair up Column names and Column values and return dictionary
     *
     * @param columns Column names
     * @param values  Column values
     * @return Dictionary to be used for dynamic query generation
     */
    public static Map<String, String> generateDict(List<String> columns, List<String> values) {
        Map<String, String> map = new LinkedHashMap<>();

        if (columns.size() != values.size())
            throw new IllegalArgumentException("Cannot combine two lists with dissimilar sizes");
        for (int i = 0; i < columns.size(); i++)
            map.put(columns.get(i), values.get(i));

        return map;
    }

    /**
     * Generate dynamically [CREATE table IF NOT EXISTS] statement
     *
     * @param table  SQL table that is queried
     * @param kwargs Dictionary with Column Names and Column Types paired-up
     * @return SQL query formatted as text
     */
    public static String generateCreateQuery(String table, Map<String, String> kwargs) {
        StringBuilder query = new StringBuilder();

        query.append(String.format("CREATE TABLE IF NOT EXISTS %s", table));
        query.append(generateAutoIncPrimaryKey());
        for (Map.Entry<String, String> entry : kwargs.entrySet())
            query.append(String.format(",%s %s", entry.getKey(), entry.getValue()));
        query.append(")");

        System.out.println(query.toString());
        return query.toString();
    }

    /**
     * Generate [SELECT * FROM table] statement
     *
     * @param table Table with the data
     * @return SQL string query to execute later on
     */
    public static String generateSelectAllQuery(String table) {
        String query = "SELECT * FROM " + table;
        System.out.println(query);
        return query;
    }

    /**
     * Generate [INSERT INTO table (column) VALUES (?)] statement
     * That said, values aren't determined here
     *
     * @param table       Name of database Table
     * @param columnNames List for column names
     * @return Formatted SQL query
     */
    public static String generateInsertQuery(String table, List<String> columnNames) {
        StringBuilder columnsQueryPart = new StringBuilder(String.format("INSERT INTO %s (", table));
        StringBuilder valuesQueryPart = new StringBuilder(" VALUES (");

        for (String column : columnNames) {
            columnsQueryPart.append(column).append(',');
            valuesQueryPart.append("?,");
        }
        columnsQueryPart = new StringBuilder(columnsQueryPart.substring(0, columnsQueryPart.length() - 1) + ')');
        valuesQueryPart = new StringBuilder(valuesQueryPart.substring(0, valuesQueryPart.length() - 1) + ')');

        System.out.println(columnsQueryPart + valuesQueryPart.toString());
        return columnsQueryPart + valuesQueryPart.toString();
    }

    /**
     * Generate an SQL query to UPDATE by ID
     * Values are set up by PreparedStatement
     *
     * @param table       Name of database Table
     * @param columnNames List for column names
     * @return Formatted SQL query
     */
    public static String generateUpdateQueryById(String table, List<String> columnNames) {
        List<String> setValueStrings = new ArrayList<>();
        String query = String.format("UPDATE %s SET ", table);
        columnNames.forEach(v -> setValueStrings.add(String.format("%s=?", v)));
        query += String.join(",", setValueStrings);
        query += " WHERE id=?";

        System.out.println(query);
        return query;
    }

    /**
     * Generate [DELETE FROM table WHERE id=?] SQL PreparedStatement
     *
     * @param table DB Table to interact with
     * @return SQL String query to execute
     */
    public static String generateDeleteQueryById(String table) {
        String query = String.format("DELETE FROM %s WHERE id=?", table);

        System.out.println(query);
        return query;
    }

    /**
     * Generate the STRING type according to the Database SQL Dialect
     *
     * @param length Size of the string
     * @return SQL Dialect StringType
     */
    public static String generateStringType(int length) {
        return String.format("VARCHAR(%d) NOT NULL", length);
    }

    /**
     * Overloaded generator - String generateStringType(int length)
     *
     * @return SQL Dialect StringType
     */
    public static String generateStringType() {
        return generateStringType(32);
    }

    /**
     * Generate the AUTO INCREMENT PRIMARY KEY type according to the current Database
     *
     * @return SQL Dialect AutoIncrement_PK
     */
    public static String generateAutoIncPrimaryKey() {
        return "(id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY";
    }

}
