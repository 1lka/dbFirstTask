package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.serialization.SerializationManager;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

public class OutputFileChainParser extends AbstractChainParser {

    private final static Logger logger = Logger.getLogger(OutputFileChainParser.class);

    /**
     * Instantiate a OutputFileChainParser object
     *
     * @param manager object of ArgsParserManager for this chain
     */
    public OutputFileChainParser(ArgsParserManager manager) {
        super(manager);
        Options options = manager.getOptions();
        Option outputOption = new Option("output", true, "input file name");
        options.addOption(outputOption);
    }

    /**
     * Obtains output file name and serialize three in this file
     */
    @Override
    public void doWork() {
        CommandLine cmd = manager.getCL();

        if (cmd.hasOption("output")) {
            SerializationManager serializationManager = new SerializationManager();
            String outputFileName = cmd.getOptionValue("output");
            serializationManager.setStrategy(outputFileName);
            try {
                serializationManager.serialize(manager.getRoot(), outputFileName);
                logger.debug("serialization completed successfully");
            } catch (SerializationException e) {
                logger.error("error with serialization occurred " + e);
            }
        }
        next();
    }
}
