package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

/**
 * Represents the many Protocols the player can send when the player joins the server on the handshake packet.
 */
public enum EnumProtocol implements NMSEnum {
    HANDSHAKING((byte) -1, (byte) 0),
    PLAY((byte) 0, (byte) 1),
    STATUS((byte) 1, (byte) 2),
    LOGIN((byte) 2, (byte) 3);

    public static final Class<?> enumProtocolClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network" : "server." + NMS.getNMSVersion()) + ".EnumProtocol");

    private final byte id;
    private final byte ordinal;

    EnumProtocol(byte id, byte ordinal) {
        this.id = id;
        this.ordinal = ordinal;
    }

    public byte getId() { return id; }
    public byte getOrdinal() { return ordinal; }

    public Object getNMSObject() {
        return JEnumReflector.getEnum(ordinal, enumProtocolClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumProtocolClass;
    }
}
