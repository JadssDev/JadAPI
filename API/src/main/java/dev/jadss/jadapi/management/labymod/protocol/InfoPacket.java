package dev.jadss.jadapi.management.labymod.protocol;

import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.Arrays;
import java.util.UUID;

/**
 * Sent by a <b>LabyMod User</b> for the server to know which protocols are enabled at the client.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.JOIN)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/protocol/onjoin")
public class InfoPacket extends LabyModPacket {

    public final String version;
    public final ChunkCachingProtocol ccp;
    public final ShadowProtocol shadow;
    public final Addon[] addons;
    public final Mod[] mods;

    public InfoPacket() {
        this(null, null, null, null, null);
    }

    public InfoPacket(String version, ChunkCachingProtocol ccp, ShadowProtocol shadow, Addon[] addons, Mod[] mods) {
        this.version = version;
        this.ccp = ccp;
        this.shadow = shadow;
        this.addons = addons;
        this.mods = mods;
    }

    @Override
    public String getMessageKey() {
        return "INFO";
    }

    public static InfoPacket parse(String jsonString) {
        return internalParse(jsonString, InfoPacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new InfoPacket(this.version, this.ccp.copy(), this.shadow.copy(), Arrays.asList(this.addons).toArray(new Addon[0]), Arrays.asList(this.mods).toArray(new Mod[0]));
    }

    public static class ChunkCachingProtocol implements Copyable<ChunkCachingProtocol> {
        public final boolean enabled;
        public final int version;

        public ChunkCachingProtocol() {
            this(false, 0);
        }

        public ChunkCachingProtocol(boolean enabled, int version) {
            this.enabled = enabled;
            this.version = version;
        }

        @Override
        public ChunkCachingProtocol copy() {
            return new ChunkCachingProtocol(this.enabled, this.version);
        }
    }

    public static class ShadowProtocol implements Copyable<ShadowProtocol> {
        public final boolean enabled;
        public final int version;

        public ShadowProtocol() {
            this(false, 0);
        }

        public ShadowProtocol(boolean enabled, int version) {
            this.enabled = enabled;
            this.version = version;
        }

        @Override
        public ShadowProtocol copy() {
            return new ShadowProtocol(this.enabled, this.version);
        }
    }

    public static class Addon implements Copyable<Addon> {
        public final UUID uuid;
        public final String name;

        public Addon() {
            this(null, null);
        }

        public Addon(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }

        @Override
        public Addon copy() {
            return new Addon(this.uuid, this.name);
        }
    }

    public static class Mod implements Copyable<Mod>{

        public final String hash;
        public final String name;

        public Mod() {
            this(null, null);
        }

        public Mod(String hash, String name) {
            this.hash = hash;
            this.name = name;
        }

        @Override
        public Mod copy() {
            return new Mod(this.hash, this.name);
        }
    }
}
