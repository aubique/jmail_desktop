package dev.aubique.bquiz.bll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoQuestion {

    private int id;
    private String question;
    private String answerOne;
    private String answerTwo;
    private String answerThree;
    private String answerFour;
    private List<String> properties;

    public BoQuestion(
            int id, String question, String answerOne,
            String answerTwo, String answerThree, String answerFour) {
        this.id = id;
        this.question = question;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.answerFour = answerFour;
        this.properties = new ArrayList<>(Arrays.asList(
                question, answerOne, answerTwo, answerThree, answerFour));
    }

    public BoQuestion(int id, List<String> properties) {
        this(id, properties.get(0), properties.get(1),
                properties.get(2), properties.get(3), properties.get(4));
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerOne() {
        return answerOne;
    }

    @Override
    public String toString() {
        return question;
    }

    public List<String> getProperties() {
        return properties;
    }
}
