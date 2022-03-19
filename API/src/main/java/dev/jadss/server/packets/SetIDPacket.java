package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;

public class SetIDPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public long currentJadID;
    public long jadID;

    public SetIDPacket() {}
    public SetIDPacket(long a, long b) { currentJadID = a; jadID = b; }

    public long getPacketProtocol() { return Protocol.SET_ID.protocolID; }
    public Packet parsePacket(Object object) {
        this.currentJadID = (long) getFieldFromPacket("currentJadID", object);
        this.jadID = (long) getFieldFromPacket("jadID", object);
        return this;
    }
}
