package com.dbbest.kirilenko.model;

import com.dbbest.kirilenko.tree.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class TreeModel {
    private Node node;

    private TreeItem<TreeModel> root;

    private ObservableList<TreeItem<TreeModel>> children;

    public TreeItem<TreeModel> getRoot() {
        return root;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public ObservableList<TreeItem<TreeModel>> getChildren() {
        return children;
    }

    public TreeModel(Node node) {
        this.node = node;
        children = FXCollections.observableArrayList();
        for (Node child : node.getChildren()) {
            children.add(new TreeModel(child).getRoot());
        }
        root = new TreeItem<>(this);
        root.getChildren().addAll(children);
    }

    @Override
    public String toString() {
        return node.getName();
    }
}
