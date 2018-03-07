package com.dbbest.kirilenko.interactionWithDB.loaders;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for marking Loaders
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityLoader {
    String element();
}
