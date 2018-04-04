package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.HashMap;
import java.util.Map;

public class OpenedProjectViewModel {

    private LoaderManager loaderManager;

    private PrinterManager printerManager;

    private ObjectProperty<TreeItem<TreeModel>> rootItemProperty = new SimpleObjectProperty<>();

    private ObjectProperty<TreeItem<TreeModel>> selectedItem = new SimpleObjectProperty<>();

    private ObjectProperty<ObservableList<Map.Entry<String, String>>> table = new SimpleObjectProperty<>();

    private StringProperty ddl = new SimpleStringProperty();

    public StringProperty ddlProperty() {
        return ddl;
    }

    public ObjectProperty<TreeItem<TreeModel>> selectedItemProperty() {
        return selectedItem;
    }

    public ObjectProperty<TreeItem<TreeModel>> rootItemPropertyProperty() {
        return rootItemProperty;
    }

    public ObjectProperty<ObservableList<Map.Entry<String, String>>> tableProperty() {
        return table;
    }

    public OpenedProjectViewModel() {
        loaderManager = LoaderManager.getInstance();
        System.out.println(loaderManager);

        //todo костыль
        printerManager = new PrinterManager(loaderManager.getType());



        Node rootNode = new Node(MySQLConstants.DBEntity.SCHEMA);
        String schemaName = "sakila";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.SCHEMA_NAME, schemaName);
        rootNode.setAttrs(attrs);
        TreeModel root = new TreeModel(rootNode);
        rootItemProperty.set(new TreeItem<>(root));

        selectedItem.addListener((observable, oldValue, newValue) -> {
            Node selectedNode = newValue.getValue().getNode();

            Map<String, String> map = selectedNode.getAttrs();
            table.setValue(FXCollections.observableArrayList(map.entrySet()));

            String ddlOfNode = printerManager.printDDL(selectedNode);
            ddl.set(ddlOfNode);
        });


    }

    public void lazyLoad() {
        Node nodeForLoading = selectedItem.getValue().getValue().getNode();
        loaderManager.lazyChildrenLoad(nodeForLoading);
        TreeModel nm = new TreeModel(nodeForLoading);
        selectedItem.get().getChildren().addAll(nm.getChildren());
    }

    public void fullyLoad() {
        Node nodeForLoading = selectedItem.getValue().getValue().getNode();
        loaderManager.fullLoadElement(nodeForLoading);
        TreeModel nm = new TreeModel(nodeForLoading);
        selectedItem.get().getChildren().addAll(nm.getChildren());
    }
}
