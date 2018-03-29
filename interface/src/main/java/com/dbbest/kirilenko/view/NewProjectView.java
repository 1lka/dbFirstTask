package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.viewModel.NewProjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class NewProjectView {

    @FXML
    private TextField url;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private Button btnConnect;

    @FXML
    private Text errorMessage;

    private NewProjectViewModel newProjectViewModel;
    private Stage stage;

    @FXML
    private void initialize() throws IOException {
        newProjectViewModel = new NewProjectViewModel();

        newProjectViewModel.urlProperty().bind(url.textProperty());
        newProjectViewModel.loginProperty().bind(login.textProperty());
        newProjectViewModel.passwordProperty().bind(password.textProperty());
    }

    public void connect(ActionEvent actionEvent) throws Exception {
        if (newProjectViewModel.connect()) {
            errorMessage.setText("");
            OpenedProjectView openedProject = new OpenedProjectView();
            openedProject.show(actionEvent);
        } else {
            errorMessage.setText("not connected");
        }
    }

    public void show(ActionEvent actionEvent) throws IOException {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("New project");
            stage.setResizable(false);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/newProject.fxml"));
            Parent root = fxmlLoader.load();

            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);

            Node source = (Node) actionEvent.getSource();
            Window mainStage = source.getScene().getWindow();
            stage.initOwner(mainStage);
        }
        stage.showAndWait(); // для ожидания закрытия окна
    }
}





