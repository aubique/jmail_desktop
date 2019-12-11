package dev.aubique.bquiz.edit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;

public class Controller {

    private Model model;
    private View view;
    private DefaultListModel<String> jListModel = new DefaultListModel<>();
    private String questionInput;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        view.getQuestionJList().setModel(jListModel);
        view.getAddButton().addActionListener(new addButtonHandler());
        view.getConfirmButton().addActionListener(new confirmButtonHandler());
        view.getQuestionJList().addListSelectionListener(new listSelectionHandler());
    }

    private String extractQuestionString() {
        // Index is required to refer item in model list of (ArrayList)<Question>
        int indexSelected = view.getQuestionJList().getSelectedIndex();
        // Go through Model->ArrayList->Question->String
        // TODO: Refine code here for better encapsulation
        return model.getQuestionList().get(indexSelected).getQuestion();
    }

    class addButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            questionInput = view.getQuestionTextField().getText();
            model.addQuestion(questionInput);//TODO - add more fields
            // No need to go down Question's properties since we use only one
            jListModel.addElement(model.toString());
        }
    }

    class confirmButtonHandler extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // TODO: Shall we make Model abstract to work along with Question's properties?
            // It needs index to replace item on its initial place in the lists
            int indexSelected = view.getQuestionJList().getSelectedIndex();
            // Get input from View text field
            // TODO: Make a method to get values from every field
            questionInput = view.getQuestionTextField().getText();
            // Replace items in DefaultListModel and Question/DB
            model.replaceQuestion(indexSelected, questionInput);
            jListModel.setElementAt(questionInput, indexSelected);
        }
    }

    class listSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            view.getQuestionTextField().setText(extractQuestionString());
        }
    }
}
