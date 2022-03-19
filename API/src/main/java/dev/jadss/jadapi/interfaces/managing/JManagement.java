package dev.jadss.jadapi.interfaces.managing;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;

/**
 * Managing stuff of the JadAPI, <b>may not be a good idea to touch in this class.</b>
 */
public interface JManagement {

    /**
     * Shutdown the JadAPIServers connection!
     */
    void shutdown();

    /**
     * Add a Channel Lookup, do not mess with this.
     * @param playerName The player.
     * @param channel The channel at Netty.
     */
    void addChannelLookup(String playerName, Object channel);

    /**
     * Inject into this player.
     * @param player the Player.
     */
    void injectPlayer(JPlayer player);

    /**
     * Register the JadAPI handler, will also kick everyone.
     */
    void registerHandler();

    /**
     * Unregister the JadAPI handler, will kick all players.
     */
    void unregisterHandler();
}
