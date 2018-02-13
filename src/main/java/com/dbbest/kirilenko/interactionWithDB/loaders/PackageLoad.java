package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.interactionWithDB.DBType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface PackageLoad {
    DBType type();
}
