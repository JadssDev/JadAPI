package dev.jadss.jadapi.utils.reflection.reflectors;

import dev.jadss.jadapi.utils.reflection.Reflection;

import java.util.Arrays;

public class JEnumReflector implements Reflection {

    public static Enum<?> getEnum(String name, Class<?> cls) {
        return Arrays.stream(getEnum(cls)).filter(e -> e.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Enum<?> getEnum(int ordinal, Class<?> cls) {
        return Arrays.stream(getEnum(cls)).filter(e -> e.ordinal() == ordinal).findFirst().orElse(null);
    }

    public static Enum<?>[] getEnum(Class<?> cls) {
        return (Enum<?>[]) cls.getEnumConstants();
    }
}
