package com.dbbest.kirilenko;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import org.apache.log4j.Logger;

public class App {

    private final static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        logger.debug("application started");

        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();

        logger.debug("application finished");
    }
}
