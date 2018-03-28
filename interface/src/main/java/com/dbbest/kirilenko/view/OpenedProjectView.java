package com.dbbest.kirilenko.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OpenedProjectView {

    private Stage primaryStage;

    /**
     * Method shows connection window
     * @param event
     * @throws Exception
     */
    public void show(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/openedProject.fxml"));
        Scene scene = new Scene(root, 1000,1000);
        primaryStage = new Stage();
        primaryStage.setTitle("Ilka");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Node source = (Node)event.getSource();
        primaryStage.initOwner(source.getScene().getWindow());
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.show();
    }

}
