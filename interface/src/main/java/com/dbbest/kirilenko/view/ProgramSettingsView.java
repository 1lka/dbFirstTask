package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.service.ProgramSettings;
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
        File file = viewModel.getDefaultFolder();
        chooser.setInitialDirectory(file);

        File file2 = chooser.showDialog(settingsStage);
        if (file2 != null) {
            projPath.textProperty().set(file2.getAbsolutePath());
        }
    }

    public void chooseLogsFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Logs folder...");
        fileChooser.setInitialDirectory(viewModel.obtainLogFileName());
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("Log files (*.log)", "*.log");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.getExtensionFilters().add(filter1);
        File file = fileChooser.showSaveDialog(settingsStage);
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
        settingsStage.initModality(Modality.WINDOW_MODAL);
        settingsStage.showAndWait();
    }

    public void resetToDefault(ActionEvent actionEvent) {

    }
}
