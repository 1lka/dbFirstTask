package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ReconnectView {

    @FXML
    public TextField pass;
    @FXML
    public ProgressBar progress;
    @FXML
    public Button reconnectBtn;
    @FXML
    private TextField u;
    @FXML
    private TextField p;
    @FXML
    private TextField n;
    @FXML
    private TextField l;

    private static String url;
    private static String port;
    private static String login;
    private static String dbName;
    private static StringBuilder password;
    private static Stage stage;

    @FXML
    private void initialize() {
        u.textProperty().set(url);
        p.textProperty().set(port);
        l.textProperty().set(login);
        n.textProperty().set(dbName);
    }

    public void show(Stage openedProjectStage, String url,String port, String dbName, String login, StringBuilder password) {
        ReconnectView.password = password;
        ReconnectView.url = url;
        ReconnectView.port = port;
        ReconnectView.login = login;
        ReconnectView.dbName = dbName;

        stage = new Stage();
        stage.setTitle("password required");
        stage.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/reconnect.fxml"));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
