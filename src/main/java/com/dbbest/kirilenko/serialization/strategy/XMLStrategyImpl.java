package com.dbbest.kirilenko.serialization.strategy;

import com.dbbest.kirilenko.exceptions.SerializationException;
import org.apache.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dbbest.kirilenko.Tree.Node;

public class XMLStrategyImpl implements SerializationStrategy {

    private static final Logger logger = Logger.getLogger(XMLStrategyImpl.class);

    @Override
    public void serialize(Node root, String fileName) throws SerializationException {

        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = df.newDocumentBuilder();

            Document document = builder.newDocument();

            appendNode(document, root);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            logger.error("problems with XML serialization occurred", e);
            throw new SerializationException(e);
        }
    }

    @Override
    public Node deserialize(String fileName) throws SerializationException {
        DocumentBuilderFactory df;
        DocumentBuilder builder;
        Document document;

        try {
            df = DocumentBuilderFactory.newInstance();
            builder = df.newDocumentBuilder();
            document = builder.parse(fileName);
            Element rootElement = document.getDocumentElement();
            return nodeCreate(rootElement);
        } catch (Exception e) {
            logger.error("problems with deserialization", e);
            throw new SerializationException(e);
        }
    }

    private Node nodeCreate(Element element) {
        Node node = new Node(element.getTagName());

        NamedNodeMap attrs = element.getAttributes();
        if (attrs.getLength() > 0) {
            Map<String, String> attributes = new LinkedHashMap<>();
            for (int i = 0; i < attrs.getLength(); i++) {
                org.w3c.dom.Node attribute = attrs.item(i);
                String name = attribute.getNodeName();
                String value = attribute.getNodeValue();
                attributes.put(name, value);
            }
            node.setAttrs(attributes);
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            org.w3c.dom.Node childNode = children.item(i);
            if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Node child = nodeCreate((Element) childNode);
                node.addChild(child);
            }
        }
        return node;
    }

    private Element appendNode(Document document, Node node) {
        Element currentElement = document.createElement(node.getName());

        Map<String, String> attrs = node.getAttrs();
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            Attr attr = document.createAttribute(name);
            attr.setValue(value);
            currentElement.setAttributeNode(attr);
        }
        if (node.getParent() == null) {
            document.appendChild(currentElement);
            appendChildren(document, node, currentElement);
        } else {
            appendChildren(document, node, currentElement);
        }
        return currentElement;
    }

    private void appendChildren(Document document, Node node, Element currentElement) {
        List<Node> children = node.getChildren();
        for (Node child : children) {
            Element el = appendNode(document, child);
            currentElement.appendChild(el);
        }
    }
}
