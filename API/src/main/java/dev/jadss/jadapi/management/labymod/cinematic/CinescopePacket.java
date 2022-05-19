package dev.jadss.jadapi.management.labymod.cinematic;

import dev.jadss.jadapi.management.labymod.LabyModPacket;

public class CinescopePacket extends LabyModPacket {

    /**
     * Coverage is from 0% to 50%.
     */
    public final int coverage;
    /**
     * Duration in seconds.
     */
    public final long duration;

    public CinescopePacket() {
        this(0, 0L);
    }

    public CinescopePacket(int coverage, long duration) {
        this.coverage = coverage;
        this.duration = duration;
    }

    @Override
    public String getMessageKey() {
        return "CINESCOPES";
    }

    public static CinescopePacket parse(String json) {
        return internalParse(json, CinescopePacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new CinescopePacket(this.coverage, this.duration);
    }
}
