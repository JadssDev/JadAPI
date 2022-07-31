package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

public enum EnumTrapdoorHalf implements NMSEnum {
    TOP,
    BOTTOM;

    public static final Class<?> enumTrapdoorHalfClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyHalf" : "BlockTrapdoor$EnumTrapdoorHalf"));

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumTrapdoorHalf is not supported in versions below 1.7");
        return JEnumReflector.getEnum(this.ordinal(), enumTrapdoorHalfClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumTrapdoorHalfClass;
    }
}
