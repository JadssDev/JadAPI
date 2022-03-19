package dev.jadss.jadapi.interfaces.managing;

import dev.jadss.jadapi.bukkitImpl.misc.JHologram;
import dev.jadss.jadapi.interfaces.other.PacketListener;
import dev.jadss.jadapi.management.JPacketHook;
import dev.jadss.jadapi.management.JQuickEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents Information about the JadAPI!
 * <p><b>Please do not edit if you don't know what you're doing.</b></p>
 */
public interface JInformationManager {

    //Events.

    /**
     * Get the events that have been called with their specific amount of times!
     * @return A {@link Map} with the name of the event as key and times as value {@link Long} value!
     */
    Map<String, Long> getEventsCalled();
    /**
     * Get in total how many times events have been called!
     * @return A long number.
     */
    long getEventsCalledTotal();

    /**
     * Add a call to the list.
     * @param eventName The event name.
     */
    void addEventCall(String eventName);



    //Packet calling stuff.

    /**
     * Get the amount of packets we sent to players.
     * @return A {@link Map}.
     */
    Map<UUID, Long> getPacketsSentToPlayers();

    /**
     * Get the amount of packets we received from players.
     * @return A {@link Map}.
     */
    Map<UUID, Long> getPacketsReceivedByPlayers();



    //JadAPI blocks.

    /**
     * Get the QuickEvents we have registered so far.
     * @return A {@link List} with the QuickEvents.
     */
    List<JQuickEvent> getQuickEvents();

    /**
     * Get the PacketHooks we have registered so far.
     * @return A {@link List} with the PacketHooks.
     */
    List<JPacketHook> getPacketHooks();

    /**
     * Get the Holograms created.
     * @return A {@link List} with Holograms.
     */
    List<JHologram> getHolograms();



    //A lil big brain ones.

    /**
     * Get the Packets hooks registered ( contains NPCS AND Holograms, since these do require some packets to work on <b>Right-click</b>)
     * @return A {@link List} with PacketListeners!
     */
    List<PacketListener<?>> getPacketListeners();

    /**
     * Add a PacketListener, you may have to remove it later tho.
     * @param listener The Listener to add.
     */
    void addPacketListener(PacketListener<?> listener);

    /**
     * Remove a PacketListener. I told you!
     * @param listener the Listener to add!
     */
    void removePacketListener(PacketListener<?> listener);



    //Others

    /**
     * Get the uptime JadAPI Registered!
     * @return the Uptime as Long!
     */
    long getUptime();


}
