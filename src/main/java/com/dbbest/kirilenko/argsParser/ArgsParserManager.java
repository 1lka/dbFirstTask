package com.dbbest.kirilenko.argsParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.chainParser.*;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

public class ArgsParserManager {

    private final static Logger logger = Logger.getLogger(ArgsParserManager.class);


    private Node root;
    private String input;
    private String output;
    private String[] args;
    private Options options;
    private AbstractChainParser firstUnit;
    private CommandLine cmd;

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
            logger.error("wrong args",e);
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

    public void execute() {
        firstUnit.doWork();
    }
}
