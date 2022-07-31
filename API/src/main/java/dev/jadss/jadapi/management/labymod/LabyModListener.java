package dev.jadss.jadapi.management.labymod;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.JadAPIPlugin;
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
