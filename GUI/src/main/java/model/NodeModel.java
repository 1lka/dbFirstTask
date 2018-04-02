package model;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.tree.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.Map;

public class NodeModel {

    private Node node;

    private ObservableList<TreeItem<NodeModel>> children;

    public void setNode(Node node) {
        this.node = node;
    }

    public ObservableList<TreeItem<NodeModel>> getChildren() {
        return children;
    }

    public void setChildren(ObservableList<TreeItem<NodeModel>> children) {
        this.children = children;
    }

    public NodeModel(Node node) {
        children = FXCollections.observableArrayList();
        for (Node child : node.getChildren()) {
            children.add(new TreeItem<>(new NodeModel(child)));
        }

        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        Map<String, String> attrs = node.getAttrs();
        if (attrs.size() == 0) {
            return node.getName();
        }

        if (attrs.get(MySQLConstants.AttributeName.TABLE_NAME) != null) {
            return attrs.get(MySQLConstants.AttributeName.TABLE_NAME);
        }

        if (attrs.get(MySQLConstants.AttributeName.SCHEMA_NAME) != null) {
            return attrs.get(MySQLConstants.AttributeName.SCHEMA_NAME);
        }

        return "скоро будет";
    }
}
