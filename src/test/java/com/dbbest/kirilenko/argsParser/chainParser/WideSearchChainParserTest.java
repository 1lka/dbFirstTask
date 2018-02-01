package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.junit.Test;

import static org.junit.Assert.*;

public class WideSearchChainParserTest {
    @Test
    public void doWork_1arg() throws Exception {
        String[] args = {"-input", "input.xml", "-ws","asd"};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        WideSearchChainParser parser = new WideSearchChainParser(manager);
        parser.doWork();
    }

    @Test
    public void doWork_2arg() throws Exception {
        String[] args = {"-input", "input.xml", "-ws","asd","123"};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        WideSearchChainParser parser = new WideSearchChainParser(manager);
        parser.doWork();
    }

    @Test (expected = ArgsInputException.class)
    public void doWorkExc() throws Exception {
        String[] args = {"-input", "input.xml", "-ws"};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        WideSearchChainParser parser = new WideSearchChainParser(manager);

        parser.doWork();
    }


}