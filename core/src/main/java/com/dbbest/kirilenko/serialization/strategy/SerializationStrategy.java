package com.dbbest.kirilenko.serialization.strategy;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.SerializationException;

/**
 * Strategy interface defining an action
 */
public interface SerializationStrategy {

    /**
     * Serialize tree of nodes in file.
     *
     * @param root     element of the tree.
     * @param fileName file location for serialization.
     * @throws SerializationException if occurs problems with serialization.
     */
    void serialize(Node root, String fileName) throws SerializationException;

    /**
     * Deserialize file in the tree of Nodes.
     *
     * @param fileName file location for deserialization.
     * @return root Node.
     * @throws SerializationException if occurs problems with serialization.
     */
    Node deserialize(String fileName) throws SerializationException;

}
