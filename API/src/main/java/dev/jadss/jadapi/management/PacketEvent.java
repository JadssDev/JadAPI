package dev.jadss.jadapi.management;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;

import java.util.concurrent.CompletableFuture;

public class PacketEvent {

    //Info
    private final boolean async;
    private Object packet;
    private JPlayer player;

    //CANCEL SH*T
    private boolean cancelled = false;

    //Checkers!
    private boolean hasBeenEdited = false;

    //FLAGS
    private final boolean editable;
    private final boolean cancellable;

    private CompletableFuture<EventResult> result = new CompletableFuture<>();

    public PacketEvent(boolean async, Object packet, JPlayer player, boolean cancellable, boolean editable) {
        this.async = async;
        this.packet = packet;
        this.player = player;
        this.editable = editable;
        this.cancellable = cancellable;
    }

    //PACKET INFORMATION GETTERS

    /**
     * Check if this Event is being called Async or Sync with the Primary Thread!
     *
     * @return if this Event is async. (most likely yes)
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * Get the NMS packet which you can use by parsing it or using reflection!
     *
     * @return the NMS Packet.
     */
    public Object getPacket() {
        return packet;
    }

    /**
     * Gets the result of this packet event!
     *
     * @return the result of the packet event or null if the packet event hasn't finished yet!
     */
    public EventResult getResult() {
        return result.getNow(null);
    }

    /**
     * Set the packet the client should receive in this packet sending.
     *
     * @param packet The packet.
     */
    public void setPacket(Object packet) {
        if (this.editable) {
            this.packet = packet;
            this.hasBeenEdited = true;
        } else {
            throw new NMSException("This PacketEvent is not editable!");
        }
    }

    /**
     * Set the packet the client should receive in this packet sending.
     *
     * @param packet The packet.
     */
    public void setPacket(DefinedPacket packet) {
        this.setPacket(packet.build());
    }

    /**
     * The player who originated this packet!
     *
     * @return The player or null if none!
     */
    public JPlayer getPlayer() {
        return player;
    }


    //CANCELLED THING!

    /**
     * Cancel this Packet from reaching the player or the server.!
     *
     * @param cancelled if the packet is cancelled.
     */
    public void setCancelled(boolean cancelled) {
        if (!cancellable)
            throw new UnsupportedOperationException("Cannot cancel this packet! It's not cancellable!");
        this.cancelled = cancelled;
    }

    /**
     * Is this packet cancelled?
     *
     * @return If the packet is cancelled or not.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Has this packet been edited?
     *
     * @return If this packet has been edited.
     */
    public boolean hasBeenEdited() {
        return this.hasBeenEdited;
    }


    //CANCELLED THING! END


    //CHECKS

    /**
     * Is this packet cancellable?
     *
     * @return If this packet is cancellable!
     */
    public boolean isCancellable() {
        return cancellable;
    }

    /**
     * Is this packet editable?
     *
     * @return If this packet is editable!
     */
    public boolean isEditable() {
        return editable;
    }
}
