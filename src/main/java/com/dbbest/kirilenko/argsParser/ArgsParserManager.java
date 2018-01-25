package com.dbbest.kirilenko.argsParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.chainParser.*;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import org.apache.commons.cli.*;

public class ArgsParserManager {

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

        inputUnit.setNextUnit(outputUnit);
        outputUnit.setNextUnit(wSearchUnit);
        wSearchUnit.setNextUnit(dSearchUnit);

        this.firstUnit = inputUnit;
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

    public void execute() {
        firstUnit.doWork();
    }

    public CommandLine getCL() {
        if (cmd != null) {
            return cmd;
        }
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            throw new ArgsInputException("wrong args", e);
        }
    }
}
