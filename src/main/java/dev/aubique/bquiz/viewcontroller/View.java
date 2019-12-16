package dev.aubique.bquiz.viewcontroller;

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

    public View() {
        JFrame frame = new JFrame("View");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        textFieldList.add(questionTextField);
        textFieldList.add(correctAnswerTextField);
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

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
