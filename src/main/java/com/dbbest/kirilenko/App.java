package com.dbbest.kirilenko;

import com.dbbest.kirilenko.Tree.Node;
import com.dbbest.kirilenko.exceptions.ArgsInputException;
import com.dbbest.kirilenko.serialization.SerializationManager;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {

//        SerializationManager manager = new SerializationManager();
//
//        Pattern deepSearchPattern = Pattern.compile("-ds|-d|-dsearch");
//        Pattern wideSearchPattern = Pattern.compile("-ws|-w|-wsearch");
//
//        String inputFileName = null;
//        for (int i = 0; i < args.length; i++) {
//            if (Pattern.matches("-input|-i", args[i])) {
//                inputFileName = args[i + 1];
//                break;
//            }
//        }
//        String inputFileNameSuffix = null;
//        Pattern pattern = Pattern.compile("(.+\\.(.+)$)");
//        Matcher matcher = pattern.matcher(inputFileName);
//        if (matcher.find()) {
//            inputFileNameSuffix = matcher.group(2);
//        }
//
//        switch (inputFileNameSuffix) {
//            case "xml":
//                manager.setStrategy(new XMLStrategyImpl());
//                break;
//        }
//
//        Node rootNode = manager.deserializeFile(inputFileName);
//
//        System.out.println(rootNode);
//
//        String outputFileName = null;
//        for (int i = 0; i < args.length; i++) {
//            if (Pattern.matches("-output|-out|-o", args[i])) {
//                outputFileName = args[i + 1];
//                break;
//            }
//        }
//        if (outputFileName != null) {
//
//            String outputFileNameSuffix = null;
//            Pattern pattern2 = Pattern.compile("(.+\\.(.+)$)");
//            Matcher matcher2 = pattern2.matcher(outputFileName);
//            System.out.println(outputFileName);
//            if (matcher2.find()) {
//                outputFileNameSuffix = matcher.group(2);
//            }
//            switch (outputFileNameSuffix) {
//                case "xml":
//                    manager.setStrategy(new XMLStrategyImpl());
//                    break;
//            }
//            manager.serialize(rootNode, outputFileName);
//
//        }




        //TODO написать парсер для аргс
        String input = "test.xml"; //required
        String output = "new.xml";
        String searchMode = "W";
        String forSearch = "gender"; //K,V or tagName

        String key = "id";
        String value = "001";

        SerializationManager manager = new SerializationManager();
        manager.setStrategy(new XMLStrategyImpl());

        Node root = manager.deserializeFile(input);
        manager.serialize(root, output);

        Node deepSearchElement = root.deepSearch(forSearch);
        Node deepSearchKV = root.deepSearch(key, value);
        System.out.println(deepSearchElement);
        System.out.println(deepSearchKV);

        Node wideSearchElement = root.wideSearch(forSearch);
        Node wideSearchKV = root.wideSearch(key, value);
        System.out.println(wideSearchElement);
        System.out.println(wideSearchKV);

        System.out.println(deepSearchElement == wideSearchElement);
        System.out.println(deepSearchKV == wideSearchKV);

    }
}
