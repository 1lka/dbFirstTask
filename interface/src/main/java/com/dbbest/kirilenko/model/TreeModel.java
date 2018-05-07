package com.dbbest.kirilenko.model;

import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
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

    private TreeModel parent;

    private BooleanProperty lazyLoaded = new SimpleBooleanProperty();

    private BooleanProperty fullyLoaded = new SimpleBooleanProperty();

    private BooleanProperty elementLoaded = new SimpleBooleanProperty();

    private ObservableMap<String, String> attrs = FXCollections.observableHashMap();

    private ObservableList<Map.Entry<String, String>> tableElements;

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
        tableElements = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

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
        fullyLoaded.set(Boolean.valueOf(attrs.get(Loader.FULLY_LOADED)));
        lazyLoaded.set(Boolean.valueOf(attrs.get(Loader.LAZILY_LOADED)));
        elementLoaded.set(Boolean.valueOf(attrs.get(Loader.ELEMENT_LOADED)));

        children.clear();
        for (Node n : node.getChildren()) {
            children.add(new TreeModel(n));
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
        return node.getAttrs().get("NAME");
    }
}
