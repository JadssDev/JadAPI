package dev.jadss.jadapi.management.labymod.displays;

import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

/**
 * Show <b>LabyMod Water mark</b> in the bottom right corner of the player's screen.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/displays/watermark")
public class WatermarkPacket extends LabyModPacket {

    public final boolean visible;

    public WatermarkPacket() {
        this(false);
    }

    public WatermarkPacket(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String getMessageKey() {
        return "WATERMARK";
    }

    public static WatermarkPacket parse(String json) {
        return internalParse(json, WatermarkPacket.class );
    }

    @Override
    public LabyModPacket copy() {
        return new WatermarkPacket(visible);
    }
}
