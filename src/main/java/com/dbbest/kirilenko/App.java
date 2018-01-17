package com.dbbest.kirilenko;

public class App {
    public static void main(String[] args) {

        //TODO написать парсер для аргс
        String input = "test.xml"; //required
        String output = "new.xml";
        String searchMode = "W";
        String forSearch = "student"; //K,V or tagName
        String searchType = "all";    //first or all

        String forSearch2 = "id";
        String seachType2 = "002";

        SerializationManager manager = new SerializationManager();
        manager.setStrategy(new XMLStrategyImpl());

        Node root = manager.deserializeFile(input);
        manager.serialize(root, output);

        Node n = findElementW(forSearch);

    }

    private static Node findElementW(String forSearch) {
        return null;
    }

}
