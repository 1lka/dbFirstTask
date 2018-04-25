package com.dbbest.kirilenko.service;

import com.dbbest.kirilenko.tree.Node;
import org.junit.Test;

public class TreeStateSerializationTest {

    @Test
    public void test() {
        Node first = new Node("f");
        first.getAttrs().put("NAME", "first");

        Node second = new Node("s");
        second.getAttrs().put("NAME", "second");

        Node third = new Node("t");
        third.getAttrs().put("NAME", "third");

        first.addChild(second);
        second.addChild(third);

        System.out.println(TreeStateSerialization.obtainFullName(third,new StringBuilder()));
    }

}