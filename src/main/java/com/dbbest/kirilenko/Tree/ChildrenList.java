package com.dbbest.kirilenko.Tree;

import com.dbbest.kirilenko.exceptions.NodeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChildrenList<E> extends ArrayList<Node> {


    /**
     * Add Node in ChildrenList.
     *
     * @param node new child.
     * @return true if node added successfully.
     * @throws NodeException if node already has a parent.
     */
    @Override
    public boolean add(Node node) {
        if (node.isParentExist()) {
            throw new NodeException("Node" + " " + node + " already has a parent");
        } else {
            return super.add(node);
        }
    }

    /**
     * Add collection of children in ChildrenList.
     * @param c collection of nodes.
     * @return true if collection added successfully.
     */
    @Override
    public boolean addAll(Collection<? extends Node> c) {
        for (Node node : c) {
            if (node.isParentExist()) {
                throw new NodeException("Node" + " " + node + " already has a parent");
            }
        }
        return super.addAll(c);
    }
}
