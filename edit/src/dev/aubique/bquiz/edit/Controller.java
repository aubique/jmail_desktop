package dev.aubique.bquiz.edit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public void loadQuestionList() {
        // Fill out model.questionList with (Question)objects
        model.loadQuestionListFromDatabaseAsResultSet();
        // Fill out DefaultQuestionList by retrieving (String)question
        model.getQuestionList().stream()
                .map(Question::getQuestion)
                .collect(Collectors.toList())
                .forEach(defaultQuestionList::addElement);
    }

    @Deprecated
    public void saveQuestionList() {
        // TODO: Send ModelView objects to Model->DAL
        // TODO: To delete
    }

    private Question getTextFieldsAsQuestion() {
        int indexSelected = view.getQuestionJList().getSelectedIndex();
        String questionInput = view.getQuestionTextField().getText();
        String correctAnswerInput = view.getCorrectAnswerTextField().getText();
        // TODO: Should (Question) be either BO or DTO/Beam?
        return new Question(indexSelected, questionInput, correctAnswerInput);
    }

    private List<String> getTextFieldsAsList() {
        String questionInput = view.getQuestionTextField().getText();
        String correctAnswerInput = view.getCorrectAnswerTextField().getText();
        return new ArrayList<>(Arrays.asList(questionInput, correctAnswerInput)); //TODO: Follow through the implementation
    }

    private void setTextFields(int index) throws NotSelectedException {
        if (index < 0) {
            throw new NotSelectedException("");
        }
        Question selectedQuestion;
        selectedQuestion = model.getQuestionList().get(index);
        view.getQuestionTextField().setText(selectedQuestion.getQuestion());
        view.getCorrectAnswerTextField().setText(selectedQuestion.getAnswer());
    }

    class addButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            model.addQuestion(getTextFieldsAsList());
            // No need to go down Question's properties since we use only one
            defaultQuestionList.addElement(model.toString());
        }
    }

    class listSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            int indexSelected = view.getQuestionJList().getSelectedIndex();
            System.out.println(indexSelected);
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
            // TODO: Do think of methods getQustionInput() and getIndexSelected()
            // It needs index to replace item on its initial place in the lists
            int indexSelected = view.getQuestionJList().getSelectedIndex();
            // Get the field required for DefaultListModel
            questionInput = view.getQuestionTextField().getText();
            // Replace question in Model and question String in DefaultListModel
            try { // TODO: Replace Exception with if(indexSelected>0)
                model.updateQuestion(indexSelected, getTextFieldsAsList());
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
            model.saveQuestionListToDatabase();
            System.exit(0);
        }
    }
}
