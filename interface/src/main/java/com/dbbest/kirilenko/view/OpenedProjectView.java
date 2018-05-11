package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.service.ProgramSettings;
import com.dbbest.kirilenko.viewModel.OpenedProjectViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class OpenedProjectView {

    @FXML
    public ProgressIndicator progress;

    @FXML
    public Menu actionMenu;

    @FXML
    public Button searchBtn;

    @FXML
    public MenuItem generateFullddlMenu2;

    @FXML
    public MenuItem fullLoadMenu2;

    @FXML
    public MenuItem generateFullddlMenu;

    @FXML
    public MenuItem fullLoadMenu;

    @FXML
    private TableView<Map.Entry<String, String>> attrTable;

    @FXML
    public TableColumn<Map.Entry<String, String>, String> attributeColumn;

    @FXML
    public TableColumn<Map.Entry<String, String>, String> valueColumn;

    @FXML
    public ContextMenu contextMenu;

    @FXML
    public Button saveDDLbtn;

    @FXML
    public TextField searchText;

    @FXML
    public MenuBar menuBar;

    @FXML
    public Menu projectMenu;

    @FXML
    public MenuItem saveProjectMenuItem;

    @FXML
    private TextArea ddlArea;

    @FXML
    private TreeView<TreeModel> treeView;

    private static OpenedProjectViewModel viewModel;

    private static Stage openedProjectStage;

    private static Stage mainViewStage;

    private static final Logger logger = Logger.getLogger(OpenedProjectView.class);

    @FXML
    public void initialize() {
        logger.info("initializing opened project View");
        mainViewStage.hide();
        openedProjectStage.setOnCloseRequest(event -> {
            mainViewStage.show();
        });

        generateFullddlMenu.visibleProperty().bind(viewModel.selectedRootProperty());
        generateFullddlMenu2.visibleProperty().bind(viewModel.selectedRootProperty());
        fullLoadMenu.visibleProperty().bind(viewModel.selectedRootProperty());
        fullLoadMenu2.visibleProperty().bind(viewModel.selectedRootProperty());

        projectMenu.disableProperty().bindBidirectional(viewModel.treeIsBeenLoadingProperty());
        actionMenu.disableProperty().bindBidirectional(viewModel.treeIsBeenLoadingProperty());
        searchBtn.disableProperty().bindBidirectional(viewModel.treeIsBeenLoadingProperty());

        attributeColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        attrTable.itemsProperty().bindBidirectional(viewModel.tableProperty());

        ddlArea.textProperty().bind(viewModel.ddlProperty());

        treeViewInitialize();
        logger.info("opened project View was initialized correctly");
    }

    private void treeViewInitialize() {
        viewModel.foundItemProperty().addListener((observable, oldValue, newValue) -> {
            treeView.getSelectionModel().select(newValue);
            int index = treeView.getRow(newValue);
            treeView.scrollTo(index);
        });

        treeView.rootProperty().bindBidirectional(viewModel.rootItemPropertyProperty());

        if (viewModel.selectedItemProperty().getValue() != null) {
            treeView.getSelectionModel().select(treeView.getRow(viewModel.selectedItemProperty().getValue()));
        } else {
            treeView.getSelectionModel().selectFirst();
        }

        viewModel.selectedItemProperty().bind(treeView.getSelectionModel().selectedItemProperty());

        treeView.disableProperty().bindBidirectional(viewModel.treeIsBeenLoadingProperty());

        viewModel.treeIsBeenLoadingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                treeView.refresh();
            }
        });

        progress.visibleProperty().bind(treeView.disableProperty());

        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (e.getClickCount() == 2) {
                lazyLoad(new ActionEvent());
                treeView.getSelectionModel().getSelectedItem().setExpanded(true);
            }
        });
    }

    public void show(Stage main, Connect connect, String path) throws IOException, SerializationException {
        logger.info("try to open view");

        viewModel = new OpenedProjectViewModel(path, connect);

        mainViewStage = main;
        openedProjectStage = new Stage();
        openedProjectStage.setMinWidth(500);
        openedProjectStage.setMinHeight(500);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/openedProject.fxml"));

        Scene openedProjectScene = new Scene(root);
        openedProjectStage.setTitle("DBBest");
        openedProjectStage.setScene(openedProjectScene);

        openedProjectStage.show();
        logger.info("view opened");
    }

    /////////////////////loading

    public void lazyLoad(ActionEvent actionEvent) {
        logger.info("lazy load current element");
        load(() -> viewModel.lazyLoad());
    }

    public void loadElement(ActionEvent actionEvent) {
        logger.info("load current element");
        load(() -> viewModel.loadElement());
    }

    public void fullLoad(ActionEvent actionEvent) {
        logger.info("fullLoad current element");
        load(() -> viewModel.fullLoad());
    }

    public void loadAll(ActionEvent actionEvent) {
        logger.info("loading all tree");
        load(() -> viewModel.loadAll());
    }

    public void reloadSelected(ActionEvent actionEvent) {
        logger.info("reloading current element");
        load(() -> viewModel.reload());
    }

    private void load(Runnable runnable) {
        treeView.disableProperty().set(true);
        if (viewModel.onlineModeProperty().get()) {
            runnable.run();
            logger.info("element loaded");
        } else {
            logger.info("reconnect is needed");
            ReconnectView reconnectView = new ReconnectView();
            StringBuilder password = new StringBuilder();

            reconnectView.show(openedProjectStage, viewModel.getUrl(),viewModel.getPort(), viewModel.getDbName(), viewModel.getLogin(), password);
            logger.info("reconnect view is closed");
            Thread thread = new Thread(() -> {
                try {
                    viewModel.reconnect(password.toString());
                    viewModel.onlineModeProperty().set(true);
                    runnable.run();
                    runnable.run();
                    logger.info("reconnected successfully");
                } catch (WrongCredentialsException e) {
                    Platform.runLater(() -> {
                        logger.debug("wrong credentials");
                        viewModel.onlineModeProperty().set(false);
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Wrong credentials");
                        alert.setContentText("wrong credentials");
                        alert.showAndWait();
                    });
                } finally {
                    Platform.runLater(()->{
                        treeView.disableProperty().set(false);
                    });
                }
            });
            thread.start();


        }
    }

    /////////////////////searching
    public void searchElement(ActionEvent actionEvent) {
        viewModel.searchElement(searchText.getText());
    }

    public void previousElement(ActionEvent actionEvent) {
        viewModel.previousElement();
    }

    public void nextElement(ActionEvent actionEvent) {
        viewModel.nextElement();
    }

    /////////////////////menu
    public void saveCurrentProject(ActionEvent actionEvent) {
        if (viewModel.checkFolder()) {
            try {
                viewModel.saveCurrent();
            } catch (SerializationException e) {
                logger.error(e);
            }
        } else saveProjectAs(actionEvent);
    }

    public void saveProjectAs(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("save");
        chooser.setInitialDirectory(viewModel.getProjectsFolder());
        try {
            File file = chooser.showDialog(openedProjectStage);
            if (file != null) {
                viewModel.saveProject(file);
            }
        } catch (SerializationException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("problems with saving");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    public void closeCurrentProject(ActionEvent actionEvent) {
        openedProjectStage.close();
        mainViewStage.show();
    }

    public void openProject(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("load");
        File folder = viewModel.getProjectsFolder();
        chooser.setInitialDirectory(folder);

        File file = chooser.showDialog(openedProjectStage);

        if (file != null) {
            try {
                OpenedProjectViewModel vm = new OpenedProjectViewModel(file.getAbsolutePath(), null);
                openedProjectStage.close();
                this.show(mainViewStage, null, file.getAbsolutePath());
            } catch (IOException | SerializationException e) {
                logger.error("problem with saved project opening:", e);
            }
        }
    }

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        ConnectView connectView = new ConnectView();
        connectView.openConnectWindow(mainViewStage, openedProjectStage);
    }

    public void generateFullDDl(ActionEvent actionEvent) {
        boolean loaded = viewModel.isTreeFullyLoaded();
        viewModel.generateFullDDl();
        if (loaded) {
            File saveFile = ddlFileChooser();
            if (saveFile != null) {
                viewModel.saveFullDdl(saveFile);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Tree isn't fully loaded.");
            alert.setContentText("Please choose your option.");

            ButtonType buttonTypeOne = new ButtonType("Generate anyway");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                File saveFile = ddlFileChooser();
                if (saveFile != null) {
                    viewModel.saveFullDdl(saveFile);
                }
            } else {
                alert.close();
            }
        }
    }

    private File ddlFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save DDL path");
        fileChooser.setInitialDirectory(viewModel.getProjectsFolder());
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.getExtensionFilters().add(filter1);
        return fileChooser.showSaveDialog(openedProjectStage);
    }

    // todo create normal HELP view
    public void showHelp(ActionEvent actionEvent) throws IOException {
        HelpView helpView = new HelpView();
        helpView.show(openedProjectStage);
    }

    public void showProjectOptions(ActionEvent actionEvent) throws IOException {
        ProjectSettingsView projectSettingsView = new ProjectSettingsView();
        Stage stage = projectSettingsView.show(viewModel.getConnectionTimeout());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(openedProjectStage);
        stage.showAndWait();
        try {
            int t = Integer.parseInt(projectSettingsView.getController().timeout.getText());
            if (t > 0) {
                viewModel.setConnectionTimeout(t);
                viewModel.updateConnectionCloseTimeout();
            }
        } catch (NumberFormatException e) {
            logger.info("wrong timeout value", e);
        }
    }

    public void showGeneralOptions(ActionEvent actionEvent) throws IOException {
        ProgramSettingsView view = new ProgramSettingsView();
        view.show(openedProjectStage);
    }

    public void saveDDL(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save DDL path");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.getExtensionFilters().add(filter1);
        File saveFile = fileChooser.showSaveDialog(openedProjectStage);
        if (saveFile != null) {
            viewModel.saveDDL(saveFile);
        }
    }
}
