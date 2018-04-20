package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.viewModel.NewProjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class NewProjectView {

    @FXML
    private TextField url;

    @FXML
    private TextField dbName;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    private NewProjectViewModel newProjectViewModel;

    private static Stage stage;
    private static Stage owner;

    @FXML
    private void initialize() {
        newProjectViewModel = new NewProjectViewModel();
        newProjectViewModel.urlProperty().bind(url.textProperty());
        newProjectViewModel.dbNameProperty().bind(dbName.textProperty());
        newProjectViewModel.loginProperty().bind(login.textProperty());
        newProjectViewModel.passwordProperty().bind(password.textProperty());

        stage.setOnCloseRequest(event -> {owner.show();});
    }

    public void connect(ActionEvent actionEvent) throws Exception {
        LoaderManager manager = newProjectViewModel.connect();

        if (manager != null) {
            OpenedProjectView openedProject = new OpenedProjectView();
            stage.hide();
            openedProject.show(owner,null);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Wrong credentials");
            alert.setContentText("wrong credentials");
            alert.showAndWait();
        }
    }

    public void show(ActionEvent actionEvent) throws IOException {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("create new project");
            stage.setResizable(false);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/newProject.fxml"));
            Parent root = fxmlLoader.load();

            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);

            Node source = (Node) actionEvent.getSource();
            owner = (Stage) source.getScene().getWindow();
            stage.initOwner(owner);
        }
        owner.hide();
        stage.showAndWait();
    }
}





