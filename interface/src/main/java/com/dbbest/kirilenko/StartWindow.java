package com.dbbest.kirilenko;

import com.dbbest.kirilenko.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class StartWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties prop = new Properties();

        File propFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6) + "config.properties");
        if (!propFile.exists()) {
            propFile.createNewFile();
        }


        try (InputStream fileInputStream = new FileInputStream(propFile);
             OutputStream outputStream = new FileOutputStream(propFile)) {
            prop.load(fileInputStream);

            String projectPath = prop.getProperty("project");
            String logPath = prop.getProperty("log");

            if (projectPath == null) {
                projectPath = System.getProperty("user.home") + "\\dbBest";
                prop.setProperty("project", projectPath);
                System.out.println("proj " + projectPath);

            }

            if (logPath == null) {
                logPath = System.getProperty("user.home") + "\\dbBest\\logs";
                prop.setProperty("log", logPath);
                System.out.println("logs " + logPath);
            }
            prop.store(outputStream, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

        new MainView().show(primaryStage);


    }
}
