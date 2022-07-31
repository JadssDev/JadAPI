package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

import java.util.Arrays;

/**
 * Represents an Enum that is present in NMS.
 */
public interface NMSEnum extends NMSObject {

    /**
     * Get the enum in NMS.
     * @return the enum as {@link Object}!
     */
    Object getNMSObject();

    /**
     * Get the class in NMS this enum is responsible for parsing.
     * @return the Class in NMS.
     */
    Class<?> getNMSEnumClass();

    /**
     * Get an enum from NMS to a {@link NMSEnum}!
     * @param enumClass the {@link NMSEnum} class!
     * @param objectEnum the NMS enum value!
     * @param <A> the type of the enum!
     * @return the enum in JadAPI form!
     */
    static <A extends NMSEnum> A getEnum(Class<A> enumClass, Object objectEnum) {
        if(objectEnum == null) return null;

        if(Arrays.stream(enumClass.getDeclaredMethods()).anyMatch(m -> m.getName().equals("customParse") && m.getReturnType().equals(enumClass))) {
            return (A) JMethodReflector.executeMethod(enumClass, "customParse", null, new Object[] {objectEnum});
        } else {
            if (objectEnum instanceof Enum)
                return enumClass.getEnumConstants()[((Enum<?>) objectEnum).ordinal()];
        }
        return null;
    }
}
