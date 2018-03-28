package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.sql.SQLException;

public class NewProjectViewModel {

    private StringProperty url = new SimpleStringProperty();
    private StringProperty login = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private LoaderManager manager;
    //todo change later
    public NewProjectViewModel() {

    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getLogin() {
        return login.get();
    }

    public StringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }



    public boolean connect() {
        if (manager == null) {
            try {
                manager = new LoaderManager(DBType.MYSQL, getUrl(), getLogin(), getPassword());
                System.out.println("connected");
                return true;
            } catch (SQLException e) {
                System.err.println("not connected");
                return false;
            }
        }
        return true;
    }
}
