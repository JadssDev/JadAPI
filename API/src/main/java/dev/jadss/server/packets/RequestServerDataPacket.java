package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;

public class RequestServerDataPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public long jadID;

    public RequestServerDataPacket() {}
    public RequestServerDataPacket(long a) { jadID = a; }

    public long getPacketProtocol() { return Protocol.REQUEST_DATA.protocolID; }
    public Packet parsePacket(Object object) {
        this.jadID = (long) getFieldFromPacket("jadID", object);
        return this;
    }
}
