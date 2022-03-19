package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents which part of a door it is.
 */
public enum EnumDoorHalf implements NMSEnum {
    UPPER,
    LOWER;

    public static final Class<?> enumDoorHalfClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + JReflection.getNMSVersion()) + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyDoubleBlockHalf" : "EnumDoorHalf"));

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumDoorHalf is not supported in versions below 1.7");
        return JReflection.getEnum(this.ordinal(), enumDoorHalfClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumDoorHalfClass;
    }
}
