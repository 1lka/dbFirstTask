package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;

public abstract class AbstractChainParser {

    protected ArgsParserManager manager;

    protected AbstractChainParser(ArgsParserManager manager) {
        this.manager = manager;
    }

    protected AbstractChainParser nextUnit;

    public void setNextUnit(AbstractChainParser nextUnit) {
        this.nextUnit = nextUnit;
    }

    public abstract void doWork();

    protected void next() {
        if (nextUnit != null) {
            nextUnit.doWork();
        }
    }

}
