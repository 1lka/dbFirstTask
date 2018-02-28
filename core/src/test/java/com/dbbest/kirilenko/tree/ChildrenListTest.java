package com.dbbest.kirilenko.tree;

import com.dbbest.kirilenko.exceptions.NodeException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ChildrenListTest {

    private static List<Node> testList;
    private static Node node;

    @BeforeClass
    public static void init() {
        testList = new ChildrenList<>();
        node = new Node();
    }

    @Test
    public void add() throws Exception {
        testList.add(node);
    }

    @Test(expected = NodeException.class)
    public void addExc() throws Exception {
        Node wrong = new Node();
        wrong.setParent(node);
        testList.add(wrong);
    }

    @Test
    public void addAll() throws Exception {
        Node f = new Node();
        Node s = new Node();
        List<Node> list = new ArrayList<>();
        list.add(f);
        list.add(s);
        testList.addAll(list);
    }

    @Test (expected = NodeException.class)
    public void addAllExc() throws Exception {
        Node f = new Node();
        Node s = new Node();
        s.setParent(new Node());
        List<Node> list = new ArrayList<>();
        list.add(f);
        list.add(s);
        testList.addAll(list);
    }

}