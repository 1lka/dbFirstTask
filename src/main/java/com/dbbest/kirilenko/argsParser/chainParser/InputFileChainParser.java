package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;

public class InputFileChainParser extends AbstractChainParser {

    public InputFileChainParser(ArgsParserManager manager) {
        super(manager);
    }

    @Override
    void doWork() {
        String[] args = getManager().getArgs();

    }
}
