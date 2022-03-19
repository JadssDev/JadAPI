package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;

public class BlacklistedPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public long jadID;
    public String reason;

    public BlacklistedPacket() {}
    public BlacklistedPacket(long a, String l) { jadID = a; reason = l; }

    public long getPacketProtocol() { return Protocol.BLACKLISTED.protocolID; }
    public Packet parsePacket(Object obj) {
        this.jadID = (Long) getFieldFromPacket("jadID", obj);
        this.reason = (String) getFieldFromPacket("reason", obj);
        return this;
    }
}
