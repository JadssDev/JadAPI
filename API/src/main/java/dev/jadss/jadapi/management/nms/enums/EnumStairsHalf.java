package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

/**
 * Represents where the most amount of wood is on a stair, example of bottom.
 * <p>X #</p>
 * <p>X Y</p>
 * <p>and top respectively.</p>
 * <p>X X</p>
 * <p>X Y</p>
 * <p><b>Y represents air, X represents one of the 4 parts of wood in 2 dimensional!</b></p>
 */
public enum EnumStairsHalf implements NMSEnum {
    TOP,
    BOTTOM;

    public static final Class<?> enumStairHalfClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyHalf" : "BlockStairs$EnumHalf"));

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumStairsHalf is not supported in versions below 1.7!");
        return JEnumReflector.getEnum(this.ordinal(), enumStairHalfClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumStairHalfClass;
    }
}
