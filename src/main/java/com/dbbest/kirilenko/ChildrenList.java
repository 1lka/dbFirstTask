package com.dbbest.kirilenko;

import java.util.ArrayList;
import java.util.Collection;

public class ChildrenList extends ArrayList<Node> {
    @Override
    public boolean add(Node node) {
        return super.add(node);
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        return super.addAll(c);
    }
}
