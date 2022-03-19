package dev.jadss.server;

import java.io.Serializable;

public enum Protocol implements Serializable {
    BLACKLISTED(-4),
    INITIAL_SERVER_DATA_COLLECTION(-3),
    SET_ID(-2),
    REQUEST_DATA(2),
    SERVER_DATA(3),
    REQUEST_PLAYER_DATA(6),
    PLAYER_DATA(7),
    SERVER_SHUTTING_DOWN(10),
    SEND_MESSAGE(16);

    public long protocolID;

    Protocol(long id) {
        this.protocolID = id;
    }

    public static Protocol parseProtocol(long id) {
        for(Protocol p : values()) {
            if(p.protocolID == id) return p;
        }
        return null;
    }
}
