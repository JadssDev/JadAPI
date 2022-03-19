package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;

public class PlayerDataPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public long jadID;

    public String playerName;
    public boolean isOperator;
    public boolean exists;
    public boolean online;

    public PlayerDataPacket() {}
    public PlayerDataPacket(long z, String a, boolean b, boolean c, boolean d) {
        jadID = z;
        playerName = a;
        isOperator = b;
        exists = c;
        online = d;
    }

    public long getPacketProtocol() { return Protocol.PLAYER_DATA.protocolID; }
    public Packet parsePacket(Object object) {
        this.jadID = (long) getFieldFromPacket("jadID", object);
        this.playerName = (String) getFieldFromPacket("playerName", object);
        this.isOperator = (boolean) getFieldFromPacket("isOperator", object);
        this.exists = (boolean) getFieldFromPacket("exists", object);
        this.online = (boolean) getFieldFromPacket("online", object);
        return this;
    }
}
