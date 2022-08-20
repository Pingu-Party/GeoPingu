package de.pinguparty.geopingu.worker.domain.formula.utils;

public class Utils {
    public static <T> T instantiateClass(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
