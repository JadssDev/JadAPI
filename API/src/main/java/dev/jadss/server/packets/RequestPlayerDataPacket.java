package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;

public class RequestPlayerDataPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public long jadID;
    public String playerName;

    public RequestPlayerDataPacket() {}
    public RequestPlayerDataPacket(long z, String a) {
        jadID = z;
        playerName = a;
    }

    public long getPacketProtocol() { return Protocol.REQUEST_PLAYER_DATA.protocolID; }
    public Packet parsePacket(Object object) {
        this.jadID = (long) getFieldFromPacket("jadID", object);
        this.playerName = (String) getFieldFromPacket("playerName", object);
        return this;
    }
}
