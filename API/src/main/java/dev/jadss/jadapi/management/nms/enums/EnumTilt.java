package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

public enum EnumTilt implements NMSEnum {
    NONE,
    UNSTABLE,
    PARTIAL,
    FULL;

    //Don't have to tell you this is present only in 1.17+
    public static final Class<?> tiltEnumClass = JClassReflector.getClass("net.minecraft.world.level.block.state.properties.Tilt");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16))
            throw new NMSException("EnumTilt is not supported in versions below 1.16");
        return JEnumReflector.getEnum(this.ordinal(), tiltEnumClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return tiltEnumClass;
    }
}
