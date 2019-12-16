package dev.aubique.bquiz.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoQuestion {

    private int id;
    private String question;
    private String answer;
    private List<String> properties;

    public BoQuestion(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.properties = new ArrayList<>(Arrays.asList(
                question, answer));
    }

    public BoQuestion(int id, List<String> properties) {
        this(id, properties.get(0), properties.get(1));
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

    public List<String> getProperties() {
        return properties;
    }
}
