package dev.aubique.bquiz.edit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;

public class Controller {

    private Model model;
    private View view;
    private DefaultListModel<String> defaultQuestionList = new DefaultListModel<>();
    private String questionInput, correctAnswerInput;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        view.getQuestionJList().setModel(defaultQuestionList);
        view.getAddButton().addActionListener(new addButtonHandler());
        view.getQuestionJList().addListSelectionListener(new listSelectionHandler());
        view.getEditButton().addActionListener(new editButtonHandler());
        view.getDeleteButton().addActionListener(new deleteButtonHandler());
        view.getExitButton().addActionListener(new exitButtonHandler());
    }

    private Question getTextFields() {
        int indexSelected = view.getQuestionJList().getSelectedIndex();
        String questionInput = view.getQuestionTextField().getText();
        String correctAnswerInput = view.getCorrectAnswerTextField().getText();
        return new Question(indexSelected, questionInput, correctAnswerInput);
    }

    private void setTextFields(int index) throws NotSelectedException {
        try {
            Question selectedQuestion;
            selectedQuestion = model.getQuestionList().get(index);
            view.getQuestionTextField().setText(selectedQuestion.getQuestion());
            view.getCorrectAnswerTextField().setText(selectedQuestion.getAnswer());
        } catch (IndexOutOfBoundsException e) {
            throw new NotSelectedException("Item is deleted, select the new one", e);
        }
    }

    class addButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            model.addQuestion(getTextFields());
            // No need to go down Question's properties since we use only one
            defaultQuestionList.addElement(model.toString());
        }
    }

    class listSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            int indexSelected = view.getQuestionJList().getSelectedIndex();
            try {
                setTextFields(indexSelected);
            } catch (NotSelectedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    class editButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // TODO: Refine code for better reusability
            // TODO: Think of methods getQustionInput() and getIndexSelected()
            // It needs index to replace item on its initial place in the lists
            int indexSelected = view.getQuestionJList().getSelectedIndex();
            // Get the field required for DefaultListModel
            questionInput = view.getQuestionTextField().getText();
            // Replace question in Model and question String in DefaultListModel
            try {
                model.updateQuestion(getTextFields());
                defaultQuestionList.setElementAt(questionInput, indexSelected);
            } catch (NotSelectedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    class deleteButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int indexSelected = view.getQuestionJList().getSelectedIndex();
            try {
                model.removeQuestionAt(indexSelected);
                defaultQuestionList.removeElementAt(indexSelected);
            } catch (NotSelectedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    class exitButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
        }
    }
}
