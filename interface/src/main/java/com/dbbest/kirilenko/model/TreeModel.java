package com.dbbest.kirilenko.model;

import com.dbbest.kirilenko.interactionWithDB.loaders.LoaderManager;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Map;
import java.util.stream.Collectors;

public class TreeModel {

    private Node node;

    private BooleanProperty lazyLoaded = new SimpleBooleanProperty();

    private BooleanProperty fullyLoaded = new SimpleBooleanProperty();

    private BooleanProperty elementLoaded = new SimpleBooleanProperty();

    private ObservableMap<String, String> attrs = FXCollections.observableHashMap();

    //для отслеживания изменения аттрибутов
    private ObservableList<Map.Entry<String, String>> tableElements = FXCollections.observableArrayList();

    private ObservableList<TreeModel> children = FXCollections.observableArrayList();

    public ObservableList<Map.Entry<String, String>> getTableElements() {
        return tableElements;
    }

    public ObservableList<TreeModel> getChildren() {
        return children;
    }

    public BooleanProperty lazyLoadedProperty() {
        return lazyLoaded;
    }

    public BooleanProperty elementLoadedProperty() {
        return elementLoaded;
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
                MyEntry entry = new MyEntry(change.getKey(), change.getValueAdded());
                tableElements.add(entry);
            }
        });
        update();
    }

    public void update() {
        attrs.putAll(node.getAttrs());
        fullyLoaded.set(Boolean.valueOf(attrs.get(LoaderManager.FULLY_LOADED)));
        lazyLoaded.set(Boolean.valueOf(attrs.get(LoaderManager.LAZILY_LOADED)));
        elementLoaded.set(Boolean.valueOf(attrs.get(LoaderManager.ELEMENT_LOADED)));

        //todo change to flatMap
        children.addAll(node.getChildren().stream()
                .filter(n -> children.stream()
                        .noneMatch(treeModel -> treeModel.getNode().equals(n))).map(TreeModel::new).collect(Collectors.toList()));

        //todo why stream doesn't work?!!!!!!
        for (TreeModel t : children) {
            t.update();
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

    //todo create normal toString
    @Override
    public String toString() {
        if (node.getAttrs().get("NAME") == null) {
            return node.getName();
        }
        return node.getAttrs().get("NAME");
    }
}
