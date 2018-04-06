package com;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Test {
    private static BooleanProperty lazyLoaded = new SimpleBooleanProperty(false);

    private static BooleanProperty fullyLoaded = new SimpleBooleanProperty(false);

    public static void main(String[] args) {
        lazyLoaded.bind(fullyLoaded);

        System.out.println(lazyLoaded.get());

        fullyLoaded.set(true);

        System.out.println(lazyLoaded.get());

        lazyLoaded.set(false);
    }
}
