package com.dbbest.kirilenko.serialization.strategy;

import com.dbbest.kirilenko.tree.Node;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XMLStrategyImplTest {
    private static XMLStrategyImpl strategy;
    private final static String INPUT = "input.xml";
    private final static String OUTPUT = "testOutput.xml";
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void init() throws IOException {
        strategy = new XMLStrategyImpl();
        new File(OUTPUT).createNewFile();
    }

    @AfterClass
    public static void clear() {
        new File(OUTPUT).delete();
    }

    @Test
    public void deserialize() throws Exception {
        Node n = strategy.deserialize(INPUT);
    }

    @Test(expected = Exception.class)
    public void deserializeExc() throws Exception {
        strategy.deserialize(INPUT + 1);
    }

    @Test
    public void serialize() throws Exception {
        Node root = new Node("root");
        Node child1 = new Node("first");
        Node child2 = new Node("second");
        Map<String, String> map = new HashMap<>();
        map.put("a", "A");
        map.put("b", "B");
        child1.setAttrs(map);
        root.setValue("value");
        root.addChild(child1);
        root.addChild(child2);
        strategy.serialize(root, OUTPUT);
    }


}