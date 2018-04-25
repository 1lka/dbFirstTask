package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exception.DdlGenerationException;
import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.interactionWithDB.printers.PrinterManager;
import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;
import com.dbbest.kirilenko.service.ProgramSettings;
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

    private String fullDDL;

    private StringProperty ddl = new SimpleStringProperty();

    private TreeItemService service = new TreeItemService();

    private LoaderManager loaderManager;

    private PrinterManager printerManager;

    private Node settingsNode;

    private TreeModel selectedTreeModel;

    private String pathToFolder;

    private BooleanProperty treeIsBeenLoading = new SimpleBooleanProperty();

    private List<TreeItem<TreeModel>> found = new ArrayList<>();

    private BooleanProperty onlineMode = new SimpleBooleanProperty();

    public BooleanProperty onlineModeProperty() {
        return onlineMode;
    }

    private ObjectProperty<TreeItem<TreeModel>> rootItemProperty = new SimpleObjectProperty<>();

    public BooleanProperty treeIsBeenLoadingProperty() {
        return treeIsBeenLoading;
    }

    private ObjectProperty<TreeItem<TreeModel>> selectedItem = new SimpleObjectProperty<>();

    private ObjectProperty<TreeItem<TreeModel>> foundItem = new SimpleObjectProperty<>();

    private ObjectProperty<ObservableList<Map.Entry<String, String>>> table = new SimpleObjectProperty<>();

    private BooleanProperty showContext = new SimpleBooleanProperty(true);

    public ObjectProperty<TreeItem<TreeModel>> foundItemProperty() {
        return foundItem;
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

    private String url;
    private String dbName;
    private String login;
    private DBType type;

    public String getUrl() {
        return url;
    }

    public String getDbName() {
        return dbName;
    }

    public String getLogin() {
        return login;
    }

    public DBType getDbType() {
        return type;
    }

    public OpenedProjectViewModel(String pathToFolder, Connect connect) throws SerializationException {
        if (pathToFolder == null) {
            onlineMode.set(true);

            loaderManager = new LoaderManager(connect);
            printerManager = new PrinterManager(loaderManager.getType());
            Node rootNode = new Node(MySQLConstants.DBEntity.SCHEMA);
            rootNode.getAttrs().put(MySQLConstants.AttributeName.NAME, loaderManager.getDBName());
            TreeModel root = new TreeModel(rootNode);
            rootItemProperty.set(new TreeItem<>(root));
        } else {
            this.pathToFolder = pathToFolder;
            SerializationStrategy strategy = new XMLStrategyImpl();
            String project = pathToFolder + "\\tree.xml";
            String projectSettings = pathToFolder + "\\settings.xml";

            Node root = strategy.deserialize(project);
            settingsNode = strategy.deserialize(projectSettings);

            TreeItem<TreeModel> rootTreeItem = new TreeItem<>(new TreeModel(root));
            service.createTreeItems(rootTreeItem);
            service.restoreExpandedItems(rootTreeItem,settingsNode);
            service.restoreSelectedItem(rootTreeItem, settingsNode,selectedItem);

            rootItemProperty.setValue(rootTreeItem);

            this.type = DBType.valueOf(settingsNode.getAttrs().get("dbType"));

            loaderManager = new LoaderManager();
            loaderManager.setType(type);
            loaderManager.setDBName(settingsNode.getAttrs().get("dbName"));
            loaderManager.setLogin(settingsNode.getAttrs().get("login"));
            loaderManager.setUrl(settingsNode.getAttrs().get("url"));
            printerManager = new PrinterManager(type);
        }
        this.url = loaderManager.getUrl();
        this.dbName = loaderManager.getDBName();
        this.login = loaderManager.getLogin();

        selectedItem.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTreeModel = newValue.getValue();
                Node selectedNode = selectedTreeModel.getNode();
                table.setValue(newValue.getValue().getTableElements());

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
            onlineMode.set(true);
        }
    }

    public File getProjectsFolder() {
        return new File(ProgramSettings.getProp().getProperty("project"));
    }

    @FunctionalInterface
    private interface LoadInterface {
        void load();
    }

    ///////////////////////////////
    public boolean checkFolder() {
        if (pathToFolder != null) {
            return true;
        }
        String path = ProgramSettings.getProp().getProperty("project") + loaderManager.getDBName();
        File file = new File(path);
        return file.exists();
    }

    public void saveCurrent() throws SerializationException {
        serializeProject(pathToFolder);
    }

    public void saveProject(File folder) throws SerializationException, IOException {
        String pathToFolder = folder.getAbsolutePath() + "\\" + loaderManager.getDBName();

        Path path = Paths.get(pathToFolder);
        Files.createDirectories(path);

        serializeProject(pathToFolder);
        this.pathToFolder = pathToFolder;
    }

    private void serializeProject(String pathToFolder) throws SerializationException {
        SerializationStrategy strategy = new XMLStrategyImpl();

        File project = new File(pathToFolder + "\\tree.xml");
        strategy.serialize(rootItemProperty.getValue().getValue().getNode(), project.getAbsolutePath());

        File settings = new File(pathToFolder + "\\settings.xml");
        List<Node> expandedNodes = TreeStateSerialization.chooseExpanded(rootItemProperty.getValue());
        Node expanded = new Node("expanded");
        expanded.addChildren(expandedNodes);

        Node selected = TreeStateSerialization.chooseSelected(selectedItem.get());

        Node projectSettings = new Node("project");
        projectSettings.addChild(expanded);
        projectSettings.addChild(selected);

        projectSettings.getAttrs().put("dbType", String.valueOf(loaderManager.getType()));
        projectSettings.getAttrs().put("url", loaderManager.getUrl());
        projectSettings.getAttrs().put("dbName", loaderManager.getDBName());
        projectSettings.getAttrs().put("login", loaderManager.getLogin());

        strategy.serialize(projectSettings, settings.getAbsolutePath());
    }


    ////////////////////////////
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

    public void reconnect(String password) throws WrongCredentialsException {
        Connect connect = ConnectFactory.getConnect(type);
        try {
            connect.initConnection(url,login,password);
            loaderManager = new LoaderManager(connect);
            loaderManager.setDBName(dbName);
        } catch (SQLException e) {
            throw new WrongCredentialsException("wrong password");
        }
    }
}
