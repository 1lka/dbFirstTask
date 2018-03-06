package com.dbbest.kirilenko.interactionWithDB.reflectionUtil;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import com.dbbest.kirilenko.interactionWithDB.loaders.EntityLoader;
import com.dbbest.kirilenko.interactionWithDB.loaders.Loader;
import com.dbbest.kirilenko.interactionWithDB.printers.NodePrinter;
import com.dbbest.kirilenko.interactionWithDB.printers.Printer;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class ReflectionUtil {

    private static ArrayList<Class> classes;

    private final static String PACKAGE_INFO = "package-info";

    static {
        try {
            classes = findAllClasses();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("problems with class loading");
        }
    }

    public static Map obtain(DBType type, Class clazz) {
        for (Class c : classes) {
            if (PACKAGE_INFO.equals(c.getSimpleName())) {
                Annotation ann = c.getAnnotation(PackageAnnotation.class);
                if (ann != null) {
                    PackageAnnotation annotation = (PackageAnnotation) ann;
                    if (annotation.type() == type && annotation.clazz() == clazz) {
                        int lastPoint = c.getName().lastIndexOf(".");
                        String path = c.getName().substring(0, lastPoint);
                        try {
                            return fillMap(path, clazz);
                        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                            throw new RuntimeException("can't load classes");
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Loaders or Printers are not found");
    }

    private static Map fillMap(String path, Class clazz)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        List<Class> packClasses = new ArrayList<>();
        for (Class c : classes) {
            String className = c.getName();
            if (className.startsWith(path)) {
                packClasses.add(c);
            }
        }
        Map map = new HashMap();

        for (Class c : packClasses) {
            Annotation a = c.getAnnotation(clazz);
            if (a != null) {
                Class<?> type = a.annotationType();
                Method m = type.getMethod("element");
                String key = (String) m.invoke(a);
                Object value = c.newInstance();
                map.put(key, value);
            }
        }
        return map;
    }

    private static ArrayList<Class> findAllClasses() throws ClassNotFoundException, IOException {
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
