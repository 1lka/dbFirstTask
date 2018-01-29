package com.dbbest.kirilenko.serialization.strategy;

import com.dbbest.kirilenko.Tree.Node;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class XMLStrategyImplTest {


    @Test
    public void serialize() throws Exception {
        XMLStrategyImpl strategy = new XMLStrategyImpl();
        Node root = new Node("root");
    }

    @Test
    public void deserialize() throws Exception {
    }

}