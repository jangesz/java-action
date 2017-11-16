package org.tic.java8.reflection;

import java.lang.reflect.Type;

public class ReflectionUtil {

    private static final String TYPE_NAME_PREFIX = "class ";

    public static String getClassName(Type type) {
        if (null == type) return "";
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }
}
