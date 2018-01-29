package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
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
        if (cmd.hasOption("input")) {
            SerializationManager manager = new SerializationManager();
            String inputFileName = cmd.getOptionValue("input");
            manager.setStrategy(inputFileName);
            try {
                rootNode = manager.deserializeFile(inputFileName);
            } catch (SerializationException e) {
                logger.warn("problems with deserialization", e);
                //todo System.exit(0) ??
                throw new RuntimeException();
            }
        } else {
            logger.error("input file required");
            throw new ArgsInputException("input file required");
        }
        manager.setRoot(rootNode);
        next();
    }
}
