package dev.jadss.jadapi.management;

/**
 * Represents the Result of an Event!
 */
public class EventResult {

    private final Object packet;
    private final boolean edited;
    private final boolean cancelled;

    public EventResult(Object packet, boolean edited, boolean cancelled) {
        this.packet = packet;
        this.edited = edited;
        this.cancelled = cancelled;
    }

    public Object getPacket() {
        return packet;
    }

    public boolean isEdited() {
        return edited;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
