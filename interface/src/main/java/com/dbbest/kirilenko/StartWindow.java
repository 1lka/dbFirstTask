package com.dbbest.kirilenko;

import com.dbbest.kirilenko.exceptions.SerializationException;
import com.dbbest.kirilenko.service.ProgramSettings;
import com.dbbest.kirilenko.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.*;

public class StartWindow extends Application {

    private static final Logger logger = Logger.getLogger(StartWindow.class);

    public static void main(String[] args) throws IOException, SerializationException {
        ProgramSettings.initialize();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new MainView().show(primaryStage);
        logger.debug("program start");
    }
}
