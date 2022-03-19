package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;
import java.util.HashMap;

public class ServerShutdownPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public long jadID;
    public double[] tps;
    public HashMap<String, Long> eventsCalled;
    public long totalEvents;

    public ServerShutdownPacket() {}
    public ServerShutdownPacket(long f, double[] z, HashMap<String, Long> a, long b) {
        jadID = f;
        tps = z;
        eventsCalled = a;
        totalEvents = b;
    }

    public long getPacketProtocol() { return Protocol.SERVER_SHUTTING_DOWN.protocolID; }
    public Packet parsePacket(Object object) {
        this.jadID = (long) getFieldFromPacket("jadID", object);
        this.tps = (double[]) getFieldFromPacket("tps", object);
        this.eventsCalled = (HashMap<String, Long>) getFieldFromPacket("eventsCalled", object);
        this.totalEvents = (long) getFieldFromPacket("totalEvents", object);
        return this;
    }
}
