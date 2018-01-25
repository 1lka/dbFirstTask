package com.dbbest.kirilenko.serialization;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerializationManager {

    private SerializationStrategy strategy;

    public SerializationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(SerializationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(String fileName) {
        Pattern pattern = Pattern.compile("(.+\\.(.+)$)");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            String fileNameSuffix = matcher.group(2);
            switch (fileNameSuffix) {
                case "xml":
                    this.strategy =  new XMLStrategyImpl();
                    return;
                default:
                    throw new RuntimeException("no such strategy");
            }
        }
        throw new RuntimeException("no such strategy");
    }

    public Node deserializeFile(String input) {
        return strategy.deserialize(input);
    }

    public void serialize(Node root, String output) {
        strategy.serialize(root, output);
    }


}
