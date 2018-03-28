package com.dbbest.kirilenko;

import com.dbbest.kirilenko.view.StartView;
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

public class StartWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/main.fxml"));
        Parent root = fxmlLoader.load();
        StartView startView = fxmlLoader.getController();
        startView.setMainStage(primaryStage);

        Scene scene = new Scene(root, 220, 120);
        primaryStage.setResizable(false);

        primaryStage.setTitle("Welcome page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
