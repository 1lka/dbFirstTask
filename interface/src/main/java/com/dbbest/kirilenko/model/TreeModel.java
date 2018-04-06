package com.dbbest.kirilenko.model;

import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Map;

public class TreeModel {

    private Node node;

    private TreeModel parent;

    private BooleanProperty lazyLoaded = new SimpleBooleanProperty(false);

    private BooleanProperty fullyLoaded = new SimpleBooleanProperty(false);

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

    public TreeModel getParent() {
        return parent;
    }

    private void setParent(TreeModel parent) {
        this.parent = parent;
    }

    public boolean isLazyLoaded() {
        return lazyLoaded.get();
    }

    public BooleanProperty lazyLoadedProperty() {
        return lazyLoaded;
    }

    public boolean isFullyLoaded() {
        return fullyLoaded.get();
    }

    public BooleanProperty fullyLoadedProperty() {
        return fullyLoaded;
    }

    public Node getNode() {
        return node;
    }

    public TreeModel(Node node) {
        this.node = node;
        attrs.addListener((MapChangeListener<? super String, ? super String>) change -> {
            if (change.wasAdded()) {
                System.out.println("attribute " + change.getKey() + " " + change.getValueAdded());
                MyEntry entry = new MyEntry(change.getKey(), change.getValueAdded());
                tableElements.add(entry);
            }
        });
        if (getParent() != null) {
            this.lazyLoaded.bind(getParent().lazyLoadedProperty());
            this.fullyLoaded.bind(getParent().fullyLoadedProperty());
        }
        this.lazyLoaded.bind(this.fullyLoadedProperty());

        update();
    }

    public void update() {
        attrs.putAll(node.getAttrs());
//todo change loop for stream
//        List<TreeModel> list = node.getChildren().stream().map(TreeModel::new).collect(Collectors.toList());
        children.clear();
        for (Node child : node.getChildren()) {
            TreeModel treeModel = new TreeModel(child);
            treeModel.setParent(this);
            children.add(treeModel);
        }
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
