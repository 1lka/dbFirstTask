package model;

import com.dbbest.kirilenko.interactionWithDB.constants.MySQLConstants;
import com.dbbest.kirilenko.tree.Node;

import java.util.Map;

public class NodeModel {

    private Node node;

    public NodeModel(Node node) {
        this.node = node;
    }


    @Override
    public String toString() {
        Map<String, String> attrs = node.getAttrs();
        return node.getName();
    }
}
