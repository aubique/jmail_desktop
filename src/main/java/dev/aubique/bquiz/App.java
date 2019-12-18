package dev.aubique.bquiz;

import dev.aubique.bquiz.bll.Model;
import dev.aubique.bquiz.gui.Controller;
import dev.aubique.bquiz.gui.View;

public class App {
    public static void main(String[] args) {
        Model m = Model.getInstance();
        View v = new View();
        Controller c = new Controller(m, v);
        c.loadQuestionList();
    }
}
