package club.eridani.cursa.utils;

import org.reflections.Reflections;

import java.util.Set;

public class ClassUtil {
    public static <T> Set<Class<? extends T>> findClasses(String pack, Class<T> subType) {
        Reflections reflections = new Reflections(pack);
        return reflections.getSubTypesOf(subType);
    }
}
