package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.viewModel.ConnectionViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectView {

    @FXML
    public ChoiceBox<DBType> choiceBox;

    @FXML
    private TextField url;

    @FXML
    private TextField dbName;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    private static ConnectionViewModel connectionViewModel;

    private static Stage stage;
    private static Stage openedProjectStage;
    private static Stage owner;

    @FXML
    private void initialize() {
        connectionViewModel = new ConnectionViewModel();
        connectionViewModel.urlProperty().bindBidirectional(url.textProperty());
        connectionViewModel.dbNameProperty().bindBidirectional(dbName.textProperty());
        connectionViewModel.loginProperty().bindBidirectional(login.textProperty());
        connectionViewModel.passwordProperty().bindBidirectional(password.textProperty());
        choiceBox.setItems(connectionViewModel.getChoicesList());
        choiceBox.getSelectionModel().select(0);
    }

    public void connect(ActionEvent actionEvent) throws IOException {
        try {
            Connect connect = connectionViewModel.connect(choiceBox.getSelectionModel().getSelectedItem());
            OpenedProjectView openedProject = new OpenedProjectView();
            openedProject.show(owner, connect, null);
            stage.hide();
            if (openedProjectStage != null) {
                openedProjectStage.close();
            }
        } catch (WrongCredentialsException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Wrong credentials");
            alert.setContentText("wrong credentials");
            alert.showAndWait();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public void show(ActionEvent actionEvent) throws IOException {
        if (stage == null) {
            stage = new Stage();
            stage.setOnCloseRequest(event -> {
                owner.show();
            });
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

    public void openConnectWindow(Stage mainViewStage, Stage openedProjectStage) throws IOException {
        ConnectView.openedProjectStage = openedProjectStage;
        owner = mainViewStage;
        stage = new Stage();
        stage.setTitle("create new project");
        stage.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/newProject.fxml"));
        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);

        stage.initOwner(openedProjectStage);
        stage.showAndWait();
    }
}





