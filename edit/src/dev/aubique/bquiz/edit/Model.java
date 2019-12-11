package dev.aubique.bquiz.edit;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private Question questionObj;
    private List<Question> questionList = new ArrayList<>();

    public Model() {
    }

    public void addQuestion(String question) {
        int id = questionList.size() - 1;
        // TODO: figure out shall we assign id or list would do his job?
        questionList.add(new Question(id, question));
    }

    public void replaceQuestion(int id, String question) {
        questionList.set(id, new Question(id, question));
    }

    public Question getQuestionObj() {
        return questionObj;
    }

    public void setQuestionObj(Question questionObj) {
        this.questionObj = questionObj;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    public String toString() {
        if (questionObj == null) {
            return "None";
        }
        return questionObj.getQuestion();
    }
}
