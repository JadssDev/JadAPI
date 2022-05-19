package dev.jadss.jadapi.management.labymod.displays;

import dev.jadss.jadapi.management.labymod.LabyModPacket;

/**
 * Shows a banner in top of the <b>Tablist</b>.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/displays/tablist")
public class BannerPacket extends LabyModPacket {

    public final String url;

    public BannerPacket() {
        this(null);
    }

    public BannerPacket(String url) {
        this.url = url;
    }

    @Override
    public String getMessageKey() {
        return "SERVER_BANNER";
    }

    public static BannerPacket parse(String json) {
        return internalParse(json, BannerPacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new BannerPacket(url);
    }
}
