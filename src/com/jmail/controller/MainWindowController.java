package com.jmail.controller;

import com.jmail.EmailManager;
import com.jmail.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

    @FXML
    private TreeView<String> emailsTreeView;

    @FXML
    private TableView<?> emailsTableView;

    @FXML
    private WebView emailWebView;

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void optionsAction() {
        System.out.println("optionsAction");
        viewFactory.showOptionsWindow();
    }

    @FXML
    void closeAction() {
        //TODO refactor the spaghetti
        final Stage stage = (Stage) emailsTreeView.getScene().getWindow();
        viewFactory.closeStage(stage);
    }

    @FXML
    void addAccountAction(ActionEvent event) {
        viewFactory.showLoginWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpEmailsTreeView();
    }

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFolderRoot());
        emailsTreeView.setShowRoot(false);
    }
}
