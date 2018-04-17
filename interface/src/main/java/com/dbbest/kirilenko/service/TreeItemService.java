package com.dbbest.kirilenko.service;

import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeItemService {

    public void createTreeItems(TreeItem<TreeModel> item) {
        item.getChildren().clear();
        for (TreeModel child : item.getValue().getChildren()) {
            TreeItem<TreeModel> treeItem = new TreeItem<>(child);
            createTreeItems(treeItem);
            item.getChildren().add(treeItem);
        }
    }

    public void restoreTreeState(TreeItem<TreeModel> item, ObjectProperty<TreeItem<TreeModel>> selected, Node settingsNode) {
        boolean isExpanded = Boolean.valueOf(settingsNode.getAttrs().get("isExpanded"));
        boolean isSelected = Boolean.valueOf(settingsNode.getAttrs().get("selected"));

        item.setExpanded(isExpanded);
        if (isSelected) {
            selected.setValue(item);
        }

        for (int i = 0; i < item.getValue().getChildren().size(); i++) {
            TreeItem<TreeModel> treeItem = new TreeItem<>(item.getValue().getChildren().get(i));
            restoreTreeState(treeItem,selected,settingsNode.getChildren().get(i));
            item.getChildren().add(treeItem);
        }
    }

    public List<TreeItem<TreeModel>> search(TreeItem<TreeModel> root, String param) {
        List<TreeItem<TreeModel>> found = new ArrayList<>();

        Queue<TreeItem<TreeModel>> nodes = new LinkedList<>();
        nodes.add(root);
        do {
            TreeItem<TreeModel> last = nodes.remove();
            String name = last.getValue().getNode().getAttrs().get("NAME");
            if (name != null && name.contains(param)) {
                found.add(last);
            }
            nodes.addAll(last.getChildren());
        } while (nodes.size() != 0);
        return found;
    }
}
