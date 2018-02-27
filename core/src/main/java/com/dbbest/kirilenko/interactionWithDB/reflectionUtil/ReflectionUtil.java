package com.dbbest.kirilenko.interactionWithDB.reflectionUtil;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.Load;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.printers.Print;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

public class ReflectionUtil {

    public static <T> Map<String, T> obtain(DBType type, Class<T> clazz) {
        ArrayList<Class> classes = null;
        try {
            classes = findClassesInDirectory();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("can't load classes");
        }
        String packInfo = "package-info";

        for (Class c : classes) {
            if (packInfo.equals(c.getSimpleName())) {
                Annotation ann = c.getAnnotation(PackageAnnotation.class);
                if (ann != null) {
                    PackageAnnotation annotation = (PackageAnnotation) ann;
                    if (annotation.type() == type && annotation.clazz() == clazz) {
                        int lastPoint = c.getName().lastIndexOf(".");
                        String path = c.getName().substring(0, lastPoint);
                        try {
                            return fillThings(path, classes, clazz);
                        } catch (IllegalAccessException | InstantiationException e) {
                            throw new RuntimeException("can't load classes");
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Loaders or Printers are not found");
    }

    private static <T> Map<String, T> fillThings(String path, ArrayList<Class> classes, Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        ArrayList<Class> packClasses = new ArrayList<>();
        for (Class c : classes) {
            String className = c.getName();
            if (className.startsWith(path)) {
                packClasses.add(c);
            }
        }

        Map<String, T> lp = new HashMap<>();
        if (clazz == Loader.class) {
            for (Class c : packClasses) {
                Annotation annotation = c.getAnnotation(Load.class);
                if (annotation != null) {
                    Load load = (Load) annotation;
                    String loaderName = load.element();
                    Object loader = c.newInstance();
                    lp.put(loaderName, (T) loader);
                }
            }
            return lp;
        }
        if (clazz == Printer.class) {
            for (Class c : packClasses) {
                Annotation annotation = c.getAnnotation(Print.class);
                if (annotation != null) {
                    Print print = (Print) annotation;
                    String printerName = print.element();
                    Printer printer = (Printer) c.newInstance();
                    lp.put(printerName, (T) printer);
                }
            }
            return lp;
        }
        throw new RuntimeException("Class " + clazz + " must be Loader or Printer");
    }

    private static ArrayList<Class> findClassesInDirectory() throws ClassNotFoundException, IOException {
        String pack = ReflectionUtil.class.getPackage().getName();
        String[] directories = pack.split("\\.");
        String path = directories[0];
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClassesInDirectory(directory, path));
        }
        return classes;
    }

    private static List<Class> findClassesInDirectory(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClassesInDirectory(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
