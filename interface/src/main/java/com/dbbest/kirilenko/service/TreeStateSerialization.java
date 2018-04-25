package com.dbbest.kirilenko.service;

import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.tree.Node;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class TreeStateSerialization {

    public static List<Node> chooseExpanded(TreeItem<TreeModel> root) {
        List<Node> expandedNodes = new ArrayList<>();
        checkTree(root,expandedNodes);
        return expandedNodes;
    }

    public static Node chooseSelected(TreeItem<TreeModel> selectedTreeModel) {
        Node node = new Node("selected");
        node.getAttrs().put("fullName", obtainFullName(selectedTreeModel.getValue().getNode(), new StringBuilder()));
        return node;
    }

    private static void checkTree(TreeItem<TreeModel> root, List<Node> expandedList) {
        boolean expanded = root.isExpanded();

        if (expanded) {
            String fullName = obtainFullName(root.getValue().getNode(), new StringBuilder());
            Node node = new Node("node");
            node.getAttrs().put("fullName", fullName);
            expandedList.add(node);
        }
        root.getChildren().forEach((treeItem) -> {
            checkTree(treeItem, expandedList);
        });


    }

    public static String obtainFullName(Node node, StringBuilder sb) {
        sb.insert(0, node.getAttrs().get("NAME"));
        if (node.getParent() != null) {
            sb.insert(0, ".");
            obtainFullName(node.getParent(), sb);
        }
        return sb.toString();
    }
}
