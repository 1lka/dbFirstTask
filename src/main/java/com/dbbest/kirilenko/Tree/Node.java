package com.dbbest.kirilenko.Tree;

import java.util.*;
import java.util.function.Predicate;

public class Node {

    private String name;
    private Map<String, String> attrs;
    private List<Node> children;
    private Node parent;

    public Node() {
        this.children = new ChildrenList<>(this);
    }

    public Node(String name) {
        this.name = name;
        this.children = new ChildrenList<>(this);
    }

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
        Predicate<Node> predicate = (n) -> n.getName().equals(element);
        return searchWithPredicate(predicate);
    }

    public Node wideSearch(String key, String value) {
        Predicate<Node> predicate = (n) -> value.equals(n.getAttrs().get(key));
        return searchWithPredicate(predicate);
    }

    private Node searchWithPredicate(Predicate<Node> nodePredicate) {
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(this);
        do {
            Node last = nodes.remove();
            if (nodePredicate.test(last)) {
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

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    //todo
    public void addChildren(Collection<Node> children) {
        this.children.addAll(children);
    }

    @Override
    public String toString() {
        String parentsName = null;
        if (parent != null) {
            parentsName = parent.getName();
        }
        return "Node name=" + name + ", attrs=" + attrs + ", children=" + children + ", parrent's name=" + parentsName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (attrs != null ? !attrs.equals(node.attrs) : node.attrs != null) return false;
        if (children != null ? !children.equals(node.children) : node.children != null) return false;
        return parent != null ? parent.equals(node.parent) : node.parent == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (attrs != null ? attrs.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
