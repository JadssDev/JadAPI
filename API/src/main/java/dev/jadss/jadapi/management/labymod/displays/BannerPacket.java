package dev.jadss.jadapi.management.labymod.displays;

import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

/**
 * Shows a banner in top of the <b>Tablist</b>.
 */
public class BannerPacket extends LabyModPacket {

    public String url;

    public BannerPacket(String url) {
        this.url = url;
    }

    @Override
    public String getMessageKey() {
        return "server_banner";
    }

    @Override
    public void parse(JsonObject object) {
        this.url = object.get("url").getAsString();
    }

    @Override
    public String buildString() {
        return g.toJson(this);
    }

    @Override
    public LabyModPacket copy() {
        return new BannerPacket(url);
    }
}
