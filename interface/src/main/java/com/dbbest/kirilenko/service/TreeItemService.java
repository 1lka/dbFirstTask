package com.dbbest.kirilenko.service;

import com.dbbest.kirilenko.model.TreeModel;
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
