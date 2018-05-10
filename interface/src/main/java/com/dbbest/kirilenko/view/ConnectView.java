package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.model.ConnectModel;
import com.dbbest.kirilenko.viewModel.ConnectionViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ConnectView {

    private final static Logger logger = Logger.getLogger(ConnectView.class);

    @FXML
    public ChoiceBox<DBType> choiceBox;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public ChoiceBox<ConnectModel> recentlyUsed;

    @FXML
    public Button btnConnect;

    @FXML
    private TextField url;

    @FXML
    private TextField dbName;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    private ConnectionViewModel connectionViewModel;

    private static Stage stage;
    private static Stage openedProjectStage;
    private static Stage owner;

    @FXML
    private void initialize() {
        logger.info("initializing connection window");
        connectionViewModel = new ConnectionViewModel();
        connectionViewModel.urlProperty().bindBidirectional(url.textProperty());
        connectionViewModel.dbNameProperty().bindBidirectional(dbName.textProperty());
        connectionViewModel.loginProperty().bindBidirectional(login.textProperty());
        connectionViewModel.passwordProperty().bindBidirectional(password.textProperty());

        recentlyUsed.setItems(connectionViewModel.getRecentlyUsed());
        connectionViewModel.selectedConnectModelProperty().bind(recentlyUsed.getSelectionModel().selectedItemProperty());

        progressBar.setVisible(false);
        choiceBox.setItems(connectionViewModel.getChoicesList());
        choiceBox.getSelectionModel().select(0);

        logger.info("connection window initialized");
    }

    public void connect(ActionEvent actionEvent) {
        logger.info("trying to connect ...");
        Thread connectionThread = new Thread(() -> {
            try {
                Connect connect = connectionViewModel.connect(choiceBox.getSelectionModel().getSelectedItem());
                logger.info("connected");
                OpenedProjectView openedProject = new OpenedProjectView();
                Platform.runLater(() -> {
                    try {
                        logger.info("opening project window");
                        openedProject.show(owner, connect, null);
                        stage.hide();
                        if (openedProjectStage != null) {
                            openedProjectStage.close();
                        }
                    } catch (IOException | SerializationException e) {
                        logger.info("problems with opening project window", e);
                    } finally {
                        progressBar.setVisible(false);
                        btnConnect.setDisable(false);
                    }
                });
            } catch (WrongCredentialsException e) {
                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    btnConnect.setDisable(false);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Wrong credentials");
                    alert.setContentText("wrong credentials");
                    alert.showAndWait();
                });
            }
        });
        progressBar.setVisible(true);
        btnConnect.setDisable(true);

        logger.info("starting connection thread");
        connectionThread.start();
    }

    public void show(ActionEvent actionEvent) throws IOException {
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





