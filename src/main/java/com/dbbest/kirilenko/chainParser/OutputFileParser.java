package com.dbbest.kirilenko.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.serialization.SerializationManager;

public class OutputFileParser extends AbstractChainParser {

    private static final String OUTPUT_PATTERN = "-output|-out|-o";

    @Override
    public void doWork(String[] args, Node node) {
        String outputFileName = getFileName(OUTPUT_PATTERN, args);
        if (outputFileName != null) {

            SerializationManager manager = new SerializationManager();

            manager.setStrategy(obtainStrategy(outputFileName));
            manager.serialize(node, outputFileName);

            System.out.println("serialization completed successfully");
        }
        if (nextUnit != null) {
            nextUnit.doWork(args, node);
        }
    }
}
