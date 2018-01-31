package com.dbbest.kirilenko.argsParser.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.ArgsParserManager;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import java.util.Arrays;

public class WideSearchChainParser extends AbstractChainParser {

    private static final Logger logger = Logger.getLogger(WideSearchChainParser.class);

    /**
     * Instantiate a WideSearchChainParser object
     *
     * @param manager object of ArgsParserManager for this chain
     */
    public WideSearchChainParser(ArgsParserManager manager) {
        super(manager);
        Option wOption = new Option("ws", true, "wide search using");
        wOption.setArgs(2);
        wOption.setOptionalArg(true);

        Options options = manager.getOptions();
        options.addOption(wOption);
    }

    /**
     * Obtain command for wide search in tree, if command exists
     * obtain parameters for search.
     *
     * @throws ArgsInputException if no params for search.
     */
    @Override
    public void doWork() {
        CommandLine cmd = manager.getCL();

        if (cmd.hasOption("ws")) {
            String[] params = cmd.getOptionValues("ws");
            Node found;
            switch (params.length) {
                case 1:
                    found = manager.getRoot().wideSearch(params[0]);
                    logger.debug("found node: " + found);
                    break;
                case 2:
                    found = manager.getRoot().wideSearch(params[0], params[1]);
                    logger.debug("found node: " + found);
                    break;
                default:
                    logger.error("wrong params for search: " + Arrays.toString(params));
                    throw new ArgsInputException("wrong params");
            }
        }
        next();
    }
}
