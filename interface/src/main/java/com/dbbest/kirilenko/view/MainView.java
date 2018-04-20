package com.dbbest.kirilenko.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class MainView {

    private NewProjectView newProjectView;
    private OpenedProjectView openedProjectView;
    private static Stage stage;

    @FXML
    private void initialize() {
        newProjectView = new NewProjectView();
        openedProjectView = new OpenedProjectView();
    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        newProjectView.show(actionEvent);
    }

    public void openExistingProject(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Window theStage = source.getScene().getWindow();

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("load");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = chooser.showDialog(theStage);

        if (file != null) {
            openedProjectView.show(stage, file.getAbsolutePath());
        }
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void show(Stage primaryStage) throws IOException {
        stage = primaryStage;
        System.out.println("mainView " + stage);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/main.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setResizable(false);

        primaryStage.setTitle("Welcome page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
