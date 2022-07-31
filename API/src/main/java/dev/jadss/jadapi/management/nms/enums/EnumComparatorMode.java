package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

/**
 * Represents the mode of a comparator.
 */
public enum EnumComparatorMode implements NMSEnum {
    COMPARE,
    SUBTRACT;

    public static final Class<?> enumComparatorModeClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + "." + (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12) ? "BlockRedstoneComparator$EnumComparatorMode" : "BlockPropertyComparatorMode"));

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumComparatorMode is not supported in versions below 1.7");
        return JEnumReflector.getEnum(this.ordinal(), enumComparatorModeClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumComparatorModeClass;
    }
}
