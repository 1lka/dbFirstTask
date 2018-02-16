package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.interactionWithDB.DBElement;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Load {
    String element();

    Class<? extends Loader> parent() default Loader.class;

}
