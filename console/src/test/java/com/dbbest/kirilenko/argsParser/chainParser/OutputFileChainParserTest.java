package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import org.junit.Test;

import java.io.File;

public class OutputFileChainParserTest {
    @Test
    public void doWork() throws Exception {
        String tempFile = "temp.xml";
        String[] args = {"-input", "input.xml", "-output", tempFile};
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
        InputFileChainParser parser = new InputFileChainParser(manager);
        parser.doWork();
        new File(tempFile).delete();
    }

}