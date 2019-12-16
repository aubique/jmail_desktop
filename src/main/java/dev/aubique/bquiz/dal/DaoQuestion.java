package dev.aubique.bquiz.dal;

import dev.aubique.bquiz.model.BoQuestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DaoQuestion {

    String table;
    private int lastIndex = 1;
    private List<String> columnNames = new ArrayList<>();
    private ConnectionConfigurable database;

    public DaoQuestion() {
        this.database = new MariaDBConnection();
    }

    public int getLastIndex() {
        return this.lastIndex;
    }

    /**
     * Increment index of the last row in the Database
     * Gets called during adding question process
     */
    public void incLastIndex() {
        this.lastIndex++;
    }

    /**
     * Try to create the [questions] table
     * Initialize table, Column names, Column types that are used later on
     */
    public void createTable() {
        List<String> columnTypes = new ArrayList<>();
        Map<String, String> row;

        this.table = Options.QUESTIONS_TABLE;
        columnNames.add(Options.QUESTION_FIELD);
        columnNames.add(Options.CORRECT_FIELD);
        columnTypes.add("VARCHAR(128) NOT NULL");
        columnTypes.add("VARCHAR(32) NOT NULL");

        row = Utility.generateDict(columnNames, columnTypes);
        try (
                Connection conn = database.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(Utility.generateCreateQuery(this.table, row));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all question in DB and return them as List<Question>
     *
     * @return List containing questions to be represented in View
     */
    public List<BoQuestion> selectAll() {
        List<BoQuestion> selectedBoQuestions = new ArrayList<>();
        List<String> properties;
        int id = 0;

        try (
                Connection conn = database.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(Utility.generateSelectAllQuery(this.table))
        ) {
            while (res.next()) {
                properties = new ArrayList<>();
                id = res.getInt(Options.getIdField());
                for (String s : columnNames) {
                    properties.add(res.getString(s));
                }
                selectedBoQuestions.add(new BoQuestion(id, properties));
            }
            // Set the last assigned question ID as the table last index
            this.lastIndex = id;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return selectedBoQuestions;
    }

    /**
     * Insert a Question object as row to the database
     * Prepare a statement with column names and set values afterwards
     *
     * @param boQuestionToAdd (Question) object that may be inserted to the Database
     */
    public void insertQuestion(BoQuestion boQuestionToAdd) {
        List<String> properties = boQuestionToAdd.getProperties();
        try (
                Connection conn = database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(Utility.generateInsertQuery(this.table, columnNames))
        ) {
            for (int i = 0; i < properties.size(); i++) {
                pstmt.setString(i + 1, properties.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update question present already in the Database
     * Set Column values in PreparedStatement while iterating Question properties
     * PreparedStatement first index is [1], whereas List<String> properties starts off with [0]
     *
     * @param boQuestionToReplace (Question) object that is replacing the existing one
     */
    public void updateQuestion(BoQuestion boQuestionToReplace) {
        List<String> properties = boQuestionToReplace.getProperties();
        int i;
        try (
                Connection conn = database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(Utility.generateUpdateQueryById(this.table, columnNames))
        ) {
            for (i = 0; i < properties.size(); i++) {
                pstmt.setString(i + 1, properties.get(i));
            }
            pstmt.setInt(i + 1, boQuestionToReplace.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete question by the given ID
     *
     * @param questionId Question index in DB
     */
    public void deleteQuestion(int questionId) {
        try (
                Connection conn = database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(Utility.generateDeleteQueryById(this.table))
        ) {
            pstmt.setInt(1, questionId);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class Utility {

        /**
         * Pair up Column names and Column values and return dictionary
         *
         * @param columns Column names
         * @param values  Column values
         * @return Dictionary to be used for dynamic query generation
         */
        private static Map<String, String> generateDict(List<String> columns, List<String> values) {
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
        private static String generateCreateQuery(String table, Map<String, String> kwargs) {
            StringBuilder query = new StringBuilder();

            query.append(String.format("CREATE TABLE IF NOT EXISTS %s", table));
            query.append("(id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY");
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
    }

    public static class Options {

        protected static final String QUESTIONS_TABLE = "questions";
        protected static final String ID_FIELD = "id";
        protected static final String QUESTION_FIELD = "question";
        protected static final String CORRECT_FIELD = "correct";
        protected static final String ANSWER1_FIELD = "answer1";
        protected static final String ANSWER2_FIELD = "answer2";
        protected static final String ANSWER3_FIELD = "answer3";

        public static String getIdField() {
            return ID_FIELD;
        }
    }
}
