package dev.aubique.bquiz.viewcontroller;

import dev.aubique.bquiz.NotSelectedException;
import dev.aubique.bquiz.model.BoQuestion;
import dev.aubique.bquiz.model.Model;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
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

    /**
     * Load questions from Persistence
     * Fill out DefaultListModel by retrieving the question property from (Question) obj
     */
    public void loadQuestionList() {
        // Fill out model.questionList with (Question)objects
        model.loadQuestions();
        // Fill out DefaultQuestionList by retrieving (String)question
        model.getBoQuestionList().stream()
                .map(BoQuestion::getQuestion)
                .collect(Collectors.toList())
                .forEach(defaultQuestionList::addElement);
    }

    /**
     * Retrieve JTextField text data from View avoiding boilerplate code
     * Collect strings while iterate over List<JTextField> from View
     *
     * @return List that may be used as properties for Question
     */
    private List<String> getTextFieldsAsList() {
        return view.textFieldList.stream()
                .map(JTextComponent::getText)
                .collect(Collectors.toList());
    }

    /**
     * Set JTextFields dynamically
     * Do it with a help of List<JTextList> that contains text fields declared in View
     * Pair it with List<String> from Question.getProperties()
     *
     * @param selectedIndex Index for JList
     * @throws NotSelectedException
     */
    private void setTextFields(int selectedIndex) throws NotSelectedException {
        if (selectedIndex < 0) {
            throw new NotSelectedException("");
        }
        List<String> questionProperties = model.getBoQuestionList().get(selectedIndex).getProperties();

        for (int i = 0; i < view.textFieldList.size(); i++) {
            view.textFieldList.get(i).setText(questionProperties.get(i));
        }
    }

    /**
     * Handle event for exitButton
     */
    static class exitButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
        }
    }

    /**
     * Handle event for addButton
     * Add a new item in DefaultListModel and pass actions down to Model
     */
    class addButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            model.addQuestion(getTextFieldsAsList());
            // No need to go down Question's properties since we use only one - question title
            defaultQuestionList.addElement(model.toString());
        }
    }

    /**
     * Handle event once JList item is selected
     * Call setTextField for updating TextField with new data
     */
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

    /**
     * Handle event for editButton
     * Retrieve data from JFrame TextFields and send it to Model
     */
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

    /**
     * Handle event for deleteButton
     * Remove item by index from DefaultListModel
     * Pass actions to delete question from Persistence
     */
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
