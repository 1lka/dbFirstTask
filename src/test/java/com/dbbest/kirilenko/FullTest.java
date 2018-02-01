package com.dbbest.kirilenko;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.NodeException;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FullTest {

    @Test
    public void readWrite() throws Exception {
        String source = "input.xml";
        String target = "output.xml";
        XMLStrategyImpl strategy = new XMLStrategyImpl();
        Node readRoot = strategy.deserialize(source);
        strategy.serialize(readRoot, target);
        BufferedReader sourceReader = new BufferedReader(new FileReader(source));
        BufferedReader targetReader = new BufferedReader(new FileReader(target));
        String s;
        String t;
        int i = 0;
        int j = 0;
        while (sourceReader.ready() && targetReader.ready()) {
            s = sourceReader.readLine().trim();
            t = targetReader.readLine().trim();
            if (s.equals(t)) {
                j++;
            }
            i++;
        }
        Assert.assertEquals(i, j);
    }
}