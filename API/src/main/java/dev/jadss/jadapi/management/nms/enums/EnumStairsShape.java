package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents if the stairs is connected to something, if yes what is the form of the stair!
 */
public enum EnumStairsShape implements NMSEnum {
    STRAIGHT,
    INNER_LEFT,
    INNER_RIGHT,
    OUTER_LEFT,
    OUTER_RIGHT;

    public static final Class<?> enumStairShapeClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + JReflection.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyStairsShape" : "BlockStairs$EnumStairShape"));

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumStairsShape is not supported in versions below 1.7");
        return JReflection.getEnum(this.ordinal(), enumStairShapeClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumStairShapeClass;
    }
}
