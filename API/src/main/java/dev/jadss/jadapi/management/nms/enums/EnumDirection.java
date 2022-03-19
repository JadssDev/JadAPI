package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents the direction of a block.
 */
public enum EnumDirection implements NMSEnum {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static final Class<?> enumDirectionClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + JReflection.getNMSVersion()) + ".EnumDirection");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumDirection is not supported by versions below 1.7");
        return enumDirectionClass.getEnumConstants()[this.ordinal()];
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumDirectionClass;
    }
}
