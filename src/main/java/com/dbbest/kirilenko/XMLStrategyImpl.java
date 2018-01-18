package com.dbbest.kirilenko;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XMLStrategyImpl implements SerializationStrategy {

    @Override
    public void serialize(Node root,String fileName) {
        try {
            DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = df.newDocumentBuilder();
            Document document = builder.newDocument();

            appendNode(document, root, null);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Node deserialize(String fileName) {
        DocumentBuilderFactory df;
        DocumentBuilder builder;
        Document document;

        try {
            df = DocumentBuilderFactory.newInstance();
            builder = df.newDocumentBuilder();
            document = builder.parse(fileName);
            Element rootElement = document.getDocumentElement();

            return nodeCreate(rootElement, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Node nodeCreate(Element element, Node parent) {
        Node node = new Node();
        node.setName(element.getTagName());
        node.setParent(parent);

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
        List<Node> childrenList = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            org.w3c.dom.Node childNode = children.item(i);
            if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Node child = nodeCreate((Element) childNode, node);
                childrenList.add(child);
            }
        }
        node.setChildren(childrenList);
        return node;
    }

    private Element appendNode(Document document, Node node, Element parent) {
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
            List<Node> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Element el = appendNode(document, children.get(i), currentElement);
                System.out.println(el.getTagName());
                currentElement.appendChild(el);
            }
        } else {
            List<Node> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Element el = appendNode(document, children.get(i), currentElement);
                currentElement.appendChild(el);
            }
        }

        return currentElement;
    }
}
