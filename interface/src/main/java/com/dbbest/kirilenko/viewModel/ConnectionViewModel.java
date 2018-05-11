package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
import com.dbbest.kirilenko.model.ConnectModel;
import com.dbbest.kirilenko.service.ProgramSettings;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Arrays;

public class ConnectionViewModel {

    private final static Logger logger = Logger.getLogger(ConnectionViewModel.class);

    private BooleanProperty showButton = new SimpleBooleanProperty();

    private StringProperty url = new SimpleStringProperty();
    private StringProperty port = new SimpleStringProperty();
    private StringProperty dbName = new SimpleStringProperty();
    private StringProperty login = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private ObservableList<DBType> choicesList = FXCollections.observableList(Arrays.asList(DBType.values()));

    public StringProperty portProperty() {
        return port;
    }

    private ObservableList<ConnectModel> recentlyUsed = FXCollections.observableArrayList();

    public ObservableList<ConnectModel> getRecentlyUsed() {
        return recentlyUsed;
    }

    public StringProperty urlProperty() {
        return url;
    }

    public StringProperty loginProperty() {
        return login;
    }

    public StringProperty passwordProperty() {
        return password;
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
                recentlyUsed.add(new ConnectModel(n.getAttrs().get("url"),n.getAttrs().get("port"), n.getAttrs().get("db"), n.getAttrs().get("login")));
            }
        } catch (NullPointerException ignored) {
        }

        selectedConnectModel.addListener((observable, oldValue, newValue) -> {
            url.set(newValue.getUrl());
            port.set(newValue.getPort());
            dbName.set(newValue.getDb());
            login.set(newValue.getLogin());
        });
        BooleanProperty u = new SimpleBooleanProperty();
        url.addListener((observable, oldValue, newValue) -> {

        });
//        BooleanBinding binding =


    }

    public ObjectProperty<ConnectModel> selectedConnectModelProperty() {
        return selectedConnectModel;
    }

    public Connect connect(DBType selectedItem) throws WrongCredentialsException {
        Connect connect = ConnectFactory.getConnect(selectedItem);
        try {
            connect.initConnection(url.get(), port.get(), login.get(), password.get());
            connect.setDbName(dbName.get());
            ProgramSettings.storeConnect(url.get(), port.get(), dbName.get(), login.get());
        } catch (SQLException e) {
            logger.info("error while trying to connect to " + url.get(), e);
            throw new WrongCredentialsException("wrong pass");
        }
        return connect;
    }
}
