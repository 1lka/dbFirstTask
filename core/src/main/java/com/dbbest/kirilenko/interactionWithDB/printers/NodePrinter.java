package com.dbbest.kirilenko.interactionWithDB.printers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NodePrinter {
    String element();
}
