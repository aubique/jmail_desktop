package dev.aubique.bquiz.bll;

import dev.aubique.bquiz.dal.DefaultDao;
import dev.aubique.bquiz.dal.FactoryDao;
import dev.aubique.bquiz.gui.NotSelectedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Model component
 * Has yet to be covered by Service Layout to encapsulate Business Object
 */
public class Model {

    private static Model instance = null;
    private List<BoQuestion> boQuestionList = new ArrayList<>();
    private BoQuestion boQuestionObj;
    private DefaultDao<BoQuestion> db;

    private Model() {
        this.db = FactoryDao.getQuestionDao();
        db.createTable();
    }

    /**
     * Singleton factory
     *
     * @return Instance of this class
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }

        return instance;
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
        db.insert(boQuestionObj);
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
        db.update(boQuestionObj);
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
        db.delete(databaseId);
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
