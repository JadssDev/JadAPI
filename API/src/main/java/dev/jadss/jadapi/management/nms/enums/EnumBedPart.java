package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

/**
 * Represents which part of a bed we are talking about!
 */
public enum EnumBedPart implements NMSEnum {
    HEAD,
    FOOT;

    public static final Class<?> enumBedPartClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyBedPart" : "BlockBed$EnumBedPart"));

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumBedPart is not supported in versions below 1.7");
        return JEnumReflector.getEnum(this.ordinal(), enumBedPartClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumBedPartClass;
    }
}
