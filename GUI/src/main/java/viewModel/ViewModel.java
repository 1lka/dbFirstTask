package viewModel;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.tree.Node;
import model.NodeModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ViewModel {
    private final static String url = "jdbc:mysql://localhost/?useSSL=false";
    private final static String login = "root";
    private final static String pass = "root";

    private LoaderManager manager;
    private NodeModel root;

    public NodeModel getRoot() {
        return root;
    }

    public void setRoot(NodeModel root) {
        this.root = root;
    }

    public ViewModel() throws SQLException {
        manager = new LoaderManager(DBType.MYSQL, url, login, pass);
        System.out.println("connected");
        Node rootNode = new Node(MySQLConstants.DBEntity.SCHEMA);
        String schemaName = "sakila";
        Map<String, String> attrs = new HashMap<>();
        attrs.put(MySQLConstants.AttributeName.SCHEMA_NAME, schemaName);
        rootNode.setAttrs(attrs);

        root = new NodeModel(rootNode);

    }
}
