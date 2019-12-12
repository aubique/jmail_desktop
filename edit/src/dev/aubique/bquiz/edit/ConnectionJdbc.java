package dev.aubique.bquiz.edit;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConnectionJdbc {

    public final Connection connection = DriverManager.getConnection(Options.HOST, Options.USER, Options.PASS);
    String table;
    private List<String> columnNames = new ArrayList<>();

    public ConnectionJdbc() throws SQLException {
    }

    public void createDatabase() {
        List<String> columnTypes = new ArrayList<>();
        Map<String, String> row;

        this.table = Options.QUESTIONS_TABLE;
        columnNames.add(Options.QUESTION_FIELD);
        columnNames.add(Options.CORRECT_FIELD);
        columnTypes.add("VARCHAR(128) NOT NULL");
        columnTypes.add("VARCHAR(32) NOT NULL");

        row = Utility.generateDict(columnNames, columnTypes);
        try (
                connection;
                PreparedStatement ps = connection.prepareStatement(Utility.generateCreateQuery(table, row))
        ) {
            ps.execute();
        } catch (SQLException e) {
            System.out.println("PreparedStatement exception");
            //TODO: throw new ConnectionNotEstablishedException(e);
        }
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    // TODO REWRITE
    public List<List<String>> selectAllRows() {
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        try (
                connection;
                PreparedStatement ps = connection.prepareStatement(Utility.generateSelectAll(this.table));
        ) {
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                columnNames.forEach(row::add);//TODO: FIX THAT
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Not managed to select all rows");
        }

        return rows;
    }

    private static class Utility {

        private static Map<String, String> generateDict(List<String> columns, List<String> values) {
            if (columns.size() != values.size())
                throw new IllegalArgumentException("Cannot combine two lists with dissimilar sizes");

            Map<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < columns.size(); i++)
                map.put(columns.get(i), values.get(i));

            return map;
        }

        private static String generateCreateQuery(String table, Map<String, String> kwargs) {
            StringBuilder query;
            query = new StringBuilder();
            query.append(String.format("CREATE TABLE IF NOT EXISTS %s", table));
            query.append("(id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY");
            for (Map.Entry<String, String> entry : kwargs.entrySet())
                query.append(String.format(",%s %s", entry.getKey(), entry.getValue()));
            query.append(")");

            System.out.println(query.toString());
            return query.toString();
        }

        public static String generateSelectAll(String table) {
            String query = "SELECT * FROM " + table;
            System.out.println(query);

            return query;
        }
    }

    private static class Options {
        protected static final String HOST = "jdbc:mariadb://localhost:3306/devcolibri";
        protected static final String USER = "idea";
        protected static final String PASS = "idea";

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
