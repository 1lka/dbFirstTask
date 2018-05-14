package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.model.ConnectModel;
import com.dbbest.kirilenko.viewModel.ConnectionViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
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
import java.util.Collections;

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
    public TextField port;

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
        connectionViewModel.portProperty().bindBidirectional(port.textProperty());

        btnConnect.disableProperty().bind(Bindings.createBooleanBinding(this::disableBtn,
                port.textProperty(), url.textProperty(),
                login.textProperty(), dbName.textProperty(),
                password.textProperty()));


        recentlyUsed.setItems(connectionViewModel.getRecentlyUsed());
        connectionViewModel.selectedConnectModelProperty().bind(recentlyUsed.getSelectionModel().selectedItemProperty());

        progressBar.setVisible(false);
        choiceBox.setItems(connectionViewModel.getChoicesList());
        choiceBox.getSelectionModel().select(0);

        port.textProperty().addListener((observable, oldValue, newValue) -> {

            ObservableList<String> styleClass = port.getStyleClass();
            try {
                Integer.parseInt(port.textProperty().get().trim());
                port.getStyleClass().remove("error");
            } catch (Exception e) {
                if (!styleClass.contains("error")) {
                    styleClass.add("error");
                }
            }
        });


        logger.info("connection window initialized");
    }

    private boolean disableBtn() {
        return checkPort() || (url == null || url.textProperty().get().isEmpty())
                || (port == null || port.textProperty().get().isEmpty())
                || (dbName == null || dbName.textProperty().get().isEmpty())
                || (password == null || password.textProperty().get().isEmpty())
                || (login == null || login.textProperty().get().isEmpty());
    }

    private boolean checkPort() {
        try {
            Integer.parseInt(port.textProperty().get());
            return false;
        } catch (Exception e) {
            return true;
        }
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
                    }
                });
            } catch (WrongCredentialsException e) {
                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Wrong credentials");
                    alert.setContentText("wrong credentials");
                    alert.showAndWait();
                });
            }
        });
        progressBar.setVisible(true);

        logger.info("starting connection thread");
        connectionThread.start();
    }

    public void show(Stage owner) throws IOException {
        ConnectView.owner = owner;
        stage = new Stage();
        stage.setOnCloseRequest(event -> {
            owner.show();
        });
        stage.setTitle("create new project");
        stage.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/newProject.fxml"));
        Parent root = fxmlLoader.load();


            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("css/error.css").toString());

        stage.setScene(scene);

        owner.hide();
        stage.show();
    }

    public void openConnectWindow(Stage mainViewStage, Stage openedProjectStage) throws IOException {
        ConnectView.openedProjectStage = openedProjectStage;
        owner = mainViewStage;
        stage = new Stage();
        stage.setTitle("Create new project");
        stage.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/newProject.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/error.css").toString());


        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.initOwner(openedProjectStage);
        stage.showAndWait();
    }
}





