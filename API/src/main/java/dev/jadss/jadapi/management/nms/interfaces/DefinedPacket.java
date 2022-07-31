package dev.jadss.jadapi.management.nms.interfaces;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

/**
 * Represents a packet that can be parsed or built in NMS!
 */
public abstract class DefinedPacket implements Copyable<DefinedPacket>, NMSPacket {

    public static final Class<?> PACKET = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol" : "server." + NMS.getNMSVersion()) + ".Packet");

    public DefinedPacket() {}

    /**
     * Check if we can parse an object.
     * @param object the Object to test.
     * @return the result if yes as boolean.
     */
    public abstract boolean canParse(Object object);

    /**
     * Parse an Object to this Packet.
     * @param packet the Packet as Object.
     */
    public abstract void parse(Object packet);

    /**
     * Build this packet using the information on this parsed packet or built packet.
     * @return the Packet as NMS Packet.
     */
    public abstract Object build();

    /**
     * Get the NMS' Packet Class this parser parses!
     * @return the Class.
     */
    public abstract Class<?> getParsingClass();
}
