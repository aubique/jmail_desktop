package dev.aubique.bquiz.edit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Domain object is a List<Question>
public class Model {

    private List<Question> questionList = new ArrayList<>();
    private Question questionObj;
    private QuestionDao db;

    public Model() {
        this.db = new QuestionDao();
        db.createTable();
    }

    public void loadQuestionListFromDatabaseAsResultSet() {
        //TODO: Implement loadListAsQuestion()
        List<String> properties;
        ResultSet rows = db.selectAllRowsAsResultSet();
        try {
            while (rows.next()) {
                properties = new ArrayList<>();
                int id = rows.getInt(QuestionDao.Options.getIdField());
                for (String s : db.getColumnNames()) {
                    properties.add(rows.getString(s));
                }
                this.addQuestion(new Question(id, properties));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addQuestion(Question questionToAdd) {
        this.questionObj = questionToAdd;
        questionList.add(questionObj);
    }

    /**
     * Create new Question with the given properties
     * Add it to questionList stored by Model, then to database
     * Grub ID for the new Question from Database property
     *
     * @param properties - Question's (String)properties except (int)ID
     */
    public void addQuestion(List<String> properties) {
        int lastIdIndex = db.getLastIndex();
        this.questionObj = new Question(lastIdIndex, properties);
        questionList.add(questionObj);
        db.addQuestion(properties);
    }

    public void updateQuestion(int jListId, List<String> properties) throws NotSelectedException {
        if (jListId < 0) {
            throw new NotSelectedException(" to edit");
        }
        int databaseId = questionList.get(jListId).getId();
        this.questionObj = new Question(databaseId, properties);
        questionList.set(jListId, questionObj);
        db.updateQuestion(questionObj);
    }

    public void removeQuestionAt(int jListId) throws NotSelectedException {
        if (jListId < 0) {
            throw new NotSelectedException(" to remove");
        }
        int databaseId = questionList.get(jListId).getId();
        questionList.remove(jListId);
        db.deleteQuestion(databaseId);
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    @Override
    public String toString() {
        if (questionObj == null) {
            return "None";
        }
        return questionObj.getQuestion();
    }
}
