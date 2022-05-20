package dev.jadss.jadapi.management.labymod;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.packets.InCustomPayloadPacket;
import dev.jadss.jadapi.management.nms.packets.OutCustomPayloadPacket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Represents a LabyModPacket, or a {@link InCustomPayloadPacket} with specific stings in the <b>ByteBuf</b>.
 */
public abstract class LabyModPacket implements Copyable<LabyModPacket> {

    protected static final ObjectMapper MAPPER = JsonMapper.builder()
//            .configure(SerializationFeature.INDENT_OUTPUT, true) //No point.
            .configure(MapperFeature.AUTO_DETECT_GETTERS, false) //Necessary, since I don't want it to have "messageKey: <something>", there's no point.
            .configure(MapperFeature.AUTO_DETECT_SETTERS, false)
            .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
            .build();

    public LabyModPacket() {}

    /**
     * Get the key of this message from labymod.
     * @return the Key of this message.
     */
    public abstract String getMessageKey();

    protected static <E extends LabyModPacket> E internalParse(String json, Class<E> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch(Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCouldn't parse the JSON String for the LabyModPacket!"));
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds the JSON String!
     * @return The String!
     */
    public String buildString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch(Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCouldn't build the JSON String for the LabyModPacket!"));
            e.printStackTrace();
            return "{}"; //Compatibility.
        }
    }

    public InCustomPayloadPacket buildIncomingPacket() {
        if (JadAPI.getInstance().getDebug().doMiscDebug()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eBuilding an Incoming LabyModPacket!"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eKey -> " + this.getMessageKey()));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eJSON -> " + this.buildString()));
        }

        return new InCustomPayloadPacket("labymod3:main", NMS.newPacketDataSerializer().writeString(this.getMessageKey().toLowerCase(), Short.MAX_VALUE).writeString(this.buildString(), Short.MAX_VALUE));
    }

    public OutCustomPayloadPacket buildOutgoingPacket() {
        if (JadAPI.getInstance().getDebug().doMiscDebug()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eBuilding an Outgoing LabyModPacket!"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eKey -> " + this.getMessageKey()));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eJSON -> " + this.buildString()));
        }

        return new OutCustomPayloadPacket("labymod3:main", NMS.newPacketDataSerializer().writeString(this.getMessageKey().toLowerCase(), Short.MAX_VALUE).writeString(this.buildString(), Short.MAX_VALUE));
    }



    public enum SentType {
        JOIN,
        WHENEVER;
    }

    /**
     * Specifies when a packet is sent by the client, or server
     */
    @Target(value = ElementType.TYPE)
    public @interface SentWhen {
        SentType value();
    }

    /**
     * Specifies the Wiki page of a packet.
     */
    @Target(value = ElementType.TYPE)
    public @interface WikiPage {
        String value();
    }
}
