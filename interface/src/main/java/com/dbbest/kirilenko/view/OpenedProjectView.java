package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.exception.DdlGenerationException;
import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.viewModel.OpenedProjectViewModel;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class OpenedProjectView {

    @FXML
    public ProgressIndicator progress;

    @FXML
    private TableView<Map.Entry<String, String>> attrTable;

    @FXML
    public TableColumn<Map.Entry<String, String>, String> attributeColumn;

    @FXML
    public TableColumn<Map.Entry<String, String>, String> valueColumn;

    @FXML
    public ContextMenu contextMenu;

    @FXML
    public MenuItem FL;

    @FXML
    public MenuItem LL;

    @FXML
    public MenuItem LE;

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

    private OpenedProjectViewModel viewModel;

    private static Stage primaryStage;

    private static String filePath;

    @FXML
    private void initialize() throws SerializationException {
        primaryStage = new Stage();

        viewModel = new OpenedProjectViewModel(filePath);

//        treeView.setEditable(true);
        treeView.rootProperty().bindBidirectional(viewModel.rootItemPropertyProperty());
        if (viewModel.selectedItemProperty().getValue() != null) {
            treeView.getSelectionModel().select(treeView.getRow(viewModel.selectedItemProperty().getValue()));
        } else {
            treeView.getSelectionModel().selectFirst();
        }
        viewModel.selectedItemProperty().bind(treeView.getSelectionModel().selectedItemProperty());

        attributeColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        attrTable.itemsProperty().bindBidirectional(viewModel.tableProperty());
        ddlArea.textProperty().bind(viewModel.ddlProperty());

        viewModel.needToConnectProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                TextInputDialog dialog = new TextInputDialog("password");
                dialog.setTitle("password required");
                dialog.setHeaderText("Enter the password and try again");
                dialog.setContentText("Please enter the password:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(s -> {
                    try {
                        viewModel.reconnect(s);
                    } catch (WrongCredentialsException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error ");
                        alert.setHeaderText("Wrong password");
                        alert.setContentText("Ooops, there was an error!");
                        alert.showAndWait();
                    }
                });
                if (!result.isPresent()) {
                    viewModel.needToConnectProperty().set(false);
                }
            }
        });

        treeView.disableProperty().bind(viewModel.treeIsBeenLoadingProperty());
        progress.visibleProperty().bind(treeView.disableProperty());

//        FL.disableProperty().bind(viewModel.fullyLoadedItemProperty());
//        LL.disableProperty().bind(viewModel.lazyLoadedItemProperty());
//        LE.disableProperty().bind(viewModel.elementLoadedProperty());

        viewModel.foundItemProperty().addListener((observable, oldValue, newValue) -> {
            treeView.getSelectionModel().select(newValue);
            int index = treeView.getRow(newValue);
            treeView.scrollTo(index);
        });

        treeView.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            if (!viewModel.isShowContext() || event.getTarget().getClass() != LabeledText.class) {
                event.consume();
            }
        });
    }

    public void showNew(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/openedProject.fxml"));
        Node source = (Node) event.getSource();
        Scene scene1 = source.getScene();
        Stage primaryStage1 = (Stage) scene1.getWindow();
        Stage s = (Stage) primaryStage1.getOwner();
        s.close();
        Scene scene = new Scene(root);
        primaryStage.setTitle("DBBest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showExisting(ActionEvent event, String file) throws IOException {
        filePath = file;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/openedProject.fxml"));
        Node source = (Button) event.getSource();
        Scene scene1 = source.getScene();
        Stage primaryStage1 = (Stage) scene1.getWindow();
        primaryStage1.close();
        Scene scene = new Scene(root);
        primaryStage.setTitle("DBBest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void lazyLoad(ActionEvent actionEvent) {
        viewModel.lazyLoad();
    }

    public void loadElement(ActionEvent actionEvent) {
        viewModel.loadElement();
    }

    public void fullLoad(ActionEvent actionEvent) {
        viewModel.fullLoad();
    }

    public void searchElement(ActionEvent actionEvent) {
        viewModel.searchElement(searchText.getText());
    }

    public void previousElement(ActionEvent actionEvent) {
        viewModel.previousElement();
    }

    public void nextElement(ActionEvent actionEvent) {
        viewModel.nextElement();
    }

    public void saveProject(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("save");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        try {
            File file = chooser.showDialog(primaryStage);
            viewModel.saveProject(file);
        } catch (SerializationException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("problems with saving");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
    }

    public void saveDDL(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save DDL path");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.getExtensionFilters().add(filter1);
        File saveFile = fileChooser.showSaveDialog(primaryStage);
        viewModel.saveDDL(saveFile);
    }

    public void generateFullDDl(ActionEvent actionEvent) {
        try {
            viewModel.generateFullDDl();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save DDL path");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
            fileChooser.getExtensionFilters().add(filter);
            fileChooser.getExtensionFilters().add(filter1);
            File saveFile = fileChooser.showSaveDialog(primaryStage);
            viewModel.saveFullDdl(saveFile);
        } catch (DdlGenerationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Something went wrong ...");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void showHelp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        // todo write help text
        alert.setContentText("there will be text about program");

        alert.showAndWait();
    }

    public void loadAll(ActionEvent actionEvent) {
        viewModel.loadAll();
    }

    public void showProjectOptions(ActionEvent actionEvent) {

    }
}
