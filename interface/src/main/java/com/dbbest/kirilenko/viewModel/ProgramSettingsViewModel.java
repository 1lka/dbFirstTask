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
        return new File(properties.getProperty("log").substring(0,properties.getProperty("log").lastIndexOf("\\")));
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
}
