package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;

public class InitServerDataPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public String jadAPIVersion;
    public String serverVersion;

    public InitServerDataPacket() {}
    public InitServerDataPacket(String a, String b) { jadAPIVersion = a; serverVersion = b; }

    public long getPacketProtocol() { return Protocol.INITIAL_SERVER_DATA_COLLECTION.protocolID; }
    public Packet parsePacket(Object obj) {
        this.jadAPIVersion = (String) getFieldFromPacket("jadAPIVersion", obj);
        this.serverVersion = (String) getFieldFromPacket("serverVersion", obj);
        return this;
    }
}
