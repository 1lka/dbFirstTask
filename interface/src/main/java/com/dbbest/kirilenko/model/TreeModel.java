package com.dbbest.kirilenko.model;

import com.dbbest.kirilenko.tree.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Map;

public class TreeModel {

    private Node node;

    private ObservableMap<String, String> attrs = FXCollections.observableHashMap();

    //для отслеживания изменения аттрибутов
    private ObservableList<Map.Entry<String, String>> tableElements = FXCollections.observableArrayList();


    private ObservableList<TreeModel> children = FXCollections.observableArrayList();

    public ObservableMap<String, String> getAttrs() {
        return attrs;
    }

    public ObservableList<TreeModel> getChildren() {
        return children;
    }

    public Node getNode() {
        return node;
    }

    public TreeModel(Node node) {
        this.node = node;
        attrs.putAll(node.getAttrs());
        //todo change loop for stream
        for (Node child : node.getChildren()) {
            children.add(new TreeModel(child));
        }
//        List<TreeModel> list = node.getChildren().stream().map(TreeModel::new).collect(Collectors.toList());

    }

    @Override
    public String toString() {
        return node.getName();
    }
}
