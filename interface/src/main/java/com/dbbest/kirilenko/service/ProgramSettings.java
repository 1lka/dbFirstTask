package com.dbbest.kirilenko.service;

import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.serialization.strategy.SerializationStrategy;
import com.dbbest.kirilenko.serialization.strategy.XMLStrategyImpl;
import com.dbbest.kirilenko.tree.Node;
import org.apache.log4j.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Properties;

public class ProgramSettings {

    private static final Logger logger = Logger.getLogger(ProgramSettings.class);

    private static Properties prop = new Properties();

    private static File config;

    private static File cashConnect;

    private static Node cash;

    public static Node getCash() {
        return cash;
    }

    public static Properties getProp() {
        return prop;
    }

    public static void initialize() throws IOException, SerializationException {

        File dbDestFolder = new File(System.getProperty("user.home") + "/DbBest");
        if (!dbDestFolder.exists()) {
            dbDestFolder.mkdir();
        }

        cashConnect = new File(dbDestFolder, "cash.xml");
        SerializationStrategy strategy = new XMLStrategyImpl();
        if (cashConnect.exists()) {
            cash = strategy.deserialize(cashConnect.getAbsolutePath());
        } else {
            cashConnect.createNewFile();
            cash = new Node("cash");
            strategy.serialize(cash, cashConnect.getAbsolutePath());
        }

        config = new File(dbDestFolder, "config.properties");
        if (!config.exists()) {
            config.createNewFile();
        }

        File logsFolder = new File(dbDestFolder, "logs");
        File logsFile = new File(logsFolder, "log.txt");
        if (!logsFolder.exists()) {
            logsFolder.mkdir();
            logsFile.createNewFile();
        }

        try (FileReader reader = new FileReader(config)) {
            prop.load(reader);
            String projectPath = prop.getProperty("project");
            String logPath = prop.getProperty("log");
            String programDefault = prop.getProperty("root");

            if (programDefault == null) {
                programDefault = dbDestFolder.getAbsolutePath();
                programDefault = decode(programDefault).trim();
                prop.setProperty("root", programDefault);
            }

            if (projectPath == null) {
                projectPath = programDefault;
                prop.setProperty("project", projectPath);
            }
            if (logPath == null) {
                logPath = decode(logsFile.getAbsolutePath());
                prop.setProperty("log", logPath);
            }

            updateProperties();


        } catch (IOException e) {
            logger.error(e);
        }
    }

    private static String decode(String s) {
        Charset cset = Charset.forName("UTF-8");
        ByteBuffer buf = cset.encode(s);
        byte[] b = buf.array();
        return new String(b);
    }

    public static void updateProperties() throws IOException {
        try (FileWriter writer = new FileWriter(config)) {
            prop.store(writer, null);
        }
        updateLog4jConfiguration(prop.getProperty("log"));
    }


    private static void updateLog4jConfiguration(String logFile) {
        Properties props = new Properties();
        try {
            InputStream configStream = ProgramSettings.class.getResourceAsStream("/log4j.properties");
            props.load(configStream);
            configStream.close();
        } catch (IOException e) {
            System.out.println("Errornot laod configuration file ");
        }
        props.setProperty("log4j.appender.FILE.File", logFile);
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }


    public static void storeConnect(String url,String port, String db, String login) {
        Node node = new Node("connect");
        node.setParent(cash);
        node.getAttrs().put("url", url);
        node.getAttrs().put("port", port);
        node.getAttrs().put("db", db);
        node.getAttrs().put("login", login);
        SerializationStrategy strategy = new XMLStrategyImpl();

        if (cash.getChildren().size() > 9) {
            cash.getChildren().remove(0);
        }

        if (!cash.getChildren().contains(node)) {
            node.setParent(null);
            cash.addChild(node);
            try {
                strategy.serialize(cash, cashConnect.getAbsolutePath());
            } catch (SerializationException ignored) {
            }
        }

        try {
            cash = strategy.deserialize(cashConnect.getAbsolutePath());
        } catch (SerializationException ignored) {

        }
    }
}
