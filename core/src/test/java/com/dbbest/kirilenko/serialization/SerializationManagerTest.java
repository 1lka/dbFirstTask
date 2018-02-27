package com.dbbest.kirilenko.serialization;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SerializationManagerTest {

    private static SerializationManager manager = new SerializationManager();


    @Test
    public void setGetStrategy() throws Exception {
        manager.setStrategy(new XMLStrategyImpl());
        XMLStrategyImpl obtained = (XMLStrategyImpl) manager.getStrategy();
        Assert.assertNotNull(obtained);
    }

    @Test
    public void setStrategyByFileName() throws Exception {
        String fName = "input.xml";
        manager.setStrategy(fName);
    }

    @Test (expected = RuntimeException.class)
    public void setStrategyByFileNameExc() throws Exception {
        String fName = "input.xmll";
        manager.setStrategy(fName);
    }

    @Test (expected = RuntimeException.class)
    public void setStrategyByFileNameExc2() throws Exception {
        String fName = "input";
        manager.setStrategy(fName);
    }

    @Test
    public void deserializeFile() throws Exception {
        String fileName = "input.xml";
        manager.setStrategy(fileName);
        manager.deserializeFile(fileName);
    }

    @Test
    public void serialize() throws Exception {
        String output = "outTest.xml";
        manager.setStrategy(output);
        manager.serialize(new Node("qwe"), output);
        new File(output).delete();
    }

}