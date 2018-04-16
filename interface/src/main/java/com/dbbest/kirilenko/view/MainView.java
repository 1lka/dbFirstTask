package com.dbbest.kirilenko.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class MainView {

    private NewProjectView newProjectView;
    private OpenedProjectView openedProjectView;

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
            openedProjectView.showExisting(actionEvent, file.getAbsolutePath());
        }
    }

    public void exit(ActionEvent actionEvent) {
        System.out.println("exit");
        Platform.exit();
    }
}
