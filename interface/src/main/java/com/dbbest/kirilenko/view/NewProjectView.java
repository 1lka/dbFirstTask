package com.dbbest.kirilenko.view;

import com.dbbest.kirilenko.viewModel.NewProjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
}





