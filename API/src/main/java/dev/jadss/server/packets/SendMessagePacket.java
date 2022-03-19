package dev.jadss.server.packets;

import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;

import java.io.Serializable;

public class SendMessagePacket extends Packet implements Serializable {

    private static final long serialVersionUID = 98L;

    public String message;
    public SenderType sender;
    public MessageType type;
    public String playerName;

    public SendMessagePacket() {}
    public SendMessagePacket(String a, SenderType b, MessageType c, String d) { message = a; sender = b; type = c; playerName = d; }

    public long getPacketProtocol() { return Protocol.SEND_MESSAGE.protocolID; }
    public Packet parsePacket(Object obj) {
        this.message = (String) getFieldFromPacket("message", obj);
        this.sender = (SenderType) getFieldFromPacket("sender", obj);
        this.type = (MessageType) getFieldFromPacket("type", obj);
        this.playerName = (String) getFieldFromPacket("playerName", obj);
        return this;
    }

    public enum SenderType implements Serializable {
        CONSOLE, PLAYER, EVERYONE;
    }

    public enum MessageType implements Serializable {
        TITLE, ACTIONBAR, CHAT;
    }
}
