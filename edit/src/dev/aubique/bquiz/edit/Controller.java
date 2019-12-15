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
//        model.loadQuestionListFromDatabaseAsResultSet();
        model.loadQuestions();
        // Fill out DefaultQuestionList by retrieving (String)question
        model.getQuestionList().stream()
                .map(Question::getQuestion)
                .collect(Collectors.toList())
                .forEach(defaultQuestionList::addElement);
    }

    private List<String> getTextFieldsAsList() {
        String questionInput = view.getQuestionTextField().getText();
        String correctAnswerInput = view.getCorrectAnswerTextField().getText();
        return new ArrayList<>(Arrays.asList(questionInput, correctAnswerInput));
    }

    private void setTextFields(int index) throws NotSelectedException {
        if (index < 0) {
            throw new NotSelectedException("");
        }
        //TODO: Do think of refactoring with a iteration over List<TextField> with List<String>
        Question selectedQuestion;
        selectedQuestion = model.getQuestionList().get(index);
        view.getQuestionTextField().setText(selectedQuestion.getQuestion());
        view.getCorrectAnswerTextField().setText(selectedQuestion.getAnswer());
    }

    static class exitButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
        }
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
//            System.out.println("JList index selected: " + indexSelected);
            try {
                setTextFields(indexSelected);
            } catch (NotSelectedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //TODO fix bug: Doesn't edit question after adding a new one
    class editButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // TODO: Do think of improving re-usability with getQuestionInput() and getIndexSelected()
            // It needs index to replace item on its initial place in the lists
            int indexSelected = view.getQuestionJList().getSelectedIndex();
            if (indexSelected < 0) return;
            // Get the field required for DefaultListModel
            String questionInput = view.getQuestionTextField().getText();
            try {
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
}
