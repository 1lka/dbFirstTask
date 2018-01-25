package com.dbbest.kirilenko.serialization.strategy;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.SerializationExeption;

public interface SerializationStrategy {

    void serialize(Node root, String fileName);

    Node deserialize(String fileName) throws SerializationExeption;

}
