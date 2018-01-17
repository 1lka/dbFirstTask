package com.dbbest.kirilenko;

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
