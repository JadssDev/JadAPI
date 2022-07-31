package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

/**
 * What hand this player is on
 * <p><b>Note:</b> non existent in versions less then 1.8 </p>
 */
public enum EnumHand implements NMSEnum {
    MAIN_HAND((byte) 0),
    OFF_HAND((byte) 1);

    public static final Class<?> enumHandClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world" : "server." + NMS.getNMSVersion()) + ".EnumHand");

    private byte ordinal;

    EnumHand(byte ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) throw new NMSException("EnumHand is not supported in versions below 1.8");
        return enumHandClass.getEnumConstants()[ordinal];
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumHandClass;
    }
}
