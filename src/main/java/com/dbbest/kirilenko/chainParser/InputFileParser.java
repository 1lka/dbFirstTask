package com.dbbest.kirilenko.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import com.dbbest.kirilenko.serialization.SerializationManager;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;

public class InputFileParser extends AbstractChainParser {

    private static final String INPUT_PATTERN = "-input|-i";

    @Override
    public void doWork(String[] args, Node node) {
        String inputFileName = getFileName(INPUT_PATTERN, args);
        if (inputFileName == null) {
            throw new ArgsInputException("input file name required");
        }
        SerializationManager manager = new SerializationManager();
        SerializationStrategy strategy = obtainStrategy(inputFileName);
        manager.setStrategy(strategy);
        Node root = manager.deserializeFile(inputFileName);
        System.out.println("Root node : " + root);
        if (nextUnit != null) {
            nextUnit.doWork(args,root);
        }
    }
}
