package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.model.ConnectModel;
import com.dbbest.kirilenko.service.ProgramSettings;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Arrays;

public class ConnectionViewModel {

    private StringProperty url = new SimpleStringProperty();
    private BooleanProperty isConnecting = new SimpleBooleanProperty();
    private StringProperty dbName = new SimpleStringProperty();
    private StringProperty login = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private ObservableList<DBType> choicesList = FXCollections.observableList(Arrays.asList(DBType.values()));

    private ObservableList<ConnectModel> recentlyUsed = FXCollections.observableArrayList();

    public ObservableList<ConnectModel> getRecentlyUsed() {
        return recentlyUsed;
    }

    public BooleanProperty isConnectingProperty() {
        return isConnecting;
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

    private ObjectProperty<ConnectModel> selectedConnectModel = new SimpleObjectProperty<>();

    public ConnectionViewModel() {
        Node cash = ProgramSettings.getCash();
        try {
            for (Node n : cash.getChildren()) {
                recentlyUsed.add(new ConnectModel(n.getAttrs().get("url"), n.getAttrs().get("db"), n.getAttrs().get("login")));
            }
        } catch (NullPointerException ignored) {
        }

        selectedConnectModel.addListener((observable, oldValue, newValue) -> {
            url.set(newValue.getUrl());
            dbName.set(newValue.getDb());
            login.set(newValue.getLogin());
        });

    }

    public ObjectProperty<ConnectModel> selectedConnectModelProperty() {
        return selectedConnectModel;
    }

    public Connect connect(DBType selectedItem) throws WrongCredentialsException {
        Connect connect = ConnectFactory.getConnect(selectedItem);
        isConnecting.set(true);

        try {
            connect.initConnection(url.get(), login.get(), password.get());
            connect.setDbName(dbName.get());
            ProgramSettings.storeConnect(url.get(), dbName.get(), login.get());
        } catch (SQLException e) {
            throw new WrongCredentialsException("wrong pass");
        } finally {
            isConnecting.set(false);
        }
        return connect;
    }
}
