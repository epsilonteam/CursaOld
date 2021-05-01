package club.eridani.cursa.utils;

public class MathUtil {
    public static int clamp(int val, final int min, final int max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }
}
