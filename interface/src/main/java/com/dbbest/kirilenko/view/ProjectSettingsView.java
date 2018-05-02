package com.dbbest.kirilenko.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjectSettingsView {

    @FXML
    public TextField timeout;

    private ProjectSettingsView controller;

    public ProjectSettingsView getController() {
        return controller;
    }

    public Stage show(long currentTimeout) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/projectSettings.fxml"));

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Project settings");
        stage.setScene(new Scene(loader.load()));


        controller = loader.getController();
        controller.timeout.textProperty().set(String.valueOf(currentTimeout));

        stage.setOnCloseRequest(event -> controller.timeout.textProperty().set("closed"));
        return stage;
    }

    public void cancel(ActionEvent actionEvent) {
        timeout.textProperty().set("canceled");
        ((Stage)timeout.getScene().getWindow()).close();
    }

    public void confirm(ActionEvent actionEvent) {
        ((Stage)timeout.getScene().getWindow()).close();
    }
}
