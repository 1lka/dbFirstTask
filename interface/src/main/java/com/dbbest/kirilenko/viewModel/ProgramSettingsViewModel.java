package com.dbbest.kirilenko.viewModel;

import com.dbbest.kirilenko.service.ProgramSettings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
