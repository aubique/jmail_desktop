package dev.aubique.bquiz.model;

import dev.aubique.bquiz.NotSelectedException;
import dev.aubique.bquiz.dal.DaoQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Model component
 * Has yet to be covered by Service Layout to incapsulate Business Object
 */
public class Model {

    private List<BoQuestion> boQuestionList = new ArrayList<>();
    private BoQuestion boQuestionObj;
    private DaoQuestion db;

    public Model() {
        this.db = new DaoQuestion();
        db.createTable();
    }

    /**
     * Load questions from DB and get them as List<Question>
     */
    public void loadQuestions() {
        boQuestionList = db.selectAll();
    }

    @Deprecated
    public void addQuestionToList(BoQuestion boQuestionToAdd) {
        this.boQuestionObj = boQuestionToAdd;
        boQuestionList.add(boQuestionObj);
    }

    /**
     * Create new Question with the given properties
     * Add it to questionList stored by Model, then to database
     * Grub ID for the new Question from Database property
     *
     * @param properties - Question properties except ID
     */
    public void addQuestion(List<String> properties) {
        db.incLastIndex();
        int lastIdIndex = db.getLastIndex();
        this.boQuestionObj = new BoQuestion(lastIdIndex, properties);

        boQuestionList.add(boQuestionObj);
        db.insertQuestion(boQuestionObj);
    }

    /**
     * Update question if it is selected by User
     * Get Question from DefaultListModel with JList-index
     * Then get a real DB index of Question from its ID-property
     *
     * @param jListId    Index for DefaultListModel
     * @param properties Retrieved data from JFrame
     * @throws NotSelectedException Pass an exception up the next level if User didn't select
     */
    public void updateQuestion(int jListId, List<String> properties) throws NotSelectedException {
        if (jListId < 0) {
            throw new NotSelectedException(" to edit");
        }
        int databaseId = boQuestionList.get(jListId).getId();
        this.boQuestionObj = new BoQuestion(databaseId, properties);
        boQuestionList.set(jListId, boQuestionObj);
        db.updateQuestion(boQuestionObj);
    }

    /**
     * Delete a question from Database/DefaultListModel
     *
     * @param jListId Index for DefaultListModel
     * @throws NotSelectedException Pass an exception up the next level if User didn't select
     */
    public void removeQuestionAt(int jListId) throws NotSelectedException {
        if (jListId < 0) {
            throw new NotSelectedException(" to remove");
        }
        int databaseId = boQuestionList.get(jListId).getId();
        boQuestionList.remove(jListId);
        db.deleteQuestion(databaseId);
    }

    public List<BoQuestion> getBoQuestionList() {
        return boQuestionList;
    }

    @Override
    public String toString() {
        if (boQuestionObj == null) {
            return "None";
        }
        return boQuestionObj.getQuestion();
    }
}
