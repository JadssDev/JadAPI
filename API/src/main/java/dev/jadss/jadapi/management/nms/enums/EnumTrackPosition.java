package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

/**
 * Represents the shape of this track! F3 in minecraft client will help with this!
 */
public enum EnumTrackPosition implements NMSEnum {
    NORTH_SOUTH(),
    EAST_WEST(),
    ASCENDING_EAST(),
    ASCENDING_WEST(),
    ASCENDING_NORTH(),
    ASCENDING_SOUTH(),
    SOUTH_EAST(),
    SOUTH_WEST(),
    NORTH_WEST(),
    NORTH_EAST();

    public static final Class<?> enumTrackPositionClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyTrackPosition" : "BlockMinecartTrackAbstract$EnumTrackPosition"));

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumTrackPosition is not supported in versions below 1.7!");
        return JEnumReflector.getEnum(this.ordinal(), enumTrackPositionClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumTrackPositionClass;
    }
}
