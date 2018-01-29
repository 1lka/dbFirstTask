package com.dbbest.kirilenko.argsParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.chainParser.*;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

public class ArgsParserManager {

    private final static Logger logger = Logger.getLogger(ArgsParserManager.class);

    /**
     * The root node of tree
     */
    private Node root;

    /**
     * Input file location for deserialization.
     */
    private String input;

    /**
     * Output file location for deserialization.
     */
    private String output;

    /**
     * Program arguments
     */
    private String[] args;

    /**
     * Contain options for search.
     */
    private Options options;

    /**
     * First element of chain of responsibility
     */
    private AbstractChainParser firstUnit;

    /**
     * Represents list of arguments parsed against a options descriptor.
     */
    private CommandLine cmd;

    /**
     * Instantiate elements of the chain and create the chain for parsing args.
     *
     * @param args Program arguments.
     * @throws ArgsInputException if there is  invalid args.
     */
    public ArgsParserManager(String[] args) {
        this.args = args;
        options = new Options();

        AbstractChainParser inputUnit = new InputFileChainParser(this);
        AbstractChainParser outputUnit = new OutputFileChainParser(this);
        AbstractChainParser wSearchUnit = new WideSearchChainParser(this);
        AbstractChainParser dSearchUnit = new DeepSearchChainParser(this);

        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("wrong args", e);
            throw new ArgsInputException("wrong args", e);
        }

        inputUnit.setNextUnit(outputUnit);
        outputUnit.setNextUnit(wSearchUnit);
        wSearchUnit.setNextUnit(dSearchUnit);

        this.firstUnit = inputUnit;

        logger.debug("chain initialized correctly");
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public AbstractChainParser getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(AbstractChainParser firstUnit) {
        this.firstUnit = firstUnit;
    }

    public CommandLine getCL() {
        return cmd;
    }

    /**
     * Start execution of the chain.
     */
    public void execute() {
        firstUnit.doWork();
    }
}
