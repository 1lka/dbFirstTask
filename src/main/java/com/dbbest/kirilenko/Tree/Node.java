package com.dbbest.kirilenko.Tree;

import com.dbbest.kirilenko.exceptions.NodeExeption;

import java.util.*;
import java.util.function.Predicate;

public class Node {

    private String name;
    private Map<String, String> attrs;
    private List<Node> children;
    private Node parent;

    public Node() {
        this.children = new ChildrenList<>();
    }

    public Node(String name) {
        this.name = name;
        this.children = new ChildrenList<>();
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

    public boolean isParentExist() {
        return parent != null;
    }

    public Node wideSearch(String... elements) {
        Predicate<Node> predicate = obtainPredicate(elements);
        return wideSearchWithPredicate(predicate);
    }

    public Node deepSearch(String... elements) {
        Predicate<Node> predicate = obtainPredicate(elements);
        return deepSearchWithPredicate(predicate);
    }

    private Predicate<Node> obtainPredicate(String[] elements) {
        int i = elements.length;
        switch (i) {
            case 1:
                return  (n) -> n.getName().equals(elements[0]);
            case 2:
                return  (n) -> elements[1].equals(n.getAttrs().get(elements[0]));
            default:
                throw new NodeExeption("search params should be in the range 1 - 2");
        }
    }

    private Node wideSearchWithPredicate(Predicate<Node> nodePredicate) {
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

    private Node deepSearchWithPredicate(Predicate<Node> nodePredicate) {
        if (nodePredicate.test(this)) {
            return this;
        } else {
            for (Node child : children) {
                Node last = child.deepSearchWithPredicate(nodePredicate);
                if (last != null) {
                    return last;
                }
            }
        }
        return null;
    }

    public void addChild(Node node) {
        children.add(node);
        node.setParent(this);
    }

    public void addChildren(Collection<Node> nodes) {
        children.addAll(nodes);
        for (Node node : nodes) {
            node.setParent(this);
        }
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
