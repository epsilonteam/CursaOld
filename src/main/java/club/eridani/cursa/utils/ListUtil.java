package club.eridani.cursa.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Kotlin would be better than java
 */
public class ListUtil {
    @SafeVarargs
    public static <T> List<T> listOf(T... elements){
        return Arrays.asList(elements);
    }
}
