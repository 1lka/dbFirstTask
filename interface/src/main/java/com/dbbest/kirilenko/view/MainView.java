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
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class MainView {

    private ConnectView newProjectView;
    private OpenedProjectView openedProjectView;
    private static Stage stage;

    private final static Logger logger = Logger.getLogger(MainView.class);

    @FXML
    private void initialize() {
        logger.info("initializing main view");
        newProjectView = new ConnectView();
        openedProjectView = new OpenedProjectView();
    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        newProjectView.show(actionEvent);
    }

    public void openExistingProject(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("load");
        File initialFile = new File(ProgramSettings.getProp().getProperty("project"));
        if (!initialFile.exists()) {
            initialFile = new File(ProgramSettings.getProp().getProperty("root"));
        }
        chooser.setInitialDirectory(initialFile);
        File file = chooser.showDialog(stage);
        if (file != null) {
            try {
                openedProjectView.show(stage, null, file.getAbsolutePath());
            } catch (IOException|SerializationException e) {
                logger.error("can't open existing project", e);
            }
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
