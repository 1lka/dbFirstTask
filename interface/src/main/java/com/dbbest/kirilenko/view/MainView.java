package com.dbbest.kirilenko.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainView {

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnClose;

    private NewProjectView newProjectView;

    @FXML
    private void initialize()  {
        newProjectView = new NewProjectView();
    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        newProjectView.show(actionEvent);
    }

    public void openExistingProject(ActionEvent actionEvent) {
        System.out.println("todo existing project");
    }

    public void exit(ActionEvent actionEvent) {
        System.out.println("exit");
        Platform.exit();
    }
}
