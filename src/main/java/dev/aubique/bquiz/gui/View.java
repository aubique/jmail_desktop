package dev.aubique.bquiz.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class View {
    public List<JTextField> textFieldList = new ArrayList<>();
    private JPanel mainPanel;
    private JButton editButton;
    private JButton addButton;
    private JList<String> questionJList;
    private JTextField questionTextField;
    private JScrollPane questionScrollPane;
    private JTextField correctAnswerTextField;
    private JButton deleteButton;
    private JButton exitButton;
    private JTextField secondAnswerTextField;
    private JTextField fourthAnswerTextField;
    private JTextField thirdAnswerTextField;

    public View() {
        JFrame frame = new JFrame("View");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        textFieldList.add(questionTextField);
        textFieldList.add(correctAnswerTextField);
        textFieldList.add(secondAnswerTextField);
        textFieldList.add(thirdAnswerTextField);
        textFieldList.add(fourthAnswerTextField);
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JList<String> getQuestionJList() {
        return questionJList;
    }

    public JTextField getQuestionTextField() {
        return questionTextField;
    }

    public JTextField getCorrectAnswerTextField() {
        return correctAnswerTextField;
    }

    public JTextField getSecondAnswerTextField() {
        return secondAnswerTextField;
    }

    public JTextField getFourthAnswerTextField() {
        return fourthAnswerTextField;
    }

    public JTextField getThirdAnswerTextField() {
        return thirdAnswerTextField;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
