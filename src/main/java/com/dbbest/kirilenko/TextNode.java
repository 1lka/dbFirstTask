package com.dbbest.kirilenko;

public class TextNode extends Node {

    private String value;

    public TextNode(Node parent) {
        super(parent);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TextNode{" +
                "value='" + value + '\'' +
                "parent= " + getParent() +
                '}';
    }
}
