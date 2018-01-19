package com.dbbest.kirilenko.Tree;

import java.util.ArrayList;
import java.util.Collection;

public class ChildrenList<E> extends ArrayList<Node> {

    private Node parent;

    public ChildrenList() {
        super();
    }

    public ChildrenList(Node parent) {
        this.parent = parent;
    }

    @Override
    public boolean add(Node node) {
        return node.getParent() == parent && super.add(node);
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        for (Node node : c) {
            if (node.getParent() != parent) {
                return false;
            }
        }
        return super.addAll(c);
    }


}
