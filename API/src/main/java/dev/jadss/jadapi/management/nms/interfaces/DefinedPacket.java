package dev.jadss.jadapi.management.nms.interfaces;

import dev.jadss.jadapi.interfaces.Copyable;

/**
 * Represents a packet that can be parsed or built in NMS!
 */
public abstract class DefinedPacket implements Copyable<DefinedPacket>, NMSPacket {

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
