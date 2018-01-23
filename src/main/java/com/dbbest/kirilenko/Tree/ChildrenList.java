package com.dbbest.kirilenko.Tree;

import com.dbbest.kirilenko.exceptions.NodeExeption;

import java.util.ArrayList;
import java.util.Collection;

public class ChildrenList<E> extends ArrayList<Node> {

    @Override
    public boolean add(Node node) {
        if (node.isParentExist()) {
            throw new NodeExeption("Node" + " " + node + " already has a parent");
        } else {
            return super.add(node);
        }
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        for (Node node : c) {
            if (node.isParentExist()) {
                throw new NodeExeption("Node" + " " + node + " already has a parent");
            }
        }
        return super.addAll(c);
    }
}
