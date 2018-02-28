package com.dbbest.kirilenko.interactionWithDB.loaders;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EntityLoader {
    String element();
}
