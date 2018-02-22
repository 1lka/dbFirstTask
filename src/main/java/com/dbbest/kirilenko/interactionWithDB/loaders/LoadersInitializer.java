package com.dbbest.kirilenko.interactionWithDB.loaders;

import com.dbbest.kirilenko.interactionWithDB.DBType;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

public class LoadersInitializer {

    private Map<String, Loader> loaders = new HashMap<>();

    private ArrayList<Class> classes = new ArrayList<>();

    public Map<String, Loader> getLoaders() {
        return loaders;
    }

    public LoadersInitializer(DBType type) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        findClasses();

        String packInfo = "package-info";

        for (Class c : classes) {
            if (packInfo.equals(c.getSimpleName())) {
                Annotation ann = c.getAnnotation(PackageLoad.class);
                if (ann != null) {
                    PackageLoad pl = (PackageLoad) ann;
                    if (pl.type() == type) {
                        int lastPoint = c.getName().lastIndexOf(".");
                        String path = c.getName().substring(0, lastPoint);
                        fillLoaders(path);
                        break;
                    }
                }
            }
        }
    }

    private void fillLoaders(String path) throws IllegalAccessException, InstantiationException {
        ArrayList<Class> packClasses = new ArrayList<>();
        for (Class c : classes) {
            String className = c.getName();
            if (className.startsWith(path)) {
                packClasses.add(c);
            }
        }

        for (Class c : packClasses) {
            Annotation annotation = c.getAnnotation(Load.class);
            if (annotation != null) {
                Load load = (Load) annotation;
                String element = load.element();
                Loader loader = (Loader) c.newInstance();
                loaders.put(element, loader);
            }
        }
    }

    private void findClasses() throws ClassNotFoundException, IOException {
        String pack = this.getClass().getPackage().getName();
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
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, path));
        }
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
