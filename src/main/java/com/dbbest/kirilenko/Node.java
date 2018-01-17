package com.dbbest.kirilenko;

import java.util.List;
import java.util.Map;

public class Node {

    private String name;
    private Map<String, String> attrs;
    private List<Node> children;
    private Node parrent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParrent() {
        return parrent;
    }

    public void setParrent(Node parrent) {
        this.parrent = parrent;
    }

    @Override
    public String toString() {
        String parentsName = null;
        if (parrent != null) {
            parentsName = parrent.getName();
        }
        return "Node name=" + name + ", attrs=" + attrs + ", children=" + children + ", parrent's name=" + parentsName;
    }
}
