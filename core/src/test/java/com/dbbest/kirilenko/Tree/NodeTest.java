package com.dbbest.kirilenko.Tree;

import com.dbbest.kirilenko.exceptions.NodeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeTest {

    @Test
    public void setGetName() throws Exception {
        String name = "node";
        Node n = new Node();
        n.setName(name);
        String nName = n.getName();
        Assert.assertEquals(name, nName);
    }

    @Test
    public void setGetAttrs() throws Exception {
        Map<String, String> attrs = new HashMap<>();
        attrs.put("first", "1");
        attrs.put("second", "2");
        Node n = new Node();
        n.setAttrs(attrs);
        Map<String, String> nAttrs = n.getAttrs();
        Assert.assertEquals(attrs, nAttrs);
    }

    @Test
    public void setGetChildren() throws Exception {
        Node n = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        List<Node> children = new ChildrenList<>();
        children.add(child1);
        children.add(child2);
        n.setChildren(children);
        List<Node> nChildren = n.getChildren();
        Assert.assertEquals(children, nChildren);
    }

    @Test
    public void setGetValue() throws Exception {
        String value = "123";
        Node n = new Node();
        n.setValue(value);
        String get = n.getValue();
        Assert.assertEquals(value,get);
    }

    @Test
    public void getParent() throws Exception {
        Node n = new Node("node");
        Node child = new Node();
        child.setParent(n);
        Node parent = child.getParent();
        Assert.assertEquals(n, parent);
    }

    @Test
    public void isParentExist() throws Exception {
        Node n = new Node();
        boolean parent = n.isParentExist();
        Assert.assertFalse(parent);
    }

    @Test
    public void deepWideS_1arg() throws Exception {
        Node root = new Node("root");
        Node fChild = new Node("firstChild");
        Node sChild = new Node("secondChild");

        root.addChild(fChild);
        root.addChild(sChild);

        Map<String, String> attrs = new HashMap<>();
        attrs.put("first", "1");
        attrs.put("second", "2");

        sChild.setAttrs(attrs);

        Node wideSearchedNode = root.wideSearch("secondChild");
        Node deepSearchedNode = root.deepSearch("secondChild");

        Assert.assertEquals(sChild, wideSearchedNode);
        Assert.assertEquals(sChild, deepSearchedNode);
    }

    @Test
    public void deepWideS_2arg() throws Exception {
        Node root = new Node("root");
        Node fChild = new Node("firstChild");
        Node sChild = new Node("secondChild");

        root.addChild(fChild);
        root.addChild(sChild);

        Map<String, String> attrs = new HashMap<>();
        attrs.put("first", "1");
        attrs.put("second", "2");

        sChild.setAttrs(attrs);

        Node wideSearchedNode = root.wideSearch("second", "2");
        Node deepSearchedNode = root.deepSearch("second", "2");

        Assert.assertEquals(sChild, wideSearchedNode);
        Assert.assertEquals(sChild, deepSearchedNode);
    }


    @Test
    public void deepWideS_retNull() throws Exception {
        Node root = new Node("root");
        Node fChild = new Node("firstChild");
        Node sChild = new Node("secondChild");

        root.addChild(fChild);
        root.addChild(sChild);

        Assert.assertNull(root.wideSearch("thirdChild"));
        Assert.assertNull(root.deepSearch("thirdChild"));
    }

    @Test
    public void addChildren() throws Exception {
        Node n = new Node();
        List<Node> children = new ArrayList<>();
        children.add(new Node("1"));
        children.add(new Node("2"));
        children.add(new Node("3"));
        n.addChildren(children);
    }

    @Test (expected = NodeException.class)
    public void addChildrenExc() throws Exception {
        Node n = new Node();
        List<Node> children = new ArrayList<>();
        Node p = new Node();
        Node fours = new Node();
        fours.setParent(p);
        children.add(new Node("1"));
        children.add(new Node("2"));
        children.add(new Node("3"));
        children.add(fours);
        n.addChildren(children);
    }

}