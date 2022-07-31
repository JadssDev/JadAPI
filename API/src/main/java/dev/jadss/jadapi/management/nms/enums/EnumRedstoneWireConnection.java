package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

/**
 * Represents where this Redstone Wire connects to.
 */
public enum EnumRedstoneWireConnection implements NMSEnum {
    UP,
    SIDE,
    NONE;

    public static final Class<?> enumRedstoneWireConnectionClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyRedstoneSide" : "BlockRedstoneWire$EnumRedstoneWireConnection"));

    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumRedstoneWireConnection is not supported in versions below 1.7");
        return JEnumReflector.getEnum(this.ordinal(), enumRedstoneWireConnectionClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumRedstoneWireConnectionClass;
    }
}
