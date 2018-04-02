package viewModel;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import model.NodeModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ViewModel {
    private final static String url = "jdbc:mysql://localhost/?useSSL=false";
    private final static String login = "root";
    private final static String pass = "root";

    private LoaderManager manager;

    private final SimpleObjectProperty<TreeItem<NodeModel>> rootItemProperty = new SimpleObjectProperty<>();


    public TreeItem<NodeModel> getRootItemProperty() {
        return rootItemProperty.get();
    }

    public SimpleObjectProperty<TreeItem<NodeModel>> rootItemPropertyProperty() {
        return rootItemProperty;
    }

    public ViewModel() throws SQLException {
        manager = new LoaderManager(DBType.MYSQL, url, login, pass);
        System.out.println("connected");
        Node rootNode = new Node(MySQLConstants.DBEntity.SCHEMA);
        String schemaName = "sakila";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.SCHEMA_NAME, schemaName);
        rootNode.setAttrs(attrs);
        rootItemProperty.set(new TreeItem<>(new NodeModel(rootNode)));

    }

    public void load(TreeItem<NodeModel> value) {
        Node node = value.getValue().getNode();
        manager.fullLoadElement(node);

        value.getChildren().addAll(new NodeModel(node).getChildren());



    }

    private void addChildren(Node node, NodeModel model) {

    }
}
