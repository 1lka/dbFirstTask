package com.dbbest.kirilenko;

import com.dbbest.kirilenko.argsParser.ArgsParserManager;

public class App {

    public static void main(String[] args) {
        ArgsParserManager manager = new ArgsParserManager(args);
        manager.execute();
    }
}
