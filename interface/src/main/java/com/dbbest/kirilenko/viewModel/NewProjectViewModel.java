package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.sql.SQLException;

public class NewProjectViewModel {

    private StringProperty url = new SimpleStringProperty();
    private StringProperty dbName = new SimpleStringProperty();
    private StringProperty login = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    private StringProperty dbFullAddress = new SimpleStringProperty();


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

    public String getLogin() {
        return login.get();
    }

    public StringProperty loginProperty() {
        return login;
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getDbName() {
        return dbName.get();
    }

    public StringProperty dbNameProperty() {
        return dbName;
    }

    public LoaderManager connect() {
        if (manager == null) {
            try {
                manager = LoaderManager.getInstance(DBType.MYSQL, getUrl(), getLogin(), getPassword());
                System.err.println("connected");
            } catch (SQLException e) {
                System.err.println("not connected");
            }
        }
        return manager;
    }
}
