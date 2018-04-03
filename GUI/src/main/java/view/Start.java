package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import model.NodeModel;
import viewModel.ViewModel;

import java.sql.SQLException;

public class Start extends Application {

    @FXML
    public MenuItem loadElement;

    @FXML
    public MenuItem doNothing;

    @FXML
    private TreeView<NodeModel> treeView;

    private ViewModel viewModel;

    @FXML
    private void initialize() throws SQLException {
        viewModel = new ViewModel();
        treeView.setEditable(true);
        treeView.rootProperty().bindBidirectional(viewModel.rootItemPropertyProperty());

        treeView.getSelectionModel().select(0);

        viewModel.selectedItemProperty().bind(treeView.getSelectionModel().selectedItemProperty());
    }

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

    public void load(ActionEvent actionEvent) {
        viewModel.load();
    }

    public void doNothing(ActionEvent actionEvent) {
        System.out.println("doing nothing");
    }
}
