package com.dbbest.kirilenko.chainParser;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractChainParser {

    protected AbstractChainParser nextUnit;

    protected Node root;

    public void setNextUnit(AbstractChainParser nextUnit) {
        this.nextUnit = nextUnit;
    }

    public abstract void doWork(String[] args, Node root);

    protected static String getFileName(String pattern, String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (Pattern.matches(pattern, args[i])) {
                return args[i + 1];
            }
        }
        return null;
    }

    protected static SerializationStrategy obtainStrategy(String fileName) {
        Pattern pattern = Pattern.compile("(.+\\.(.+)$)");
        Matcher matcher = pattern.matcher(fileName);
        String fileNameSuffix = null;
        if (matcher.find()) {
            fileNameSuffix = matcher.group(2);
            switch (fileNameSuffix) {
                case "xml":
                    return new XMLStrategyImpl();
                default:
                    throw new RuntimeException("no such strategy");
            }
        }
        throw new RuntimeException("no such strategy");
    }
}
