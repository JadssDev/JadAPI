package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;
import java.util.HashMap;

public class ServerDataPacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public long jadID;
    public String jadAPIVersion;
    public String serverVersion;
    public String[] plugins;
    public String[] pluginsVersion;
    public String[] currentPlayers;
    public long uptime;
    public long eventsCalledTotal;
    public HashMap<String, Long> eventsCalled;
    public double[] tps;

    public ServerDataPacket() {}
    public ServerDataPacket(long a, String b, String c, String[] d, String[] e, String[] f, long g, long h, double[] z, HashMap<String, Long> hashMap) {
        jadID = a;
        jadAPIVersion = b;
        serverVersion = c;
        plugins = d;
        pluginsVersion = e;
        currentPlayers = f;
        uptime = g;
        eventsCalledTotal = h;
        eventsCalled = hashMap;
        tps = z;
    }

    public long getPacketProtocol() { return Protocol.SERVER_DATA.protocolID; }
    public Packet parsePacket(Object object) {
        this.jadID = (Long) getFieldFromPacket("jadID", object);
        this.jadAPIVersion = (String) getFieldFromPacket("jadAPIVersion", object);
        this.serverVersion = (String) getFieldFromPacket("serverVersion", object);
        this.plugins = (String[]) getFieldFromPacket("plugins", object);
        this.pluginsVersion = (String[]) getFieldFromPacket("pluginsVersion", object);
        this.currentPlayers = (String[]) getFieldFromPacket("currentPlayers", object);
        this.uptime = (Long) getFieldFromPacket("uptime", object);
        this.eventsCalledTotal = (Long) getFieldFromPacket("eventsCalledTotal", object);
        this.eventsCalled = (HashMap<String, Long>) getFieldFromPacket("eventsCalled", object);
        this.tps = (double[]) getFieldFromPacket("tps", object);
        return this;
    }
}
