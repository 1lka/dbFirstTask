package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.service.ProgramSettings;
import com.dbbest.kirilenko.viewModel.OpenedProjectViewModel;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    @FXML
    public void initialize() {

        mainViewStage.hide();
        openedProjectStage.setOnCloseRequest(event -> {
            mainViewStage.show();
        });


        projectMenu.disableProperty().bindBidirectional(viewModel.treeIsBeenLoadingProperty());
        actionMenu.disableProperty().bindBidirectional(viewModel.treeIsBeenLoadingProperty());
        searchBtn.disableProperty().bindBidirectional(viewModel.treeIsBeenLoadingProperty());

        attributeColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        attrTable.itemsProperty().bindBidirectional(viewModel.tableProperty());

        ddlArea.textProperty().bind(viewModel.ddlProperty());

        viewModel.foundItemProperty().addListener((observable, oldValue, newValue) -> {
            treeView.getSelectionModel().select(newValue);
            int index = treeView.getRow(newValue);
            treeView.scrollTo(index);
        });

        treeViewInitialize();
    }

    private void treeViewInitialize() {
        treeView.rootProperty().bindBidirectional(viewModel.rootItemPropertyProperty());

        if (viewModel.selectedItemProperty().getValue() != null) {
            treeView.getSelectionModel().select(treeView.getRow(viewModel.selectedItemProperty().getValue()));
        } else {
            treeView.getSelectionModel().selectFirst();
        }

        viewModel.selectedItemProperty().bind(treeView.getSelectionModel().selectedItemProperty());

        treeView.disableProperty().bind(viewModel.treeIsBeenLoadingProperty());
        progress.visibleProperty().bind(treeView.disableProperty());

        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
            if (e.getClickCount() == 2) {
                viewModel.loadElement();
                viewModel.lazyLoad();
                treeView.getSelectionModel().getSelectedItem().setExpanded(true);
            }
        });
    }



    public void show(Stage main, Connect connect, String path) throws IOException, SerializationException {
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
    }

    /////////////////////loading

    public void lazyLoad(ActionEvent actionEvent) {
        if (viewModel.onlineModeProperty().get()) {
            viewModel.lazyLoad();
        } else {
            ReconnectView reconnectView = new ReconnectView();
            StringBuilder password = new StringBuilder();
            try {
                reconnectView.show(openedProjectStage, viewModel.getUrl(), viewModel.getDbName(), viewModel.getLogin(), password);
                viewModel.reconnect(password.toString());
                viewModel.onlineModeProperty().set(true);
                viewModel.lazyLoad();
            } catch (IOException | WrongCredentialsException e) {
                viewModel.onlineModeProperty().set(false);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Wrong credentials");
                alert.setContentText("wrong credentials");
                alert.showAndWait();
            }
        }
    }

    public void loadElement(ActionEvent actionEvent) {
        if (viewModel.onlineModeProperty().get()) {
            viewModel.loadElement();
        } else {
            ReconnectView reconnectView = new ReconnectView();
            StringBuilder password = new StringBuilder();
            try {
                reconnectView.show(openedProjectStage, viewModel.getUrl(), viewModel.getDbName(), viewModel.getLogin(), password);
                viewModel.reconnect(password.toString());
                viewModel.onlineModeProperty().set(true);
                viewModel.loadElement();
            } catch (IOException | WrongCredentialsException e) {
                viewModel.onlineModeProperty().set(false);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Wrong credentials");
                alert.setContentText("wrong credentials");
                alert.showAndWait();
            }
        }
    }

    public void fullLoad(ActionEvent actionEvent) {
        if (viewModel.onlineModeProperty().get()) {
            viewModel.fullLoad();
        } else {
            ReconnectView reconnectView = new ReconnectView();
            StringBuilder password = new StringBuilder();
            try {
                reconnectView.show(openedProjectStage, viewModel.getUrl(), viewModel.getDbName(), viewModel.getLogin(), password);
                viewModel.reconnect(password.toString());
                viewModel.onlineModeProperty().set(true);
                viewModel.fullLoad();
            } catch (IOException | WrongCredentialsException e) {
                viewModel.onlineModeProperty().set(false);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Wrong credentials");
                alert.setContentText("wrong credentials");
                alert.showAndWait();
            }
        }
    }

    public void loadAll(ActionEvent actionEvent) {
        if (viewModel.onlineModeProperty().get()) {
            viewModel.loadAll();
        } else {
            ReconnectView reconnectView = new ReconnectView();
            StringBuilder password = new StringBuilder();
            try {
                reconnectView.show(openedProjectStage, viewModel.getUrl(), viewModel.getDbName(), viewModel.getLogin(), password);
                viewModel.reconnect(password.toString());
                viewModel.onlineModeProperty().set(true);
                viewModel.loadAll();
            } catch (IOException | WrongCredentialsException e) {
                viewModel.onlineModeProperty().set(false);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Wrong credentials");
                alert.setContentText("wrong credentials");
                alert.showAndWait();
            }
        }
    }

    public void reloadSelected(ActionEvent actionEvent) {
        if (viewModel.onlineModeProperty().get()) {
            viewModel.reload();
        } else {
            ReconnectView reconnectView = new ReconnectView();
            StringBuilder password = new StringBuilder();
            try {
                reconnectView.show(openedProjectStage, viewModel.getUrl(), viewModel.getDbName(), viewModel.getLogin(), password);
                viewModel.reconnect(password.toString());
                viewModel.onlineModeProperty().set(true);
                viewModel.reload();
            } catch (IOException | WrongCredentialsException e) {
                viewModel.onlineModeProperty().set(false);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Wrong credentials");
                alert.setContentText("wrong credentials");
                alert.showAndWait();
            }
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

    public void saveCurrentProject(ActionEvent actionEvent) throws IOException, SerializationException {
        if (viewModel.checkFolder()) {
            viewModel.saveCurrent();
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

    public void openProject(ActionEvent actionEvent) throws IOException, SerializationException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("load");
        File folder = new File(ProgramSettings.getProp().getProperty("project"));
        chooser.setInitialDirectory(folder);
        File file = chooser.showDialog(openedProjectStage);

        if (file != null) {
            openedProjectStage.close();
            this.show(mainViewStage, null, file.getAbsolutePath());
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
        fileChooser.setInitialDirectory(new File(ProgramSettings.getProp().getProperty("project")));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.getExtensionFilters().add(filter1);
        return fileChooser.showSaveDialog(openedProjectStage);
    }

    // todo create normal HELP view
    public void showHelp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.setContentText("there will be a text about the program");

        alert.showAndWait();
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
