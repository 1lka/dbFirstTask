package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.service.TreeItemService;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.HashMap;
import java.util.Map;

public class OpenedProjectViewModel {

    private LoaderManager loaderManager;
    private PrinterManager printerManager;
    private TreeModel selectedTreeModel;

    private ObjectProperty<TreeItem<TreeModel>> rootItemProperty = new SimpleObjectProperty<>();

    private ObjectProperty<TreeItem<TreeModel>> selectedItem = new SimpleObjectProperty<>();

    private BooleanProperty lazyLoadedItem = new SimpleBooleanProperty(false);

    private BooleanProperty fullyLoadedItem = new SimpleBooleanProperty(false);

    private ObjectProperty<ObservableList<Map.Entry<String, String>>> table = new SimpleObjectProperty<>();

    private BooleanProperty showContext = new SimpleBooleanProperty();

    private StringProperty ddl = new SimpleStringProperty();

    private TreeItemService service = new TreeItemService();

    public BooleanProperty lazyLoadedItemProperty() {
        return lazyLoadedItem;
    }

    public BooleanProperty fullyLoadedItemProperty() {
        return fullyLoadedItem;
    }

    public boolean isShowContext() {
        return showContext.get();
    }

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
            selectedTreeModel = newValue.getValue();
            Node selectedNode = selectedTreeModel.getNode();
            table.setValue(newValue.getValue().getTableElements());
            lazyLoadedItem.set(selectedTreeModel.lazyLoadedProperty().get());
            fullyLoadedItem.set(selectedTreeModel.fullyLoadedProperty().get());

            try {
                String ddlOfNode = printerManager.printDDL(selectedNode);
                ddl.set(ddlOfNode);
            } catch (NullPointerException e) {
                ddl.setValue("nothing to show");
            }

            //todo create mechanism to ignoring conteiner node
            if (selectedNode.getName().equals("tables")) {
                showContext.set(false);
            } else {
                showContext.set(true);
            }
        });

    }

    public void fullyLoad() {
        Node nodeForLoading = selectedTreeModel.getNode();
        try {
            loaderManager.fullLoadElement(nodeForLoading);
            selectedTreeModel.fullyLoadedProperty().set(true);
            selectedTreeModel.lazyLoadedProperty().set(true);
            selectedTreeModel.update();
        } catch (LoadingException e) {
            return;
        }
        service.createTreeItems(selectedItem.getValue());

        String ddlOfNode = printerManager.printDDL(nodeForLoading);
        ddl.set(ddlOfNode);
        fullyLoadedItem.set(true);
    }

    public void lazyLoad() {
        Node nodeForLoading = selectedTreeModel.getNode();
        try {
            loaderManager.lazyChildrenLoad(nodeForLoading);
            selectedTreeModel.lazyLoadedProperty().set(true);
            selectedTreeModel.update();
        } catch (LoadingException e) {
            return;
        }
        service.createTreeItems(selectedItem.getValue());
        lazyLoadedItem.set(true);
    }
}
