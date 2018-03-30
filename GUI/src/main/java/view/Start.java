package view;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import viewModel.ViewModel;

import java.sql.SQLException;

public class Start extends Application {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private void initialize() throws SQLException {
        treeView.setEditable(true);
        viewModel = new ViewModel();
        TreeItem<String> rootNode = new TreeItem<>(viewModel.getRoot().toString());
        treeView.setRoot(rootNode);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> {
            loadNode(treeView.selectionModelProperty());}));
    }

    private void loadNode(ObjectProperty<MultipleSelectionModel<TreeItem<String>>> property) {
        System.out.println(property.get().getSelectedItem().getValue());
    }

    private ViewModel viewModel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("start.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Welcome page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
