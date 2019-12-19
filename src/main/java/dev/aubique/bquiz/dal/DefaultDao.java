package dev.aubique.bquiz.dal;

import java.util.List;

public interface DefaultDao<T> {

    int getLastIndex();

    void incLastIndex();

    void createTable();

    List<T> selectAll();

    void insert(T boQuestionToAdd);

    void update(T boQuestionToReplace);

    void delete(int questionId);
}
