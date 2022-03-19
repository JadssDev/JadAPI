package dev.jadss.jadapi.interfaces.managing;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.management.EventResult;

/**
 * Represents the Packet hooker of the server. (idk)
 */
public interface JPacketHooker {

    /**
     * Call an Event for a packet, called mostly by the inside of the API.
     * @param packet the Packet in specific.
     * @param player the Player who is on the other side of this connection, may be null.
     * @param cancellable is the event cancellable.
     * @param editable can this packet be edited?
     * @return the Result of the event as {@link EventResult}
     * @see EventResult
     */
    EventResult callPacketHooks(Object packet, JPlayer player, boolean cancellable, boolean editable);
}
