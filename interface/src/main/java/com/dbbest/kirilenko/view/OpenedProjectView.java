package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.viewModel.OpenedProjectViewModel;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Map;

public class OpenedProjectView {

    @FXML
    public TableColumn<Map.Entry<String, String>, String> attributeColumn;

    @FXML
    public TableColumn<Map.Entry<String, String>, String> valueColumn;

    @FXML
    public ContextMenu contextMenu;

    @FXML
    private TableView<Map.Entry<String, String>> attrTable;

    @FXML
    private TextArea ddlArea;

    @FXML
    private TreeView<TreeModel> treeView;

    private OpenedProjectViewModel viewModel;

    @FXML
    private void initialize() {
        viewModel = new OpenedProjectViewModel();

        treeView.setEditable(true);
        treeView.rootProperty().bindBidirectional(viewModel.rootItemPropertyProperty());
        treeView.getSelectionModel().select(0);
        viewModel.selectedItemProperty().bind(treeView.getSelectionModel().selectedItemProperty());

        attributeColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getKey()));
        valueColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getValue()));

        attrTable.itemsProperty().bindBidirectional(viewModel.tableProperty());
        ddlArea.textProperty().bind(viewModel.ddlProperty());

        treeView.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {

            if (!viewModel.isShowContext() || event.getTarget().getClass() != LabeledText.class) {
                event.consume();
            }

        }
    );


}

    private Stage primaryStage;

    public void show(ActionEvent event, TextField dbName) throws Exception {
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

    }

    public void fullyLoad(ActionEvent actionEvent) {
        viewModel.fullyLoad();
    }
}
