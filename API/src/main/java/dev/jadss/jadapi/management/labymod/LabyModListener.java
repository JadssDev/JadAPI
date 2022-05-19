package dev.jadss.jadapi.management.labymod;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.annotations.ForRemoval;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;

/**
 * A Listener that listens for LabyMod Packets!
 */
public interface LabyModListener {

    /**
     * The owner of this listener!
     * @return the JadAPIPlugin instance.
     * @see JadAPIPlugin
     */
    JadAPIPlugin getOwner();

    /**
     * Handle a new User created!
     * @param user The new User.
     */
    void onUserCreation(LabyUser user);

    /**
     * Handle a packet sent by a player.
     * @param player The player who sent it.
     * @param packet The packet he sent.
     * @see LabyModPacket
     */
    @Deprecated
    @ForRemoval(willBeRemoved = true, expectedVersionForRemoval = "1.23.1", reason = "A new and improved method has been implemented.")
    void handlePacket(JPlayer player, LabyModPacket packet);

    /**
     * Handles a packet sent by a user.
     * @param user The user who sent it.
     * @param packet The packet we received.
     */
    void handlePacket(LabyUser user, LabyModPacket packet);

    /**
     * Send a Packet to the player.
     * @param player the Player to send the packet to.
     * @param packet the Packet to send.
     * @see JadAPI#getInstance()
     * @see JadAPI#getLabyService()
     * @see LabyService#sendPacket(JPlayer, LabyModPacket)
     */
    void sendPacket(JPlayer player, LabyModPacket packet);
}
