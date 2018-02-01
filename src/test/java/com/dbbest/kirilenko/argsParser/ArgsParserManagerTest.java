package com.dbbest.kirilenko.argsParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ArgsParserManagerTest {

    private final static String[] ARGS = {"-input", "input.xml", "-output", "output.xml"};
    private ArgsParserManager manager = new ArgsParserManager(ARGS);

    public ArgsParserManagerTest() throws ParseException {
    }

    @Test
    public void setGetOptions() throws Exception {

        Options optionsSet = new Options();
        manager.setOptions(optionsSet);
        Options options = manager.getOptions();
        Assert.assertEquals(optionsSet, options);
    }

    @Test
    public void setGetRoot() throws Exception {
        Node root = new Node("root");
        manager.setRoot(root);
        Node get = manager.getRoot();
        Assert.assertEquals(root,get);
    }

    @Test
    public void setGetInput() throws Exception {
        String set = "input.xml";
        manager.setInput(set);
        String get = manager.getInput();
        Assert.assertEquals(set, get);
    }

    @Test
    public void getOutput() throws Exception {
        String set = "input.xml";
        manager.setOutput(set);
        String get = manager.getOutput();
        Assert.assertEquals(set, get);
    }

    @Test
    public void setGetArgs() throws Exception {
        manager.setArgs(ARGS);
        String[] get = manager.getArgs();
        if (!Arrays.equals(ARGS, get)) {
            fail("that's impossible");
        }
    }


    @Test
    public void getCL() throws Exception {
        CommandLine cl = manager.getCL();
        Assert.assertNotNull(cl);
    }

    @Test
    public void execute() throws Exception {
        manager.execute();
    }

}