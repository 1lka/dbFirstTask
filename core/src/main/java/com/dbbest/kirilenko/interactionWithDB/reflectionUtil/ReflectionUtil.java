package com.dbbest.kirilenko.interactionWithDB.reflectionUtil;

import com.dbbest.kirilenko.interactionWithDB.DBType;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class ReflectionUtil {

    private static Logger logger = Logger.getLogger(ReflectionUtil.class);

    private static ArrayList<Class> classes;

    private final static String PACKAGE_INFO = "package-info";

    private static final String ROOT_PACKAGE_NAME = "com/dbbest";

    private static final String FILE_PREFIX = "file:";

    private static final String JAR_PATH_SEPARATOR = "!\\";


    static {
        try {
            classes = findAllClasses();
        } catch (ClassNotFoundException | IOException e) {
            logger.error(e);
        }
    }

    public static Map obtainAnnotatedClasses(DBType type, Class clazz) {
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
                String[] keys = (String[]) m.invoke(a);
                Object value = c.newInstance();
                for (String k : keys) {
                    map.put(k, value);
                }
            }
        }
        return map;
    }

    private static ArrayList<Class> findAllClasses() throws ClassNotFoundException, IOException {
        logger.debug("starting to search all classes");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        Enumeration<URL> resources = classLoader.getResources(ROOT_PACKAGE_NAME);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            String path2 = resources.nextElement().getPath();
            File directory = new File(URLDecoder.decode(path2, "UTF-8"));
            dirs.add(directory);
        }

        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            if (isJar(directory)) {
                classes.addAll(findClassesInJar(directory, ROOT_PACKAGE_NAME));
            } else {
                classes.addAll(findClassesInDirectory(directory, ROOT_PACKAGE_NAME));
            }
        }
        return classes;
    }

    private static boolean isJar(File file) {
        String directory = file.getPath();
        boolean f = directory.startsWith(FILE_PREFIX);
        boolean s = directory.contains(JAR_PATH_SEPARATOR);
        return directory.startsWith(FILE_PREFIX) && directory.contains(JAR_PATH_SEPARATOR);
    }

    private static List<Class> findClassesInJar(File resourcePath, String rootPackage) throws IOException, ClassNotFoundException {
        List<Class> classes = new ArrayList<>();

        String path = resourcePath.getPath();
        String[] split = path.split("!\\\\");
        URL jarUrl = new URL(split[0]);
        JarInputStream zip = new JarInputStream(jarUrl.openStream());

        ZipEntry zipEntry;
        while ((zipEntry = zip.getNextEntry()) != null) {
            String entryName = zipEntry.getName();

            logger.info(entryName);
            if (!entryName.endsWith(".class") || !entryName.startsWith(rootPackage) || entryName.contains("$")) {
                continue;
            }
            String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
            classes.add(Class.forName(className));
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
                classes.addAll(findClassesInDirectory(file, packageName + "/" + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '/' + file.getName().substring(0, file.getName().length() - 6);
                className = className.replace("/", ".");
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }
}
