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

    String table;
    private int lastIndex = 1;
    private List<String> columnNames = new ArrayList<>();

    public QuestionDao() {
    }

    public int getLastIndex() {
        return this.lastIndex;
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

    //TODO: Replace like selectAllQuestions() -> List<Question>
    public ResultSet selectAllRowsAsResultSet() {
        ResultSet res = null;
        try (
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateSelectAllQuery(this.table))
        ) {
            res = ps.executeQuery();
            lastIndex = res.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //TODO: Refactor like addQuestion(Question q) -> None
    public void addQuestion(List<String> properties) {
        Map<String, String> row = Utility.generateDict(columnNames, properties);
        try (
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateInsertQuery(this.table, row))
        ) {
            ps.execute();
            this.lastIndex++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuestion(Question questionToReplace) {
        List<String> properties = questionToReplace.getProperties();
        Map<String, String> row = Utility.generateDict(columnNames, properties);
        try (
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateUpdateQueryById(this.table, row))
        ) {
            ps.setInt(1, questionToReplace.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteQuestion(int questionId) {
        try (
                Connection conn = MariaDbConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(Utility.generateDeleteQueryById(this.table))
        ) {
            ps.setInt(1, questionId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        //TODO: Do not put values in generator, leave it for PreparedStatement setter
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

        public static String generateSelectAllQuery(String table) {
            String query = "SELECT * FROM " + table;

            System.out.println(query);
            return query;
        }

        public static String generateInsertQuery(String table, Map<String, String> kwargs) {
            String columnsQueryPart = String.format("INSERT INTO %s (", table);
            String valuesQueryPart = " VALUES (";

            for (Map.Entry<String, String> entry : kwargs.entrySet()) {
                columnsQueryPart += entry.getKey() + ',';
                valuesQueryPart += String.format("\"%s\",", entry.getValue().replaceAll("\"", "'"));
            }
            columnsQueryPart = columnsQueryPart.substring(0, columnsQueryPart.length() - 1) + ')';
            valuesQueryPart = valuesQueryPart.substring(0, valuesQueryPart.length() - 1) + ')';

            System.out.println(columnsQueryPart + valuesQueryPart);
            return columnsQueryPart + valuesQueryPart;
        }

        public static String generateUpdateQueryById(String table, Map<String, String> kwargs) {
            List<String> valuesToUpdate = new ArrayList<>();
            String query = String.format("UPDATE %s SET ", table);
            kwargs.forEach((k, v) -> valuesToUpdate.add(String.format("%s=\"%s\"", k, v)));
            query += String.join(",", valuesToUpdate);
            query += " WHERE id=?";

            System.out.println(query);
            return query;
        }

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
