package dev.aubique.bquiz.edit;

import javax.swing.*;

public class View {
    private JPanel mainPanel;
    private JButton confirmButton;
    private JButton addButton;
    private JList<String> questionJList;
    private JTextField questionTextField;
    private JScrollPane questionScrollPane;

    public View() {
        JFrame frame = new JFrame("View");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(JButton confirmButton) {
        this.confirmButton = confirmButton;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public void setAddButton(JButton addButton) {
        this.addButton = addButton;
    }

    public JList<String> getQuestionJList() {
        return questionJList;
    }

    public void setQuestionJList(JList<String> questionJList) {
        this.questionJList = questionJList;
    }

    public JTextField getQuestionTextField() {
        return questionTextField;
    }

    public void setQuestionTextField(JTextField questionTextField) {
        this.questionTextField = questionTextField;
    }

    public JScrollPane getQuestionScrollPane() {
        return questionScrollPane;
    }

    public void setQuestionScrollPane(JScrollPane questionScrollPane) {
        this.questionScrollPane = questionScrollPane;
    }
}
