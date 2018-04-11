package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.viewModel.OpenedProjectViewModel;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class OpenedProjectView {

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
    public MenuItem saveProjMenuItem;

    @FXML
    private TableView<Map.Entry<String, String>> attrTable;

    @FXML
    private TextArea ddlArea;

    @FXML
    private TreeView<TreeModel> treeView;

    private OpenedProjectViewModel viewModel;

    private static Stage primaryStage;

    @FXML
    private void initialize() {
        primaryStage = new Stage();
        viewModel = new OpenedProjectViewModel();

        treeView.setEditable(true);
        treeView.rootProperty().bindBidirectional(viewModel.rootItemPropertyProperty());
        treeView.getSelectionModel().selectFirst();
        viewModel.selectedItemProperty().bind(treeView.getSelectionModel().selectedItemProperty());

        attributeColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        attrTable.itemsProperty().bindBidirectional(viewModel.tableProperty());
        ddlArea.textProperty().bind(viewModel.ddlProperty());

        FL.disableProperty().bind(viewModel.fullyLoadedItemProperty());
        LL.disableProperty().bind(viewModel.lazyLoadedItemProperty());
        LE.disableProperty().bind(viewModel.elementLoadedProperty());
//        FL.textProperty()

        viewModel.foundItemProperty().addListener((observable, oldValue, newValue) -> {
            treeView.getSelectionModel().select(newValue);
        });


        treeView.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            if (!viewModel.isShowContext() || event.getTarget().getClass() != LabeledText.class) {
                event.consume();
            }
        });
    }

    public void show(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/openedProject.fxml"));
        Node source = (Node) event.getSource();
        Scene scene1 = source.getScene();
        Stage primaryStage1 = (Stage) scene1.getWindow();
        Stage s = (Stage) primaryStage1.getOwner();
        s.close();
        Scene scene = new Scene(root);
        primaryStage = new Stage();
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

    public void fullyLoad(ActionEvent actionEvent) {
        viewModel.fullLoad();
    }

    public void saveDDL(ActionEvent actionEvent) {
        viewModel.saveDDL();
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
        } catch (SerializationException|IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("problems with saving");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText(e.toString());

            alert.showAndWait();
        }
    }
}
