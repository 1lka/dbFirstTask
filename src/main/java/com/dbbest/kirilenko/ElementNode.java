package com.dbbest.kirilenko;

import java.util.List;
import java.util.Map;

public class ElementNode extends Node {

    private String name;
    private Map<String, String> attrs;
    private List<Node> children;

    public ElementNode(Node parent) {
        super(parent);
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

    @Override
    public String toString() {
        return "ElementNode{" +
                "name='" + name + '\'' +
                ", attrs=" + attrs +
                ", children=" + children +
                ", parent=" + getParent() +
                '}';
    }
}
