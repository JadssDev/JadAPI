package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents the axis of a block in minecraft... basically...
 */
public enum EnumAxis implements NMSEnum {
    X,
    Y,
    Z;

    public static final Class<?> enumAxisClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + JReflection.getNMSVersion()) + ".EnumDirection$EnumAxis");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) throw new NMSException("EnumAxis is not supported in versions below 1.7");
        return JReflection.getEnum(this.ordinal(), enumAxisClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumAxisClass;
    }
}
