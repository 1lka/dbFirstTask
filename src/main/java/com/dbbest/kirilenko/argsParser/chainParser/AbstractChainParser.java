package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;

public abstract class AbstractChainParser {

    /**
     * The ArgsParserManager object.
     */
    protected ArgsParserManager manager;

    /**
     * The next element in chain or responsibility
     */
    protected AbstractChainParser nextUnit;

    protected AbstractChainParser(ArgsParserManager manager) {
        this.manager = manager;
    }

    public void setNextUnit(AbstractChainParser nextUnit) {
        this.nextUnit = nextUnit;
    }

    /**
     * Each element of chain executes this method
     */
    public abstract void doWork();

    /**
     * Checks if next element of chain exists.
     */
    protected void next() {
        if (nextUnit != null) {
            nextUnit.doWork();
        }
    }

}
