package com.dbbest.kirilenko;

import java.util.*;

public class Node {

    private String name;
    private Map<String, String> attrs;
    private List<Node> children;
    private Node parent;

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

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node wideSearch(String element) {
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(this);

        do {
            Node last = nodes.remove();
            if (last.getName().equals(element)) {
                return last;
            } else nodes.addAll(last.getChildren());
        } while (nodes.size() != 0);

        return null;
    }

    public Node wideSearch(String key, String value) {
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(this);

        do {
            Node last = nodes.remove();
            if (value.equals(last.getAttrs().get(key))) {
                return last;
            } else nodes.addAll(last.getChildren());
        } while (nodes.size() != 0);
        return null;
    }

    public Node deepSearch(String element) {
        if (name.equals(element)) {
            return this;
        } else {
            for (Node child : children) {
                Node last = child.deepSearch(element);
                if (last != null) {
                    return last;
                }
            }
        }
        return null;
    }

    public Node deepSearch(String key, String value) {
        if (Objects.equals(attrs.get(key), value)) {
            return this;
        } else {
            for (Node child : children) {
                Node last = child.deepSearch(key, value);
                if (last != null) {
                    return last;
                }
            }
        }
        return null;
    }

    //todo
    public void addChild(Node child) {

    }
    //todo
    public void addChildren(Collection<Node> ch) {

    }

    @Override
    public String toString() {
        String parentsName = null;
        if (parent != null) {
            parentsName = parent.getName();
        }
        return "Node name=" + name + ", attrs=" + attrs + ", children=" + children + ", parrent's name=" + parentsName;
    }
}
