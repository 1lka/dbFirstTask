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
        this.children = children;
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
     * Does the wide search by elements in the node.
     *
     * @param elements the array of values for search
     * @return node if found or null if there are no nodes satisfy params
     * @throws NodeException if the arguments less than one or bigger than two
     */
    public Node wideSearch(String... elements) {
        Predicate<Node> predicate = obtainPredicate(elements);
        return wideSearchWithPredicate(predicate);
    }

//    public Node wideSearchElement(String element) {
//        Predicate<Node> predicate = this.getName().equals(ele);
//    }

    /**
     * Does the deep search by elements in the node.
     *
     * @param elements the array of values for search
     * @return node if found or null if there are no nodes satisfy params
     * @throws NodeException if the arguments less than one or bigger than two
     */
    public Node deepSearch(String... elements) {
        Predicate<Node> predicate = obtainPredicate(elements);
        return deepSearchWithPredicate(predicate);
    }

    private Predicate<Node> obtainPredicate(String[] elements) {
        int i = elements.length;
        switch (i) {
            case 1:
                return (n) -> n.getName().equals(elements[0]);
            case 2:
                return (n) -> elements[1].equals(n.getAttrs().get(elements[0]));
            default:
                throw new NodeException("search params should be in the range 1 - 2");
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
        return "Node name=" + name + ", attrs=" + attrs + ", children=" + children + ", parent's name=" + parentsName;
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
