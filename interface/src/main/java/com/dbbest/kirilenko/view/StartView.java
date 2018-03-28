package com.dbbest.kirilenko.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StartView {

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnOpen;

    @FXML
    private Button tbnClose;

    private Stage mainStage;
    private Stage newProjectStage;
    private Parent parent;
    private NewProjectView newProjectView;

    @FXML
    private void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/newProject.fxml"));
        parent = fxmlLoader.load();
        newProjectView = fxmlLoader.getController();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void createNewProject(ActionEvent actionEvent) {
        if (newProjectStage == null) {
            System.out.println("creating new Stage");
            newProjectStage = new Stage();
            newProjectStage.setTitle("Редактирование записи");
            newProjectStage.setMinHeight(150);
            newProjectStage.setMinWidth(300);
            newProjectStage.setResizable(false);
            newProjectStage.setScene(new Scene(parent));
            newProjectStage.initModality(Modality.WINDOW_MODAL);
            newProjectStage.initOwner(mainStage);
        }
        newProjectStage.showAndWait(); // для ожидания закрытия окна
    }

    public void exit(ActionEvent actionEvent) {
        System.out.println("exit");
        Platform.exit();
    }

    public void openExistingProject(ActionEvent actionEvent) {
        System.out.println("todo existing project");
    }
}
