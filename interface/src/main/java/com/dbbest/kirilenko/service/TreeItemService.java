package com.dbbest.kirilenko.service;

import com.dbbest.kirilenko.model.TreeModel;
import javafx.scene.control.TreeItem;

public class TreeItemService {

    public void createTreeItems(TreeItem<TreeModel> item) {

        item.getChildren().clear();
        for (TreeModel child : item.getValue().getChildren()) {
            TreeItem<TreeModel> treeItem = new TreeItem<>(child);
            createTreeItems(treeItem);
            item.getChildren().add(treeItem);
        }
    }
}
