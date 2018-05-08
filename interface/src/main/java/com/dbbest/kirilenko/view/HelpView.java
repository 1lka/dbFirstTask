package com.dbbest.kirilenko.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpView {

    public void show(Stage owner) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("HELP");
        stage.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/help.fxml"));
        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);

        stage.initOwner(owner);
        stage.showAndWait();
    }
}
