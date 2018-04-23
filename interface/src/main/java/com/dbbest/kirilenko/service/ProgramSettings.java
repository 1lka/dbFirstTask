package com.dbbest.kirilenko.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.Properties;

public class ProgramSettings {

    private static final Logger logger = Logger.getLogger(ProgramSettings.class);

    private static Properties prop = new Properties();

    private static File config;

    public static Properties getProp() {
        return prop;
    }

    public static void initialize() throws IOException {

        File dbDestFolder = new File(System.getProperty("user.home") + "/DbBest");
        if (!dbDestFolder.exists()) {
            dbDestFolder.mkdir();
        }

        config = new File(dbDestFolder, "config.properties");
        if (!config.exists()) {
            config.createNewFile();
        }

        File logsFolder = new File(dbDestFolder, "logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdir();
        }

        try (FileReader reader = new FileReader(config)) {
            prop.load(reader);
            String projectPath = prop.getProperty("project");
            String logPath = prop.getProperty("log");

            if (projectPath == null) {
                projectPath = System.getProperty("user.home") + "\\dbBest" + System.lineSeparator();
                prop.setProperty("project", projectPath);
            }
            if (logPath == null) {
                logPath = System.getProperty("user.home") + "\\dbBest\\logs";
                prop.setProperty("log", logPath);
            }

            updateProperties();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateProperties() throws IOException {
        try (FileWriter writer = new FileWriter(config)) {
            prop.store(writer,null);
        }

        Properties log4jprops = new Properties();
        try {
            InputStream configStream = ProgramSettings.class.getResourceAsStream("/log4j.properties");
            log4jprops.load(configStream);
            configStream.close();
        } catch (IOException e) {
            System.out.println("Error: Cannot load configuration file");
        }
        log4jprops.setProperty("log4j.appender.FILE.File", prop.getProperty("log") + "\\out.log");
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(log4jprops);
    }
}
