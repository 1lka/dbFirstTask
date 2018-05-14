package com.dbbest.kirilenko.view;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjectSettingsView {

    @FXML
    public TextField timeout;

    @FXML
    public Button okBtn;

    private ProjectSettingsView controller;

    public ProjectSettingsView getController() {
        return controller;
    }

    @FXML
    private void initialize() {
        timeout.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> styleClass = timeout.getStyleClass();
            try {
                Integer.parseInt(timeout.textProperty().get().trim());
                timeout.getStyleClass().remove("error");
            } catch (Exception e) {
                if (!styleClass.contains("error")) {
                    styleClass.add("error");
                }
            }
        });
    }

    public Stage show(long currentTimeout) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/projectSettings.fxml"));

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Project settings");


        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/error.css").toString());

        stage.setScene(scene);


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
