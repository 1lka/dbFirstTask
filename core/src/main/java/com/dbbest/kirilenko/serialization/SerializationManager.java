package com.dbbest.kirilenko.serialization;

import com.dbbest.kirilenko.tree.Node;
import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerializationManager {

    /**
     * Instance of SerializationStrategy.
     */
    private SerializationStrategy strategy;

    public SerializationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(SerializationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Obtain strategy by file name.
     *
     * @param fileName file name.
     * @throws RuntimeException if there is no strategy for such file extension.
     */
    public void setStrategy(String fileName) {
        Pattern pattern = Pattern.compile("(.+\\.(.+)$)");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            String fileNameSuffix = matcher.group(2);
            switch (fileNameSuffix) {
                case "xml":
                    this.strategy = new XMLStrategyImpl();
                    return;
                default:
                    throw new RuntimeException("no such strategy");
            }
        }
        throw new RuntimeException("no such strategy");
    }

    /**
     * Deserialize file in the tree.
     *
     * @param input location of the file for deserialization.
     * @return root node of the tree.
     * @throws SerializationException if occurs problems with serialization.
     */
    public Node deserializeFile(String input) throws SerializationException {
        return strategy.deserialize(input);
    }

    /**
     * Serialize the tree in the file.
     *
     * @param root   node of the tree.
     * @param output file location.
     * @throws SerializationException if occurs problems with serialization.
     */
    public void serialize(Node root, String output) throws SerializationException {
        strategy.serialize(root, output);
    }


}
