package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeepSearchChainParserTest {
    @Test
    public void doWork_1arg() throws Exception {
        String[] args = {"-input", "input.xml", "-ds","asd"};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        DeepSearchChainParser parser = new DeepSearchChainParser(manager);
        parser.doWork();
    }

    @Test
    public void doWork_2arg() throws Exception {
        String[] args = {"-input", "input.xml", "-ds","asd","123"};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        DeepSearchChainParser parser = new DeepSearchChainParser(manager);
        parser.doWork();
    }

    @Test (expected = ArgsInputException.class)
    public void doWorkExc() throws Exception {
        String[] args = {"-input", "input.xml", "-ds"};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        DeepSearchChainParser parser = new DeepSearchChainParser(manager);
        parser.doWork();
    }

}