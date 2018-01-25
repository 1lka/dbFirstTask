package com.dbbest.kirilenko.argsParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.argsParser.chainParser.AbstractChainParser;
import com.dbbest.kirilenko.argsParser.chainParser.InputFileChainParser;
import com.dbbest.kirilenko.argsParser.chainParser.OutputFileChainParser;

public class ArgsParserManager {

    private Node root;
    private String input;
    private String output;
    private String[] args;

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

    public ArgsParserManager(String[] args) {
        this.args = args;
        AbstractChainParser unit1 = new InputFileChainParser(this);
        AbstractChainParser unit2 = new OutputFileChainParser(this);

        unit1.setNextUnit(unit2);
    }

}
