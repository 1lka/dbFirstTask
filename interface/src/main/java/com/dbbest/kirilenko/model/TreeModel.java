package com.dbbest.kirilenko.model;

import com.dbbest.kirilenko.tree.Node;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Map;

public class TreeModel {

    private Node node;

    private ObservableMap<String, String> attrs = FXCollections.observableHashMap();

    //для отслеживания изменения аттрибутов
    private ObservableList<Map.Entry<String, String>> tableElements = FXCollections.observableArrayList();


    private ObservableList<TreeModel> children = FXCollections.observableArrayList();

    public ObservableList<Map.Entry<String, String>> getTableElements() {
        return tableElements;
    }

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
        //List<TreeModel> list = node.getChildren().stream().map(TreeModel::new).collect(Collectors.toList());
        for (Node child : node.getChildren()) {
            children.add(new TreeModel(child));
        }


        attrs.addListener((MapChangeListener<? super String, ? super String>) change -> {
            if (change.wasAdded()) {
                MyEntry entry = new MyEntry(change.getKey(), change.getValueAdded());
                tableElements.add(entry);
            }
        });

    }

    private class MyEntry implements Map.Entry<String, String> {

        private final String key;
        private String value;

        public MyEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            String old = this.value;
            this.value = value;
            return old;
        }
    }

    @Override
    public String toString() {
        return node.getName();
    }
}
