package com.dbbest.kirilenko.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class OpenedProjectView {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private void initialize() {
        TreeItem<String> root = new TreeItem<>("root String");
        root.setExpanded(false);

        TreeItem<String> root1 = new TreeItem<>("root1");
        TreeItem<String> root2 = new TreeItem<>("root2");
        TreeItem<String> root3 = new TreeItem<>("root3");

        root.getChildren().add(root1);
        root.getChildren().add(root2);
        root.getChildren().add(root3);

        treeView.setEditable(true);
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> {treeView.edit(new TreeItem<>("edited"));}));
        treeView.setRoot(root);

    }

    private Stage primaryStage;

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

//        primaryStage.setResizable(true);
//        primaryStage = new Stage();
//        primaryStage.setTitle("Ilka");
//        primaryStage.setScene(scene);
//
//        primaryStage.initOwner(source.getScene().getWindow());
//        primaryStage.initModality(Modality.WINDOW_MODAL);
//
//        primaryStage.show();
    }

    public void expandTreeItem(MouseEvent mouseEvent) {

    }
}
