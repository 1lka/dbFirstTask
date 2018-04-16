package com.dbbest.kirilenko.service;

import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.tree.Node;
import javafx.scene.control.TreeItem;

public class TreeStateSerialization {

    public static Node convert(TreeItem<TreeModel> root, TreeItem<TreeModel> selected) {
        boolean expanded = root.isExpanded();
        Node parent = new Node(root.getValue().getNode().getAttrs().get("NAME"));
        parent.getAttrs().put("isExpanded", String.valueOf(expanded));
        if (root == selected) {
            parent.getAttrs().put("selected", "true");
        }
        root.getChildren().forEach(treeItem ->
                parent.addChild(convert(treeItem, selected))
        );
        return parent;
    }


}
