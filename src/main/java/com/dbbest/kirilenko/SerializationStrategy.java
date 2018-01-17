package com.dbbest.kirilenko;

public interface SerializationStrategy {

    void serialize(Node root, String fileName);

    Node deserialize(String fileName);

}
