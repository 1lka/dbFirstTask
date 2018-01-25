package com.dbbest.kirilenko.argsParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.chainParser.AbstractChainParser;
import com.dbbest.kirilenko.argsParser.chainParser.InputFileChainParser;
import com.dbbest.kirilenko.argsParser.chainParser.OutputFileChainParser;
import com.dbbest.kirilenko.argsParser.chainParser.SearchChainParser;
import org.apache.commons.cli.Options;

public class ArgsParserManager {

    private Node root;
    private String input;
    private String output;
    private String[] args;
    private Options options;
    private AbstractChainParser firstUnit;

    public ArgsParserManager(String[] args) {
        this.args = args;
        options = new Options();

        AbstractChainParser inputUnit = new InputFileChainParser(this);
        AbstractChainParser outputUnit = new OutputFileChainParser(this);
        AbstractChainParser searchUnit = new SearchChainParser(this);

        inputUnit.setNextUnit(outputUnit);
        outputUnit.setNextUnit(searchUnit);

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
}
