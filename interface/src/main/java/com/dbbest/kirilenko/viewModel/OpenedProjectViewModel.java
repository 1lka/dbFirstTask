package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exceptions.LoadingException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;
import com.dbbest.kirilenko.service.TreeItemService;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenedProjectViewModel {

    private LoaderManager loaderManager;

    private PrinterManager printerManager;

    private TreeModel selectedTreeModel;

    private List<TreeItem<TreeModel>> found = new ArrayList<>();

    private ObjectProperty<TreeItem<TreeModel>> rootItemProperty = new SimpleObjectProperty<>();

    private ObjectProperty<TreeItem<TreeModel>> selectedItem = new SimpleObjectProperty<>();

    private ObjectProperty<TreeItem<TreeModel>> foundItem = new SimpleObjectProperty<>();

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

    public ObjectProperty<TreeItem<TreeModel>> foundItemProperty() {
        return foundItem;
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
        printerManager = new PrinterManager(loaderManager.getType());

        Node rootNode = new Node(MySQLConstants.DBEntity.SCHEMA);
        rootNode.getAttrs().put(MySQLConstants.AttributeName.SCHEMA_NAME, loaderManager.getDBName());

        TreeModel root = new TreeModel(rootNode);
        rootItemProperty.set(new TreeItem<>(root));

        selectedItem.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTreeModel = newValue.getValue();
                Node selectedNode = selectedTreeModel.getNode();
                table.setValue(newValue.getValue().getTableElements());

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
                if (selectedNode.getAttrs().get("childrenCount") != null) {
                    showContext.set(false);
                } else {
                    showContext.set(true);
                }
            }
        });
    }

    public void fullLoad() {
        Node nodeForLoading = selectedTreeModel.getNode();
        try {
            loaderManager.fullLoadElement(nodeForLoading);
            selectedTreeModel.update();
            service.createTreeItems(selectedItem.getValue());
            String ddlOfNode = printerManager.printDDL(nodeForLoading);
            ddl.set(ddlOfNode);
        } catch (LoadingException ignored) {
        }
    }

    public void lazyLoad() {
        Node nodeForLoading = selectedTreeModel.getNode();
        try {
            loaderManager.lazyChildrenLoad(nodeForLoading);
            selectedTreeModel.update();
            service.createTreeItems(selectedItem.getValue());
        } catch (LoadingException ignored) {
        }
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

    public void searchElement(String s) {
        found = service.search(rootItemProperty.getValue(), s);
        if (found.size() > 0) {
            foundItem.setValue(found.get(0));
        }
    }

    public void previousElement() {
        int next = found.indexOf(foundItem.get()) - 1;
        if (next < 0) {
            return;
        }
        foundItem.setValue(found.get(next));
    }

    public void nextElement() {
        int next = found.indexOf(foundItem.get()) + 1;
        if (found.size() == next) {
            return;
        }
        foundItem.setValue(found.get(next));
    }

    public void saveDDL() {

    }

    public void saveProject(File file) throws SerializationException, IOException {
        SerializationStrategy strategy = new XMLStrategyImpl();
        String pathToFolder = file.getAbsolutePath() + "\\" + loaderManager.getDBName();

        Path path = Paths.get(pathToFolder);
        Files.createDirectories(path);

        File file1 = new File(pathToFolder + "\\project.xml");
        System.out.println(file1.createNewFile());

        strategy.serialize(rootItemProperty.getValue().getValue().getNode(), file1.getAbsolutePath());
    }
}
