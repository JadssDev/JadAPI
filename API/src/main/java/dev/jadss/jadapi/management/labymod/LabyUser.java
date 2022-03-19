package dev.jadss.jadapi.management.labymod;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.management.labymod.protocol.InfoPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user who is using <b>LabyMod</b>.
 */
public class LabyUser {

    private JPlayer player;
    private List<LabyModPacket> packetsSent = new ArrayList<>();
    private InfoPacket info;

    public LabyUser(JPlayer player, InfoPacket info) {
        this.player = player;
        this.info = info;
    }

    /**
     * Add a packet into the list of packets sent by this player!
     * @param packet the Packet in specific!
     */
    public void addPacket(LabyModPacket packet) {
        this.packetsSent.add(packet);
    }

    /**
     * Get the player who is responsible for this object.
     * @return the Player as a {@link JPlayer}!
     */
    public JPlayer getPlayer() { return player; }

    /**
     * Get the {@link InfoPacket} initial that was used to create this object!
     * @return the InfoPacket!
     */
    public InfoPacket getInfo() { return info; }

    /**
     * Get the packets they have sent the entire time.
     * @return A {@link List} with all the packets!
     */
    public List<LabyModPacket> getPacketsSent() { return new ArrayList<>(packetsSent); }
}
