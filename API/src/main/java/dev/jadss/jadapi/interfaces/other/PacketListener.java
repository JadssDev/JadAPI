package dev.jadss.jadapi.interfaces.other;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Represents a <b>Listener</b> to listen for packets, the listeners <b>MAY NOT </b> change the packets and can only listen for the packets that have been <b>parsed</b>.
 * @param <T> The packet to listen for.
 */
public interface PacketListener<T extends DefinedPacket> {

    /**
     * Gets the listeners of this entity.
     * @return the listeners
     */
    List<BiConsumer<JPlayer, T>> getListeners();

    /**
     * Add a listener to when someone clicks on an entity.
     * @param listener the listener.
     */
    void addListener(BiConsumer<JPlayer, T> listener);

    /**
     * Called to check if we should call the listeners on a specific packet.
     * @param packet the Packet to test if we should execute the listeners.
     * @return if we should call the listeners.
     */
    boolean shouldCallListeners(T packet);
}
