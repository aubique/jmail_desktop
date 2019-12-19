package dev.aubique.bquiz.dal;

import dev.aubique.bquiz.bll.BoQuestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionDao implements DefaultDao<BoQuestion> {

    String table;
    private int lastIndex = 1;
    private List<String> columnNames = new ArrayList<>();
    private MariaDbConnection database;

    public QuestionDao() {
        this.database = new MariaDbConnection();
    }

    @Override
    public int getLastIndex() {
        return this.lastIndex;
    }

    /**
     * Increment index of the last row in the Database
     * Gets called during adding question process
     */
    @Override
    public void incLastIndex() {
        this.lastIndex++;
    }

    /**
     * Try to create the [questions] table
     * Initialize table, Column names, Column types that are used later on
     */
    @Override
    public void createTable() {
        List<String> columnTypes = new ArrayList<>();
        Map<String, String> row;

        this.table = Options.QUESTIONS_TABLE;
        columnNames.add(Options.QUESTION_FIELD);
        columnNames.add(Options.ANSWER1_FIELD);
        columnNames.add(Options.ANSWER2_FIELD);
        columnNames.add(Options.ANSWER3_FIELD);
        columnNames.add(Options.ANSWER4_FIELD);

        columnTypes.add(MariaDbConnection.generateStringType(64));
        columnTypes.add(MariaDbConnection.generateStringType());
        columnTypes.add(MariaDbConnection.generateStringType());
        columnTypes.add(MariaDbConnection.generateStringType());
        columnTypes.add(MariaDbConnection.generateStringType());

        row = MariaDbConnection.generateDict(columnNames, columnTypes);
        try (
                Connection conn = MariaDbConnection.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(MariaDbConnection.generateCreateQuery(this.table, row));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Select all question in DB and return them as List<Question>
     *
     * @return List containing questions to be represented in View
     */
    @Override
    public List<BoQuestion> selectAll() {
        List<BoQuestion> selectedBoQuestions = new ArrayList<>();
        List<String> properties;
        int id = 0;

        try (
                Connection conn = MariaDbConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(MariaDbConnection.generateSelectAllQuery(this.table))
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
    @Override
    public void insert(BoQuestion boQuestionToAdd) {
        List<String> properties = boQuestionToAdd.getProperties();
        try (
                Connection conn = MariaDbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        MariaDbConnection.generateInsertQuery(this.table, columnNames))
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
    @Override
    public void update(BoQuestion boQuestionToReplace) {
        List<String> properties = boQuestionToReplace.getProperties();
        int i;
        try (
                Connection conn = MariaDbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(MariaDbConnection.generateUpdateQueryById(this.table, columnNames))
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
    @Override
    public void delete(int questionId) {
        try (
                Connection conn = MariaDbConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(MariaDbConnection.generateDeleteQueryById(this.table))
        ) {
            pstmt.setInt(1, questionId);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Options {

        protected static final String QUESTIONS_TABLE = "questions";
        protected static final String ID_FIELD = "id";
        protected static final String QUESTION_FIELD = "question";
        protected static final String ANSWER1_FIELD = "answer1";
        protected static final String ANSWER2_FIELD = "answer2";
        protected static final String ANSWER3_FIELD = "answer3";
        protected static final String ANSWER4_FIELD = "answer4";

        public static String getIdField() {
            return ID_FIELD;
        }
    }
}
