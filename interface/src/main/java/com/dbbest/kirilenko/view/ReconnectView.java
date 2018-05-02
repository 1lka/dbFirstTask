package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ReconnectView {

    @FXML
    public TextField pass;
    @FXML
    private TextField u;
    @FXML
    private TextField n;
    @FXML
    private TextField l;

    private static String url;
    private static String login;
    private static String dbName;
    private static StringBuilder password;
    private static Stage stage;

    @FXML
    private void initialize() {
        u.textProperty().set(url);
        l.textProperty().set(login);
        n.textProperty().set(dbName);
    }

    public void show(Stage openedProjectStage, String url, String dbName, String login, StringBuilder password) throws IOException {
        ReconnectView.password = password;
        ReconnectView.url = url;
        ReconnectView.login = login;
        ReconnectView.dbName = dbName;

        stage = new Stage();
        stage.setTitle("password required");
        stage.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/reconnect.fxml"));

        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);

        stage.initOwner(openedProjectStage);

        stage.showAndWait();
    }

    public void reconnect(ActionEvent actionEvent) {
        ReconnectView.password.append(pass.textProperty().get());
        stage.close();
    }
}