package com.dbbest.kirilenko;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import com.dbbest.kirilenko.serialization.SerializationManager;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    private static final String INPUT_PATTERN = "-input|-i";
    private static final String OUTPUT_PATTERN = "-output|-out|-o";

    private static SerializationStrategy obtainStrategy(String fileName) {
        Pattern pattern = Pattern.compile("(.+\\.(.+)$)");
        Matcher matcher = pattern.matcher(fileName);
        String fileNameSuffix = null;
        if (matcher.find()) {
            fileNameSuffix = matcher.group(2);
        }
        switch (fileNameSuffix) {
            case "xml":
                return new XMLStrategyImpl();
            default:
                throw new RuntimeException("no such strategy");
        }
    }

    private static String getFileName(String pattern, String[] args) {
        String fileName = null;
        for (int i = 0; i < args.length; i++) {
            if (Pattern.matches(pattern, args[i])) {
                fileName = args[i + 1];
                break;
            }
        }
        return fileName;
    }

    public static void main(String[] args) {

        SerializationManager manager = new SerializationManager();

        String inputFileName = getFileName(INPUT_PATTERN, args);
        manager.setStrategy(obtainStrategy(inputFileName));
        Node rootNode = manager.deserializeFile(inputFileName);
        System.out.println(rootNode);

        String outputFileName = getFileName(OUTPUT_PATTERN, args);
        if (outputFileName != null) {
            manager.setStrategy(obtainStrategy(outputFileName));
            manager.serialize(rootNode, outputFileName);
            System.out.println("serialization successful");
        }


        Pattern deepSearchPattern = Pattern.compile("-ds|-d|-dsearch");
        Pattern wideSearchPattern = Pattern.compile("-ws|-w|-wsearch");

        //TODO create parser for args
//        String input = "test.xml"; //required
//        String output = "new.xml";
//        String searchMode = "W";
//        String forSearch = "gender"; //K,V or tagName
//
//        String key = "id";
//        String value = "001";
//
//        SerializationManager manager = new SerializationManager();
//        manager.setStrategy(new XMLStrategyImpl());
//
//        Node root = manager.deserializeFile(input);
//        manager.serialize(root, output);
//
//        Node deepSearchElement = root.deepSearch(forSearch);
//        Node deepSearchKV = root.deepSearch(key, value);
//        System.out.println(deepSearchElement);
//        System.out.println(deepSearchKV);
//
//        Node wideSearchElement = root.wideSearch(forSearch);
//        Node wideSearchKV = root.wideSearch(key, value);
//        System.out.println(wideSearchElement);
//        System.out.println(wideSearchKV);
//
//        System.out.println(deepSearchElement == wideSearchElement);
//        System.out.println(deepSearchKV == wideSearchKV);

    }
}
