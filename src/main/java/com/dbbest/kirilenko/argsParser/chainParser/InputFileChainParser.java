package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import com.dbbest.kirilenko.serialization.SerializationManager;
import org.apache.commons.cli.*;

public class InputFileChainParser extends AbstractChainParser {

    public InputFileChainParser(ArgsParserManager manager) {
        super(manager);
        Options options = manager.getOptions();
        Option inputOption = new Option("input", true, "input file name");
        options.addOption(inputOption);
    }

    @Override
    public void doWork() {
        CommandLine cmd = manager.getCL();
        Node rootNode;
        if (cmd.hasOption("input")) {
            SerializationManager manager = new SerializationManager();
            String inputFileName = cmd.getOptionValue("input");
            manager.setStrategy(inputFileName);
            rootNode = manager.deserializeFile(inputFileName);
        } else {
            throw new ArgsInputException("input file required");
        }
        manager.setRoot(rootNode);
        if (nextUnit != null) {
            nextUnit.doWork();
        }
    }
}
