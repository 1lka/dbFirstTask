package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.service.ProgramSettings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ProgramSettingsViewModel {

    private StringProperty log = new SimpleStringProperty();

    private StringProperty project = new SimpleStringProperty();

    public StringProperty logProperty() {
        return log;
    }

    public StringProperty projectProperty() {
        return project;
    }

    public File obtainLogFileName() {
        Properties properties = ProgramSettings.getProp();
        File file = new File(properties.getProperty("log").substring(0, properties.getProperty("log").lastIndexOf("\\")));
        if (!file.exists()) {
            file = new File(properties.getProperty("root"));
        }
        return file;
    }

    public ProgramSettingsViewModel() {
        Properties properties = ProgramSettings.getProp();
        log.set(properties.getProperty("log"));
        project.set(properties.getProperty("project"));
    }

    public void saveChanges() throws IOException {
        Properties properties = ProgramSettings.getProp();
        properties.setProperty("project", project.getValue());
        properties.setProperty("log", log.getValue());
        ProgramSettings.updateProperties();
    }

    public File getDefaultFolder() {
        Properties properties = ProgramSettings.getProp();
        String name = properties.getProperty("project");
        File file = new File(name);
        if (!file.exists()) {
            name = properties.getProperty("root");
            return new File(name);
        }
        return file;
    }
}
