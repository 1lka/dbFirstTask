package com.dbbest.kirilenko.serialization;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;

public class SerializationManager {

    private SerializationStrategy strategy;

    public SerializationStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(SerializationStrategy strategy) {
        this.strategy = strategy;
    }

    public Node deserializeFile(String input) {
        return strategy.deserialize(input);
    }

    public void serialize(Node root, String output) {
        strategy.serialize(root, output);
    }


}
