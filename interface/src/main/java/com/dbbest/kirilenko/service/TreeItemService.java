package com.dbbest.kirilenko.service;

import com.dbbest.kirilenko.model.TreeModel;
import com.dbbest.kirilenko.tree.Node;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeItemService {

    public static synchronized void createTreeItems(TreeItem<TreeModel> item) {
        create(item);
    }

    private static void create(TreeItem<TreeModel> item) {
        item.getChildren().clear();
        for (TreeModel child : item.getValue().getChildren()) {
            TreeItem<TreeModel> treeItem = new TreeItem<>(child);
            create(treeItem);
            item.getChildren().add(treeItem);
        }
    }

    public synchronized void restoreExpandedItems(TreeItem<TreeModel> root, Node settingsNode) {
        List<Node> expandedNodeList = settingsNode.wideSearch("expanded").getChildren();
        List<String> expandedList = new LinkedList<>();

        for (Node node : expandedNodeList) {
            String name = node.getAttrs().get("fullName");
            expandedList.add(name);
        }
        checkForExpanded(root, expandedList);
    }

    private void checkForExpanded(TreeItem<TreeModel> root, List<String> expandedList) {
        String fullName = TreeStateSerialization.obtainFullName(root.getValue().getNode(), new StringBuilder());
        if (expandedList.contains(fullName)) {
            root.setExpanded(true);
            expandedList.remove(fullName);
        }
        root.getChildren().forEach((item) -> {
            checkForExpanded(item, expandedList);
        });
    }

    public synchronized List<TreeItem<TreeModel>> search(TreeItem<TreeModel> root, String param) {
        List<TreeItem<TreeModel>> found = new ArrayList<>();

        Queue<TreeItem<TreeModel>> nodes = new LinkedList<>();
        nodes.add(root);
        do {
            TreeItem<TreeModel> last = nodes.remove();
            String name = last.getValue().getNode().getAttrs().get("NAME").toLowerCase();
            param = param.toLowerCase();
            if (name != null && name.contains(param)) {
                found.add(last);
            }
            nodes.addAll(last.getChildren());
        } while (nodes.size() != 0);
        return found;
    }

    public synchronized void restoreSelectedItem(TreeItem<TreeModel> root, Node settingsNode, ObjectProperty<TreeItem<TreeModel>> selectedItem) {
        Node selected = settingsNode.wideSearch("selected");
        if (selected != null) {
            String fullName = selected.getAttrs().get("fullName");

            Queue<TreeItem<TreeModel>> queue = new LinkedList<>();
            queue.add(root);

            while (queue.size() != 0) {
                TreeItem<TreeModel> item = queue.remove();
                Node node = item.getValue().getNode();
                String nodeFullName = TreeStateSerialization.obtainFullName(node, new StringBuilder());
                if (nodeFullName.equals(fullName)) {
                    selectedItem.setValue(item);
                    return;
                } else {
                    queue.addAll(item.getChildren());
                }
            }
        }
    }
}
