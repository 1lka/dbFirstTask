package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.service.ProgramSettings;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainView {

    private ConnectView newProjectView;
    private OpenedProjectView openedProjectView;
    private static Stage stage;

    @FXML
    private void initialize() {
        newProjectView = new ConnectView();
        openedProjectView = new OpenedProjectView();
    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        newProjectView.show(actionEvent);
    }

    public void openExistingProject(ActionEvent actionEvent) throws IOException, SerializationException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("load");
        File folder = new File(ProgramSettings.getProp().getProperty("project"));
        chooser.setInitialDirectory(folder);
        File file = chooser.showDialog(stage);

        if (file != null) {
            openedProjectView.show(stage, null, file.getAbsolutePath());
        }
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void show(Stage primaryStage) throws IOException {
        stage = primaryStage;
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
