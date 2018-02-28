package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.argsParser.exceptions.ArgsInputException;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.serialization.SerializationManager;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

public class InputFileChainParser extends AbstractChainParser {

    private final static Logger logger = Logger.getLogger(InputFileChainParser.class);

    /**
     * Instantiate a InputFileChainParser object
     *
     * @param manager object of ArgsParserManager for this chain
     */
    public InputFileChainParser(ArgsParserManager manager) {
        super(manager);
        Options options = manager.getOptions();
        Option inputOption = new Option("input", true, "input file name");
        options.addOption(inputOption);
    }

    /**
     * Obtains input file name and creates tree of Node in memory
     *
     * @throws ArgsInputException if there is no file name
     */
    @Override
    public void doWork() {
        CommandLine cmd = manager.getCL();
        Node rootNode;

        SerializationManager serManager = new SerializationManager();
        String inputFileName = cmd.getOptionValue("input");
        serManager.setStrategy(inputFileName);
        try {
            rootNode = serManager.deserializeFile(inputFileName);
        } catch (SerializationException e) {
            logger.warn("problems with deserialization", e);
            throw new RuntimeException(e);
        }

        manager.setRoot(rootNode);
        next();
    }
}
