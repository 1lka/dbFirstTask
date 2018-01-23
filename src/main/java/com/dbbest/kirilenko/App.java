package com.dbbest.kirilenko;

import com.dbbest.kirilenko.chainParser.AbstractChainParser;
import com.dbbest.kirilenko.chainParser.InputFileParser;
import com.dbbest.kirilenko.chainParser.OutputFileParser;
import com.dbbest.kirilenko.chainParser.SearchParser;

public class App {

    public static void main(String[] args) {
        getChainOfParsers().doWork(args,null);
    }

    private static AbstractChainParser getChainOfParsers() {
        AbstractChainParser c1 = new InputFileParser();
        AbstractChainParser c2 = new OutputFileParser();
        AbstractChainParser c3 = new SearchParser();

        c1.setNextUnit(c2);
        c2.setNextUnit(c3);

        return c1;
    }
}
