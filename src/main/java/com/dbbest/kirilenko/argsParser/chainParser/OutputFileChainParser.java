package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import com.dbbest.kirilenko.serialization.SerializationManager;
import org.apache.commons.cli.*;

public class OutputFileChainParser extends AbstractChainParser{

    public OutputFileChainParser(ArgsParserManager manager) {
        super(manager);
        Options options = manager.getOptions();
        Option outputOption = new Option("output", true, "input file name");
        options.addOption(outputOption);

    }

    @Override
    public void doWork() {
        CommandLine cmd = manager.getCL();

        if (cmd.hasOption("output")) {
            SerializationManager serializationManager = new SerializationManager();
            String outputFileName = cmd.getOptionValue("output");
            System.out.println(outputFileName);
            serializationManager.setStrategy(outputFileName);
            serializationManager.serialize(manager.getRoot(), outputFileName);
            System.out.println("success");
        }

        if (nextUnit != null) {
            nextUnit.doWork();
        }
    }
}
