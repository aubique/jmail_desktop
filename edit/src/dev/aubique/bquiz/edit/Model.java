package dev.aubique.bquiz.edit;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private Question questionObj;
    private List<Question> questionList = new ArrayList<>();

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
