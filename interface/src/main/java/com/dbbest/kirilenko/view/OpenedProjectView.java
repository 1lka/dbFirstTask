package com.dbbest.kirilenko.view;

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
import javafx.stage.Stage;
import javafx.util.Callback;

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
    private TableView<Map.Entry<String, String>> attrTable;

    @FXML
    private TextArea ddlArea;

    @FXML
    private TreeView<TreeModel> treeView;

    private OpenedProjectViewModel viewModel;
    private Stage primaryStage;

    @FXML
    private void initialize() {
        viewModel = new OpenedProjectViewModel();

        treeView.setEditable(true);
        treeView.rootProperty().bindBidirectional(viewModel.rootItemPropertyProperty());
        treeView.getSelectionModel().select(0);
        viewModel.selectedItemProperty().bind(treeView.getSelectionModel().selectedItemProperty());

        attributeColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        attrTable.itemsProperty().bindBidirectional(viewModel.tableProperty());
        ddlArea.textProperty().bind(viewModel.ddlProperty());

        FL.disableProperty().bind(viewModel.fullyLoadedItemProperty());
        LL.disableProperty().bind(viewModel.lazyLoadedItemProperty());
        LE.disableProperty().bind(viewModel.elementLoadedProperty());

        treeView.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            if (!viewModel.isShowContext() || event.getTarget().getClass() != LabeledText.class) {
                event.consume();
            }
        });

    }

    public void show(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/openedProject.fxml"));
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        scene.setRoot(root);
        scene.getWindow().setHeight(600);
        scene.getWindow().setWidth(600);
        Stage stage = (Stage) scene.getWindow();
        stage.setResizable(true);
        stage.setMinHeight(400);
        stage.setMinWidth(400);
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
}
