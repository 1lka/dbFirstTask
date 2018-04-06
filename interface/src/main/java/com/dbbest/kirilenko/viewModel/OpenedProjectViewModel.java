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

    private ObjectProperty<TreeItem<TreeModel>> rootItemProperty = new SimpleObjectProperty<>();

    private ObjectProperty<TreeItem<TreeModel>> selectedItem = new SimpleObjectProperty<>();

    private ObjectProperty<ObservableList<Map.Entry<String, String>>> table = new SimpleObjectProperty<>();

    private BooleanProperty showContext = new SimpleBooleanProperty();

    private StringProperty ddl = new SimpleStringProperty();

    private TreeItemService service = new TreeItemService();

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
            newValue.getValue().getChildren();
            Node selectedNode = newValue.getValue().getNode();

            Map<String, String> map = selectedNode.getAttrs();

            //todo change to observable entryset
            table.setValue(newValue.getValue().getTableElements());

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
        TreeModel treeModel = selectedItem.getValue().getValue();
        Node nodeForLoading = treeModel.getNode();

        try {
            loaderManager.fullLoadElement(nodeForLoading);
            treeModel.fullyLoadedProperty().set(true);
            treeModel.update();
        } catch (LoadingException e) {
            return;
        }
        service.createTreeItems(selectedItem.getValue());
    }

    public void lazyLoad() {
        Node nodeForLoading = selectedItem.getValue().getValue().getNode();
        try {
            loaderManager.lazyChildrenLoad(nodeForLoading);
        } catch (LoadingException e) {
            return;
        }
        TreeModel nm = new TreeModel(nodeForLoading);
    }
}
