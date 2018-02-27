package com.dbbest.kirilenko.Tree;

import com.dbbest.kirilenko.exceptions.NodeException;

import java.util.*;
import java.util.function.Predicate;

/**
 * Class serves for storing tree of nodes
 */
public class Node {

    /**
     * the name of the node
     */
    private String name;

    /**
     * the Map of argument values
     */
    private Map<String, String> attrs;

    /**
     * the List of child nodes
     */
    private List<Node> children;

    /**
     * parent node
     */
    private Node parent;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * creates new node object with empty ChildrenList
     *
     * @see ChildrenList
     */
    public Node() {
        this.children = new ChildrenList<>();
        this.attrs = new HashMap<>();
    }

    /**
     * creates new node object with empty ChildrenList and initialize it's name
     *
     * @param name of this node
     */
    public Node(String name) {
        this();
        this.name = name;
    }

    /**
     * Returns the node's name.
     *
     * @return the {@code String} value of node's name or null if there is no name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this node.
     *
     * @param name of this node.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the Map of node's attributes.
     *
     * @return Map of node's attributes or null if there is no attributes.
     */
    public Map<String, String> getAttrs() {
        return attrs;
    }

    /**
     * Sets the Map of attributes of this node.
     *
     * @param attrs of this node.
     */
    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    /**
     * Returns List of child nodes
     *
     * @return List of child nodes or empty List if there is no child nodes
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Sets the List of children
     *
     * @param children list of new children
     */
    public void setChildren(List<Node> children) {
        addChildren(children);
    }

    /**
     * Return parent node.
     *
     * @return Node of parent or null if this is root node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the parent for this node.
     *
     * @param parent value for this node.
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Query to see if this node has a parent
     *
     * @return boolean flag indicating existence of a parent
     */
    public boolean isParentExist() {
        return parent != null;
    }

    /**
     * Does the wide search by the element in the node.
     *
     * @param element the array of values for search.
     * @return node if found or null if there are no nodes satisfy params.
     */
    public Node wideSearch(String element) {
        Predicate<Node> predicate = (n) -> n.getName().equals(element);
        return wideSearchWithPredicate(predicate);
    }

    /**
     * Does the wide search by Key - Value in the node.
     *
     * @param key   in the Map of attributes for this node.
     * @param value in the Map of attributes for this node.
     * @return node if found or null if there are no nodes satisfy params
     */
    public Node wideSearch(String key, String value) {
        Predicate<Node> predicate = (n) -> value.equals(n.getAttrs().get(key));
        return wideSearchWithPredicate(predicate);
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

    /**
     * Does the deep search by the element in the node.
     *
     * @param element the array of values for search.
     * @return node if found or null if there are no nodes satisfy params.
     */
    public Node deepSearch(String element) {
        Predicate<Node> predicate = (n) -> n.getName().equals(element);
        return deepSearchWithPredicate(predicate);
    }

    /**
     * Does the deep search by Key - Value in the node.
     *
     * @param key   in the Map of attributes for this node.
     * @param value in the Map of attributes for this node.
     * @return node if found or null if there are no nodes satisfy params
     */
    public Node deepSearch(String key, String value) {
        Predicate<Node> predicate = (n) -> value.equals(n.attrs.get(key));
        return deepSearchWithPredicate(predicate);
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

    /**
     * Adds node in children
     *
     * @param node the new child node
     * @throws NodeException if added node already has a parent
     */
    public void addChild(Node node) {
        children.add(node);
        node.setParent(this);
    }

    /**
     * Adds collection of child nodes to children
     *
     * @param nodes new child nodes
     * @throws NodeException if collection contains a node with parent
     */
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
        return "Node name=" + name + ", attrs=" + attrs + ", children=" + children + ", parent's name=" + parentsName + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (!attrs.equals(node.attrs)) return false;
        if (!children.equals(node.children)) return false;
        if (parent != null ? !parent.getName().equals(node.parent.getName()) : node.parent != null) return false;
        return value != null ? value.equals(node.value) : node.value == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + attrs.hashCode();
        result = 31 * result + children.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
