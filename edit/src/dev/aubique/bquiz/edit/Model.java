package dev.aubique.bquiz.edit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private Question questionObj;
    private List<Question> questionList = new ArrayList<>();
    private ConnectionJdbc database;

    public Model() {
        try {
            this.database = new ConnectionJdbc();
            database.createDatabase();
        } catch (SQLException e) {
            System.out.println("Connection failed");
            //TODO throw new ConnectionException();
        }
    }

    public void loadQuestionListFromDatabase() {
        List<List<String>> rows = database.selectAllRows();
        for (List<String> row : rows) {
            int id = Integer.parseInt(row.get(0));
            String question = row.get(1);
            String correct = row.get(2);
            addQuestion(new Question(id, question, correct));
        }
    }

    public void addQuestion(Question questionToAdd) {
        this.questionObj = questionToAdd;
        questionObj.setId(questionList.size());
        questionList.add(questionObj);
    }

    public void updateQuestion(Question question) throws NotSelectedException {
        this.questionObj = question;
        try {
            questionList.set(questionObj.getId(), questionObj);
        } catch (IndexOutOfBoundsException e) {
            throw new NotSelectedException("Select an item to edit", e);
        }
    }

    public void removeQuestionAt(int indexSelected) throws NotSelectedException {
        try {
            questionList.remove(indexSelected);
        } catch (IndexOutOfBoundsException e) {
            throw new NotSelectedException("Select an item to remove", e);
        }
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
