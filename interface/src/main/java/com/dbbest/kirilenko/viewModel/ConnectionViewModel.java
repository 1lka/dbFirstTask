package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Arrays;

public class ConnectionViewModel {

    private StringProperty url = new SimpleStringProperty();
    private StringProperty dbName = new SimpleStringProperty();
    private StringProperty login = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private ObservableList<DBType> choicesList = FXCollections.observableList(Arrays.asList(DBType.values()));

    private LoaderManager manager;

    //todo change later
    public ConnectionViewModel() {

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

    public ObservableList<DBType> getChoicesList() {
        return choicesList;
    }

    public StringProperty dbNameProperty() {
        return dbName;
    }

    public Connect connect(DBType selectedItem) throws WrongCredentialsException {
        Connect connect = ConnectFactory.getConnect(selectedItem);
        try {
            connect.initConnection(url.get(), login.get(), password.get());
            connect.setDbName(dbName.get());
        } catch (SQLException e) {
            throw new WrongCredentialsException("cant obtain connect for " + url.get() + " database");
        }
        return connect;
    }
}
