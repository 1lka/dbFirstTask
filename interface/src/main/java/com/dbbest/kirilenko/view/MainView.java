package com.dbbest.kirilenko.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainView extends Application {


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/main.fxml"));

        Scene scene = new Scene(root, 220, 120);
        primaryStage.setResizable(false);

        primaryStage.setTitle("Welcome page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        Stage createProjectStage = new Stage();
        createProjectStage.setTitle("new project");
        createProjectStage.setMinHeight(150);
        createProjectStage.setMinWidth(250);
        createProjectStage.setResizable(false);

        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/newProject.fxml"));

        createProjectStage.setScene(new Scene(parent));
        createProjectStage.initModality(Modality.WINDOW_MODAL);

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        createProjectStage.initOwner(stage);


        createProjectStage.showAndWait(); // для ожидания закрытия окна
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
