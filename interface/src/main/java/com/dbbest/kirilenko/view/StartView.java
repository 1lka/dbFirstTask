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

public class StartView {

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnClose;

    private Stage mainStage;
    private Stage newProjectStage;
    private Parent parent;
    private NewProjectView newProjectView;

    @FXML
    private void initialize() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/newProject.fxml"));
//        parent = fxmlLoader.load();
//        newProjectView = fxmlLoader.getController();
        newProjectView = new NewProjectView();

    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        newProjectView.show(actionEvent);
    }

    public void exit(ActionEvent actionEvent) {
        System.out.println("exit");
        Platform.exit();
    }

    public void openExistingProject(ActionEvent actionEvent) {
        System.out.println("todo existing project");
    }
}
