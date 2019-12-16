package dev.aubique.bquiz;

import dev.aubique.bquiz.model.Model;
import dev.aubique.bquiz.viewcontroller.Controller;
import dev.aubique.bquiz.viewcontroller.View;

public class App {
    public static void main(String[] args) {
        Model m = new Model();
        View v = new View();
        Controller c = new Controller(m, v);
        c.loadQuestionList();
    }
}
