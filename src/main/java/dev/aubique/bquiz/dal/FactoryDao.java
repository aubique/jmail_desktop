package dev.aubique.bquiz.dal;

import dev.aubique.bquiz.bll.BoQuestion;

public class FactoryDao {

    public static DefaultDao<BoQuestion> getQuestionDao() {
        return new QuestionDao();
    }
}
