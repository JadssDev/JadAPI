package dev.jadss.jadapi.management.labymod.displays;

import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

/**
 * Show <b>LabyMod Water mark</b> in the bottom right corner of the player's screen.
 */
public class WatermarkPacket extends LabyModPacket {

    public boolean visible;

    public WatermarkPacket(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String getMessageKey() {
        return "watermark";
    }

    @Override
    public void parse(JsonObject object) {
        this.visible = object.get("visible").getAsBoolean();
    }

    @Override
    public String buildString() {
        return g.toJson(this);
    }

    @Override
    public LabyModPacket copy() {
        return new WatermarkPacket(visible);
    }
}
