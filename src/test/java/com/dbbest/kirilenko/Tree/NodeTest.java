package com.dbbest.kirilenko.Tree;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeTest {

    private Node root;

    private String rootName = "test";

    @Before
    public void initNode() {
        root = new Node(rootName);
    }


    @Test
    public void setGetName() throws Exception {
        Assert.assertEquals(rootName, root.getName());
        String newName = "test02";
        root.setName(newName);
        Assert.assertEquals(newName,root.getName());
    }

    @Test
    public void setGetAttrs() throws Exception {
        Map<String, String> attrs = new HashMap<>();
        attrs.put("first", "1");
        attrs.put("second", "2");
        root.setAttrs(attrs);
        Assert.assertEquals(attrs,root.getAttrs());
    }

    @Test
    public void setGetChildren() throws Exception {
        List<Node> children = new ChildrenList<>();
        Node firstChild = new Node("FC");
        Node secondChild = new Node("SC");
        root.setChildren(children);
        Assert.assertEquals(children,root.getChildren());
    }

    @Test
    public void setGetParent() throws Exception {
        Node child = new Node();
        child.setParent(root);
        Assert.assertEquals(child.getParent(),root);
    }

    @Test
    public void isParentExist() throws Exception {
        Assert.assertFalse(root.isParentExist());
    }

    @Test
    public void wideSearch() throws Exception {

    }

    @Test
    public void deepSearch() throws Exception {
    }

    @Test
    public void addChild() throws Exception {
    }

    @Test
    public void addChildren() throws Exception {
    }

}