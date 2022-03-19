package dev.jadss.jadapi.management.labymod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.packets.InCustomPayloadPacket;
import dev.jadss.jadapi.management.nms.packets.OutCustomPayloadPacket;

/**
 * Represents a LabyModPacket, or a {@link InCustomPayloadPacket} with specific stings in the <b>ByteBuf</b>.
 */
public abstract class LabyModPacket implements Copyable<LabyModPacket> {

    protected static final Gson g = new Gson();

    public LabyModPacket() {}

    /**
     * Get the key of this message from labymod.
     * @return the Key of this message.
     */
    public abstract String getMessageKey();

    /**
     * Parse the <b>JSON Object</b>.
     * @param object the data from labymod packet.
     */
    public abstract void parse(JsonObject object);

    /**
     * Builds the JSON String!
     * @return The String!
     */
    public abstract String buildString();

    public InCustomPayloadPacket buildIncomingPacket() {
        return new InCustomPayloadPacket("labymod3:main", NMS.newPacketDataSerializer().writeString(this.getMessageKey(), Short.MAX_VALUE).writeString(this.buildString(), Short.MAX_VALUE));
    }

    public OutCustomPayloadPacket buildOutgoingPacket() {
        return new OutCustomPayloadPacket("labymod3:main", NMS.newPacketDataSerializer().writeString(this.getMessageKey(), Short.MAX_VALUE).writeString(this.buildString(), Short.MAX_VALUE));
    }
}
