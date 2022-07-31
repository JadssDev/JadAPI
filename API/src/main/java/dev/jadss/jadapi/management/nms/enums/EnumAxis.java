package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

/**
 * Represents the axis of a block in minecraft... basically...
 */
public enum EnumAxis implements NMSEnum {
    X,
    Y,
    Z;

    public static final Class<?> enumAxisClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + NMS.getNMSVersion()) + ".EnumDirection$EnumAxis");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) throw new NMSException("EnumAxis is not supported in versions below 1.7");
        return JEnumReflector.getEnum(this.ordinal(), enumAxisClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumAxisClass;
    }
}
