package dev.jadss.jadapi.management.labymod.cinematic;

import dev.jadss.jadapi.management.labymod.LabyModPacket;

@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/visual/cinematic")
public class CinematicPacket extends LabyModPacket {

    public CinematicPacket() { //Todo: finish this packet class!!!
        throw new UnsupportedOperationException("This packet is not done yet!");
    }

    @Override
    public LabyModPacket copy() {
        return null;
    }

    @Override
    public String getMessageKey() {
        return null;
    }
}
