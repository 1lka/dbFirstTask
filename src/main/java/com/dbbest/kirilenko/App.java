package com.dbbest.kirilenko;

import com.dbbest.kirilenko.exceptions.ConsoleInputException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {

        Pattern pattern = Pattern.compile("-input");
        Matcher matcher = pattern.matcher(args[0]);
        if (!matcher.matches()) {
            throw new ConsoleInputException("wrong input");
        }

//        //TODO написать парсер для аргс
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
