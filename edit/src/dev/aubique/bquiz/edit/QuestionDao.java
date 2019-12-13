package dev.aubique.bquiz.edit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestionDao {

    private static int lastIndex = 1;
    String table;
    private List<String> columnNames = new ArrayList<>();

    public QuestionDao() throws SQLException {
    }

    public static int getLastIndex() {
        return lastIndex;
    }

    private static void updateLastIndexByResultSet(ResultSet resultSet) throws SQLException {
        lastIndex = resultSet.getRow();
    }

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
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateCreateQuery(table, row))
        ) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<List<String>> selectAllRowsAsRowList() {
        List<List<String>> rows = new ArrayList<>();
        List<String> row;
        try (
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateSelectAll(this.table));
                ResultSet res = ps.executeQuery()
        ) {
            updateLastIndexByResultSet(res);
            while (res.next()) {
                row = new ArrayList<>();
                // First column is always (int) primary key ID
                row.add(String.valueOf(res.getInt(1)));
                // The next columns are listed in columnNames
                for (String fieldName : columnNames) {
                    row.add(res.getString(fieldName));
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    public ResultSet selectAllRowsAsResultSet() {
        ResultSet res = null;
        try (
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateSelectAll(this.table))
        ) {
            res = ps.executeQuery();
            updateLastIndexByResultSet(res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void addQuestion(List<String> properties) {
        Map<String, String> row = Utility.generateDict(columnNames, properties);
        try (
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateInsertQuery(this.table, row))
        ) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuestion(Question questionToReplace) {
        List<String> properties = questionToReplace.getProperties();
        Map<String, String> row = Utility.generateDict(columnNames, properties);
        try (
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateUpdateQueryById(
                        this.table, row, questionToReplace.getId()))
        ) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRow() {
    }

    private static class Utility {

        private static Map<String, String> generateDict(List<String> columns, List<String> values) {
            Map<String, String> map = new LinkedHashMap<>();

            if (columns.size() != values.size())
                throw new IllegalArgumentException("Cannot combine two lists with dissimilar sizes");
            for (int i = 0; i < columns.size(); i++)
                map.put(columns.get(i), values.get(i));

            return map;
        }

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

        public static String generateSelectAll(String table) {
            String query = "SELECT * FROM " + table + ";";

            System.out.println(query);
            return query;
        }

        public static String generateInsertQuery(String table, Map<String, String> kwargs) {
            String columnString = String.format("INSERT INTO %s (", table);
            String columnValues = " VALUES (";

            for (Map.Entry<String, String> entry : kwargs.entrySet()) {
                columnString += entry.getKey() + ',';
                columnValues += String.format("\"%s\",", entry.getValue().replaceAll("\"", "\'"));
            }
            columnString = columnString.substring(0, columnString.length() - 1) + ')';
            columnValues = columnValues.substring(0, columnValues.length() - 1) + ')';

            System.out.println(columnString + columnValues);
            return columnString + columnValues;
        }

        public static String generateUpdateQueryById(String table, Map<String, String> kwargs, int id) {
            // TODO: Keep coding here
            return "";
        }
    }

    private static class Options {

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

        public static String getQuestionField() {
            return QUESTION_FIELD;
        }

        public static String getCorrectField() {
            return CORRECT_FIELD;
        }
    }
}
