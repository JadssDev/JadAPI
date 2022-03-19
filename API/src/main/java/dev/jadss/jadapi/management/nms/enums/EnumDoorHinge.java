package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents where the hinge of the door is.
 */
public enum EnumDoorHinge implements NMSEnum{
    LEFT,
    RIGHT;

    public static final Class<?> enumDoorHingeClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + JReflection.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyDoorHinge" : "BlockDoor$EnumDoorHinge"));

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumDoorHinge is not supported in versions below 1.7");
        return JReflection.getEnum(this.ordinal(), enumDoorHingeClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumDoorHingeClass;
    }
}
