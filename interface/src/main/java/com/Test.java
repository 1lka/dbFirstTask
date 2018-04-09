package com;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Test {
    static ObjectProperty o = new SimpleObjectProperty();
    static ObjectProperty e = new SimpleObjectProperty();

    static ChangeListener listener = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            System.out.println(newValue);
        }
    };
    public static void main(String[] args) {

        o.addListener(listener);
        e.addListener(listener);
        o.setValue("asd");
        e.setValue("asdas");


        o.setValue("asdss");

    }
}
