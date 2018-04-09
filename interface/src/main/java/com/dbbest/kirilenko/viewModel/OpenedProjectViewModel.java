package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.service.TreeItemService;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
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

    private BooleanProperty elementLoaded = new SimpleBooleanProperty();

    private BooleanProperty lazyLoadedItem = new SimpleBooleanProperty();

    private BooleanProperty fullyLoadedItem = new SimpleBooleanProperty();

    private ObjectProperty<ObservableList<Map.Entry<String, String>>> table = new SimpleObjectProperty<>();

    private BooleanProperty showContext = new SimpleBooleanProperty(true);

    private StringProperty ddl = new SimpleStringProperty();

    private TreeItemService service = new TreeItemService();

    public BooleanProperty lazyLoadedItemProperty() {
        return lazyLoadedItem;
    }

    public BooleanProperty elementLoadedProperty() {
        return elementLoaded;
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

//    private ListChangeListener<TreeModel> listener = c -> {
//        while (c.next()) {
//            for (TreeModel model : c.getAddedSubList()) {
//                System.out.println(model.getNode());
//            }
//        }
//        System.out.println();
//    };

    public OpenedProjectViewModel() {
        loaderManager = LoaderManager.getInstance();
        printerManager = new PrinterManager(loaderManager.getType());

        Node rootNode = new Node(MySQLConstants.DBEntity.SCHEMA);
        rootNode.getAttrs().put(MySQLConstants.AttributeName.SCHEMA_NAME, loaderManager.getDBName());

        TreeModel root = new TreeModel(rootNode);
        rootItemProperty.set(new TreeItem<>(root));

        selectedItem.addListener((observable, oldValue, newValue) -> {
            selectedTreeModel = newValue.getValue();
            Node selectedNode = selectedTreeModel.getNode();
            table.setValue(newValue.getValue().getTableElements());
//            selectedTreeModel.getChildren().addListener(listener);

            fullyLoadedItem.bind(selectedTreeModel.fullyLoadedProperty());
            lazyLoadedItem.bind(selectedTreeModel.lazyLoadedProperty());
            elementLoaded.bind(selectedTreeModel.elementLoadedProperty());

            try {
                String ddlOfNode = printerManager.printDDL(selectedNode);
                ddl.set(ddlOfNode);
            } catch (NullPointerException e) {
                ddl.setValue("nothing to show");
            }

            //todo create mechanism to ignoring container node
//            String name = selectedNode.getName();
//            if (name.equals("tables")||name.equals(MySQLConstants.NodeNames)) {
//                showContext.set(false);
//            } else {
//                showContext.set(true);
//            }
        });
    }

    public void fullLoad() {
        Node nodeForLoading = selectedTreeModel.getNode();
        try {
            loaderManager.fullLoadElement(nodeForLoading);
            selectedTreeModel.update();
        } catch (LoadingException e) {
            return;
        }
        service.createTreeItems(selectedItem.getValue());
        String ddlOfNode = printerManager.printDDL(nodeForLoading);
        ddl.set(ddlOfNode);
    }

    public void lazyLoad() {
        Node nodeForLoading = selectedTreeModel.getNode();
        try {
            loaderManager.lazyChildrenLoad(nodeForLoading);
            selectedTreeModel.update();
        } catch (LoadingException e) {
            return;
        }
        service.createTreeItems(selectedItem.getValue());
    }

    public void loadElement() {
        Node nodeForLoading = selectedTreeModel.getNode();
        try {
            loaderManager.loadElement(nodeForLoading);
            selectedTreeModel.update();
        } catch (LoadingException e) {
            return;
        }
        String ddlOfNode = printerManager.printDDL(nodeForLoading);
        ddl.set(ddlOfNode);
    }
}
