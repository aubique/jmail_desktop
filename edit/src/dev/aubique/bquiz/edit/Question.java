package dev.aubique.bquiz.edit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {

    private int id;
    private String question;
    private String answer;

    public Question(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public Question(int id, List<String> properties) {
        this(id, properties.get(0), properties.get(1));
    }

    public List<String> getPropertiesAsList() {
        return new ArrayList<>(Arrays.asList(question, answer));
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return question;
    }
}
