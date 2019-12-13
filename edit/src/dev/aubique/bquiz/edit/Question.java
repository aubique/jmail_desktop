package dev.aubique.bquiz.edit;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {

    private int id;
    private String question;
    private String answer;

    public Question(int id, String question) {
        this(id, question, "");
    }

    public Question(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public Question(int id, List<String> properties) {
        this.id = id;
        this.question = properties.get(0);
        this.answer = properties.get(1);
    }

    public List<String> getProperties() {
        return new ArrayList<String>(Arrays.asList(question, answer));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return question;
    }
}
