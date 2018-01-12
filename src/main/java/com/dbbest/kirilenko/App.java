package com.dbbest.kirilenko;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(args[0]);

        Element root = document.getDocumentElement();
        String rootNodeName = root.getNodeName();
        Map<String, String> rootAttrs = new LinkedHashMap<>();
        NamedNodeMap namedNodeMap = root.getAttributes();
        for (int i = 0; i < namedNodeMap.getLength(); i++) {
            rootAttrs.put(namedNodeMap.item(i).getNodeName(), namedNodeMap.item(i).getNodeValue());
        }
        NodeList rootNodeList = root.getChildNodes();
        for (int i = 0; i < rootNodeList.getLength(); i++) {

            Node node = rootNodeList.item(i);
            System.out.println(node.getNodeName() + " "  + node.getNodeValue() + " " + node.getNodeType());
        }

        ElementNode rootNode = new ElementNode(null);
        rootNode.setName(rootNodeName);
        rootNode.setAttrs(rootAttrs);
//        System.out.println(rootNode);
    }
}
