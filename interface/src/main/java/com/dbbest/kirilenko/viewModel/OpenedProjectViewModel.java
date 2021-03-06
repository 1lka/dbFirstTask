package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.exception.WrongCredentialsException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.connections.Connect;
import com.dbbest.kirilenko.interactionWithDB.connections.ConnectFactory;
import com.dbbest.kirilenko.interactionWithDB.constants.GeneralConstants;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
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
import org.apache.log4j.Logger;

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

    private final static Logger logger = Logger.getLogger(OpenedProjectViewModel.class);

    private Thread connectionCloser = new Thread();

    private int connectionTimeout = 3000;

    private String fullDDL;

    private StringProperty ddl = new SimpleStringProperty();

    private TreeItemService service = new TreeItemService();

    private LoaderManager loaderManager;

    private PrinterManager printerManager;

    private TreeModel selectedTreeModel;

    private String pathToFolder;

    private BooleanProperty selectedRoot = new SimpleBooleanProperty();

    public BooleanProperty selectedRootProperty() {
        return selectedRoot;
    }

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

    private BooleanProperty showSubtreeMenuItem = new SimpleBooleanProperty(true);

    public BooleanProperty showSubtreeMenuItemProperty() {
        return showSubtreeMenuItem;
    }

    private ObjectProperty<ObservableList<Map.Entry<String, String>>> table = new SimpleObjectProperty<>();

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

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    private String url;
    private String port;
    private String dbName;
    private String login;
    private DBType type;

    public String getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public String getDbName() {
        return dbName;
    }

    public String getLogin() {
        return login;
    }

    public OpenedProjectViewModel(String pathToFolder, Connect connect) throws SerializationException {
        onlineMode.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                updateConnectionCloseTimeout();
            }
        });

        if (pathToFolder == null) {
            loaderManager = new LoaderManager(connect);
            type = connect.getType();
            login = connect.getLogin();
            url = connect.getUrl();
            port = connect.getPort();
            printerManager = new PrinterManager(loaderManager.getType());
            Node rootNode = new Node(GeneralConstants.SCHEMA);
            rootNode.getAttrs().put(GeneralConstants.NAME, loaderManager.getDBName());
            TreeModel root = new TreeModel(rootNode);
            rootItemProperty.set(new TreeItem<>(root));
            onlineMode.set(true);
        } else {
            this.pathToFolder = pathToFolder;


            Node root = deserializeTree();
            Node settingsNode = deserializeSettings();

            TreeItem<TreeModel> rootTreeItem = new TreeItem<>(new TreeModel(root));
            TreeItemService.createTreeItems(rootTreeItem);
            service.restoreExpandedItems(rootTreeItem, settingsNode);
            service.restoreSelectedItem(rootTreeItem, settingsNode, selectedItem);
            selectedTreeModel = selectedItem.getValue().getValue();

            rootItemProperty.setValue(rootTreeItem);

            this.type = DBType.valueOf(settingsNode.getAttrs().get("dbType"));

            loaderManager = new LoaderManager(type);
            loaderManager.setDBName(settingsNode.getAttrs().get("dbName"));
            loaderManager.setLogin(settingsNode.getAttrs().get("login"));
            loaderManager.setUrl(settingsNode.getAttrs().get("url"));
            loaderManager.setPort(settingsNode.getAttrs().get("port"));
            printerManager = new PrinterManager(type);
        }
        this.url = loaderManager.getUrl();
        this.port = loaderManager.getPort();
        this.dbName = loaderManager.getDBName();
        this.login = loaderManager.getLogin();

        //todo ref this shit
        List<String> showSubtree = new ArrayList<>();
        showSubtree.add(MySQLConstants.DBEntity.SCHEMA);
        showSubtree.add(MySQLConstants.DBEntity.TABLE);
        showSubtree.add(MySQLConstants.NodeNames.TABLES);
        showSubtree.add(MySQLConstants.DBEntity.FUNCTION);
        showSubtree.add(MySQLConstants.NodeNames.FUNCTIONS);
        showSubtree.add(MySQLConstants.DBEntity.PROCEDURE);
        showSubtree.add(MySQLConstants.NodeNames.PROCEDURES);
        showSubtree.add(MySQLConstants.NodeNames.VIEWS);

        selectedItem.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTreeModel = newValue.getValue();
                Node selectedNode = selectedTreeModel.getNode();
                table.setValue(newValue.getValue().getTableElements());

                if (showSubtree.contains(newValue.getValue().getNode().getName())) {
                    showSubtreeMenuItem.set(true);
                } else {
                    showSubtreeMenuItem.set(false);
                }

                try {
                    String ddlOfNode = printerManager.printDDL(selectedNode);
                    ddl.set(ddlOfNode);
                } catch (NullPointerException e) {
                    ddl.setValue("nothing to show");
                }
                if (newValue == rootItemProperty.get()) {
                    selectedRoot.set(true);
                } else {
                    selectedRoot.set(false);
                }
            }
        });

        ddlProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                ddl.set("this node can't be printed");
            }
        });

    }

    private Node deserializeSettings() throws SerializationException {
        SerializationStrategy strategy = new XMLStrategyImpl();
        String projectSettings = pathToFolder + "\\settings.xml";
        return strategy.deserialize(projectSettings);
    }

    private Node deserializeTree() throws SerializationException {
        SerializationStrategy strategy = new XMLStrategyImpl();
        String project = pathToFolder + "\\tree.xml";
        return strategy.deserialize(project);

    }

    public synchronized void reload() {
        Node selected = selectedItem.getValue().getValue().getNode();
        selected.getChildren().clear();
        String name = selected.getAttrs().get(GeneralConstants.NAME);
        selected.getAttrs().clear();
        selected.getAttrs().put(GeneralConstants.NAME, name);
        fullLoad();
    }

    public synchronized void fullLoad() {
        load(() -> {
            loaderManager.fullLoadElement(selectedItem.getValue().getValue().getNode());
            selectedItem.getValue().getValue().update();
            TreeItemService.createTreeItems(selectedItem.getValue());
            String ddlOfNode = printerManager.printDDL(selectedItem.getValue().getValue().getNode());
            ddl.set(ddlOfNode);
        });
    }

    public synchronized void lazyLoad() {
        load(() -> {
            loaderManager.lazyChildrenLoad(selectedItem.getValue().getValue().getNode());
            selectedItem.getValue().getValue().update();
            TreeItemService.createTreeItems(selectedItem.getValue());
            String ddlOfNode = printerManager.printDDL(selectedItem.getValue().getValue().getNode());
            ddl.set(ddlOfNode);
        });
    }

    public synchronized void loadElement() {
        load(() -> {
            loaderManager.loadElement(selectedItem.getValue().getValue().getNode());
            selectedTreeModel.update();
            String ddlOfNode = printerManager.printDDL(selectedItem.getValue().getValue().getNode());
            ddl.set(ddlOfNode);
        });
    }

    public synchronized void loadAll() {
        load(() -> {
            loaderManager.fullLoadElement(rootItemProperty.getValue().getValue().getNode());
            rootItemProperty.getValue().getValue().update();
            TreeItemService.createTreeItems(rootItemProperty.getValue());
        });
    }

    private void load(LoadInterface lambda) {
        updateConnectionCloseTimeout();
        Thread loadingThread = new Thread(() -> {
            try {
                lambda.load();
            } finally {
                Platform.runLater(() -> treeIsBeenLoading.set(false));
            }
        });
        loadingThread.setDaemon(true);
        loadingThread.start();
    }

    public File getProjectsFolder() {
        File file = new File(ProgramSettings.getProp().getProperty("project"));
        if (!file.exists()) {
            file = new File(ProgramSettings.getProp().getProperty("root"));
        }
        return file;
    }


    public void updateConnectionCloseTimeout() {
        if (onlineMode.get() && connectionTimeout > 0) {
            connectionCloser.interrupt();

            connectionCloser = new Thread(() -> {
                int i = connectionTimeout;
                try {
                    while (i > 0) {
                        Thread.sleep(1000);
                        i--;
                    }
                    try {
                        Platform.runLater(() -> onlineMode.set(false));
                        loaderManager.getConnection().close();
                    } catch (SQLException e) {
                        logger.error(e);
                    }
                } catch (InterruptedException e) {
                    logger.debug(e);
                }
            });
            connectionCloser.setDaemon(true);
            connectionCloser.start();
        }
    }

    @FunctionalInterface
    private interface LoadInterface {
        void load();
    }

    ///////////////////////////////
    public boolean checkFolder() {
        return pathToFolder != null;
    }

    public void saveCurrent() throws SerializationException {
        serializeProject(pathToFolder);
    }

    public void saveProject(File folder) throws SerializationException, IOException {
        String pathToFolder = folder.getAbsolutePath();

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
        projectSettings.getAttrs().put("port", loaderManager.getPort());
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
        try (FileWriter writer = new FileWriter(saveFile, false)) {
            writer.write(ddl.get());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void generateFullDDl() {
        Node root = rootItemProperty.getValue().getValue().getNode();
        fullDDL = printerManager.printAllNodes(root);
    }

    public boolean isTreeFullyLoaded() {
        Node root = rootItemProperty.getValue().getValue().getNode();
        return Boolean.valueOf(root.getAttrs().get("fullyLoaded"));
    }

    public void saveFullDdl(File saveFile) {
        try (FileWriter writer = new FileWriter(saveFile, false)) {
            writer.write(fullDDL);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void reconnect(String password) throws WrongCredentialsException {
        Connect connect = ConnectFactory.getConnect(type);
        try {
            connect.initConnection(url, port, login, password);
            loaderManager = new LoaderManager(connect);
            loaderManager.setDBName(dbName);
        } catch (SQLException e) {
            throw new WrongCredentialsException("wrong password");
        }
    }
}
