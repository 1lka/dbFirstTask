package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.viewModel.ProgramSettingsViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class ProgramSettingsView {

    private static final Logger logger = Logger.getLogger(ProgramSettingsView.class);

    @FXML
    public TextField projPath;

    @FXML
    public TextField logPath;

    private ProgramSettingsViewModel viewModel;

//    private static Stage owner;
    private static Stage settingsStage;

    @FXML
    public void initialize() {
        viewModel = new ProgramSettingsViewModel();

        projPath.textProperty().bindBidirectional(viewModel.projectProperty());
        logPath.textProperty().bindBidirectional(viewModel.logProperty());
    }

    public void chooseProjectFolder(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Default project path");
        chooser.setInitialDirectory(new File(projPath.getText()));

        File file = chooser.showDialog(settingsStage);
        if (file != null) {
            projPath.textProperty().set(file.getAbsolutePath());
        }
    }

    public void chooseLogsFile(ActionEvent actionEvent) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Logs folder...");
        fileChooser.setInitialDirectory(new File(logPath.getText()));
        File file = fileChooser.showDialog(settingsStage);
        if (file != null) {
            logPath.textProperty().set(file.getAbsolutePath());
        }
    }

    public void saveChanges(ActionEvent actionEvent) throws IOException {
        viewModel.saveChanges();
        settingsStage.close();
    }

    public void cancelChanges(ActionEvent actionEvent) {
        settingsStage.hide();
    }

    public void show(Stage openedProjectStage) throws IOException {
        logger.debug("opening project settings");
        settingsStage = new Stage();
        settingsStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ProgramSettings.fxml"));

        Scene settingsScene = new Scene(root);
        settingsStage.setTitle("Settings");
        settingsStage.setScene(settingsScene);

        settingsStage.initOwner(openedProjectStage);
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.showAndWait();
    }
}
