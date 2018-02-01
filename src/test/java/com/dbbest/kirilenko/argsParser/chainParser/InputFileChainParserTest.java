package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputFileChainParserTest {

    @Test
    public void doWork() throws Exception {
        String[] args = {"-input", "input.xml", "-ws", "asd"};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        InputFileChainParser parser = new InputFileChainParser(manager);
        parser.doWork();
    }

    @Test (expected = RuntimeException.class)
    public void doWorkExc() throws Exception {
        String[] args = {"-input", "iiiiinput.xml", "-ws", "asd"};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        InputFileChainParser parser = new InputFileChainParser(manager);
        parser.doWork();
    }

}