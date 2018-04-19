package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exception.DdlGenerationException;
import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;
import com.dbbest.kirilenko.service.TreeItemService;
import com.dbbest.kirilenko.service.TreeStateSerialization;
import com.dbbest.kirilenko.tree.Node;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenedProjectViewModel {

    private BooleanProperty treeIsBeenLoading = new SimpleBooleanProperty();

    public BooleanProperty treeIsBeenLoadingProperty() {
        return treeIsBeenLoading;
    }

    private String fullDDL;

    private LoaderManager loaderManager;

    private Node settingsNode;

    private PrinterManager printerManager;

    private TreeModel selectedTreeModel;

    private List<TreeItem<TreeModel>> found = new ArrayList<>();

    private BooleanProperty onlineMode = new SimpleBooleanProperty();

    private BooleanProperty needToConnect = new SimpleBooleanProperty();

    private ObjectProperty<TreeItem<TreeModel>> rootItemProperty = new SimpleObjectProperty<>();

    private ObjectProperty<TreeItem<TreeModel>> selectedItem = new SimpleObjectProperty<>();

    private ObjectProperty<TreeItem<TreeModel>> foundItem = new SimpleObjectProperty<>();

    private BooleanProperty elementLoaded = new SimpleBooleanProperty();

    private BooleanProperty lazyLoadedItem = new SimpleBooleanProperty();

    private BooleanProperty fullyLoadedItem = new SimpleBooleanProperty();

    private ObjectProperty<ObservableList<Map.Entry<String, String>>> table = new SimpleObjectProperty<>();

    private BooleanProperty showContext = new SimpleBooleanProperty(true);

    public BooleanProperty needToConnectProperty() {
        return needToConnect;
    }

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

    public OpenedProjectViewModel(String pathToFolder) throws SerializationException {
        if (pathToFolder == null) {
            onlineMode.set(true);

            loaderManager = LoaderManager.getInstance();
            printerManager = new PrinterManager(loaderManager.getType());
            Node rootNode = new Node(MySQLConstants.DBEntity.SCHEMA);
            rootNode.getAttrs().put(MySQLConstants.AttributeName.NAME, loaderManager.getDBName());
            TreeModel root = new TreeModel(rootNode);
            rootItemProperty.set(new TreeItem<>(root));
        } else {
            SerializationStrategy strategy = new XMLStrategyImpl();
            String project = pathToFolder + "\\project.xml";
            String projectSettings = pathToFolder + "\\settings.xml";

            Node root = strategy.deserialize(project);
            settingsNode = strategy.deserialize(projectSettings);

            TreeModel rootModel = new TreeModel(root);
            TreeItem<TreeModel> rootTreeItem = new TreeItem<>(rootModel);
            service.restoreTreeState(rootTreeItem, selectedItem, settingsNode.getChildren().get(0));
            rootItemProperty.setValue(rootTreeItem);

            DBType type = DBType.valueOf(settingsNode.getAttrs().get("dbType"));
            printerManager = new PrinterManager(type);
        }

        selectedItem.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTreeModel = newValue.getValue();
                Node selectedNode = selectedTreeModel.getNode();
                table.setValue(newValue.getValue().getTableElements());

//                fullyLoadedItem.bind(selectedTreeModel.fullyLoadedProperty());
//                lazyLoadedItem.bind(selectedTreeModel.lazyLoadedProperty());
//                elementLoaded.bind(selectedTreeModel.elementLoadedProperty());

                try {
                    String ddlOfNode = printerManager.printDDL(selectedNode);
                    ddl.set(ddlOfNode);
                } catch (NullPointerException e) {
                    ddl.setValue("nothing to showNew");
                }
            }
        });
    }

    public void fullLoad() {
        load(() -> {
            loaderManager.fullLoadElement(selectedTreeModel.getNode());
            selectedTreeModel.update();
            service.createTreeItems(selectedItem.getValue());
            String ddlOfNode = printerManager.printDDL(selectedTreeModel.getNode());
            ddl.set(ddlOfNode);
        });
    }

    public void lazyLoad() {
        load(() -> {
            loaderManager.lazyChildrenLoad(selectedTreeModel.getNode());
            selectedTreeModel.update();
            service.createTreeItems(selectedItem.getValue());
        });
    }

    public void loadElement() {
        load(() -> {
            loaderManager.loadElement(selectedTreeModel.getNode());
            selectedTreeModel.update();
            String ddlOfNode = printerManager.printDDL(selectedTreeModel.getNode());
            ddl.set(ddlOfNode);
        });
    }

    public void loadAll() {
        load(() -> {
            loaderManager.fullLoadElement(rootItemProperty.getValue().getValue().getNode());
            rootItemProperty.getValue().getValue().update();
            service.createTreeItems(rootItemProperty.getValue());
        });
    }

    private void load(LoadInterface lambda) {
        if (onlineMode.get()) {
            treeIsBeenLoading.set(true);
            new Thread(() -> {
                lambda.load();
                Platform.runLater(()->{treeIsBeenLoading.set(false);});
            }).start();
        } else {
            needToConnect.set(true);
        }
    }

    @FunctionalInterface
    private interface LoadInterface {
        void load();
    }

    public void reconnect(String password) throws WrongCredentialsException {
        String login = settingsNode.getAttrs().get("login");
        String url = settingsNode.getAttrs().get("url");
        String name = settingsNode.getAttrs().get("dbName");
        DBType type = DBType.valueOf(settingsNode.getAttrs().get("dbType"));
        try {
            loaderManager = LoaderManager.getInstance(type, name, url, login, password);
            onlineMode.set(true);
        } catch (SQLException e) {
            needToConnect.set(false);
            throw new WrongCredentialsException(e);
        }
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

    public void saveProject(File file) throws SerializationException, IOException {
        SerializationStrategy strategy = new XMLStrategyImpl();
        String pathToFolder = file.getAbsolutePath() + "\\" + loaderManager.getDBName();

        Path path = Paths.get(pathToFolder);
        Files.createDirectories(path);

        File project = new File(pathToFolder + "\\project.xml");
        strategy.serialize(rootItemProperty.getValue().getValue().getNode(), project.getAbsolutePath());

        File settings = new File(pathToFolder + "\\settings.xml");
        Node state = TreeStateSerialization.convert(rootItemProperty.getValue(), selectedItem.getValue());

        Node projectSettings = new Node("project");
        projectSettings.addChild(state);
        projectSettings.getAttrs().put("dbType", String.valueOf(loaderManager.getType()));
        projectSettings.getAttrs().put("url", loaderManager.getUrl());
        projectSettings.getAttrs().put("dbName", loaderManager.getDBName());
        projectSettings.getAttrs().put("login", loaderManager.getLogin());

        strategy.serialize(projectSettings, settings.getAbsolutePath());
    }

    public void saveDDL(File saveFile) {
        try(FileWriter writer = new FileWriter(saveFile, false))        {
            writer.write(ddl.get());
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void generateFullDDl() throws DdlGenerationException {
        Node root = rootItemProperty.getValue().getValue().getNode();
        if (!Boolean.valueOf(root.getAttrs().get("fullyLoaded"))) {
            throw new DdlGenerationException("load all tree to generate DDL");
        }
        fullDDL = printerManager.printAllNodes(root);
    }

    public void saveFullDdl(File saveFile) {
        try(FileWriter writer = new FileWriter(saveFile, false))        {
            writer.write(fullDDL);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
