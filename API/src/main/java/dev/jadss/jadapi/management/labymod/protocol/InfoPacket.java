package dev.jadss.jadapi.management.labymod.protocol;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Detecting a <b>LabyMod User</b> and know which protocols are enabled at the player's client.
 */
public class InfoPacket extends LabyModPacket {

    public String version;
    public ChunkCachingProtocol ccp;
    public ShadowProtocol shadow;
    public Addon[] addons;
    public Mod[] mods;

    public InfoPacket() { }

    @Override
    public String getMessageKey() {
        return "info";
    }

    public InfoPacket(String version, ChunkCachingProtocol ccp, ShadowProtocol shadow, Addon[] addons, Mod[] mods) {
        this.version = version;
        this.ccp = ccp;
        this.shadow = shadow;
        this.addons = addons;
        this.mods = mods;
    }

    @Override
    public void parse(JsonObject object) {
        //Version
        this.version = object.get("version").getAsString();

        //ChunkCachingProtocol
        JsonObject chunkCachingObject = object.get("ccp").getAsJsonObject();
        this.ccp = new ChunkCachingProtocol(chunkCachingObject.get("enabled").getAsBoolean(), chunkCachingObject.get("version").getAsInt());

        //ShadowProtocol
        JsonObject shadowObject = object.get("shadow").getAsJsonObject();
        this.shadow = new ShadowProtocol(shadowObject.get("enabled").getAsBoolean(), shadowObject.get("version").getAsInt());

        //Addons
        JsonArray addonsObject = object.get("addons").getAsJsonArray();
        List<Addon> addons = new ArrayList<>();
        addonsObject.forEach(addon -> {
            addons.add(new Addon(UUID.fromString(addon.getAsJsonObject().get("uuid").getAsString()), addon.getAsJsonObject().get("name").getAsString()));
        });
        this.addons = addons.toArray(new Addon[0]);

        //Mods
        JsonArray modsObject = object.get("mods").getAsJsonArray();
        List<Mod> mods = new ArrayList<>();
        modsObject.forEach(mod -> {
            mods.add(new Mod(mod.getAsJsonObject().get("hash").getAsString(), mod.getAsJsonObject().get("name").getAsString()));
        });
        this.mods = mods.toArray(new Mod[0]);
    }

    @Override
    public String buildString() {
        return g.toJson(this);
    }

    @Override
    public LabyModPacket copy() {
        return new InfoPacket(this.version, this.ccp.copy(), this.shadow.copy(), Arrays.asList(this.addons).toArray(new Addon[0]), Arrays.asList(this.mods).toArray(new Mod[0]));
    }

    public class ChunkCachingProtocol implements Copyable<ChunkCachingProtocol> {
        public boolean enabled;
        public int version;

        public ChunkCachingProtocol(boolean enabled, int version) {
            this.enabled = enabled;
            this.version = version;
        }

        @Override
        public ChunkCachingProtocol copy() {
            return new ChunkCachingProtocol(this.enabled, this.version);
        }
    }

    public class ShadowProtocol implements Copyable<ShadowProtocol> {
        public boolean enabled;
        public int version;

        public ShadowProtocol(boolean enabled, int version) {
            this.enabled = enabled;
            this.version = version;
        }

        @Override
        public ShadowProtocol copy() {
            return new ShadowProtocol(this.enabled, this.version);
        }
    }

    public class Addon {
        public UUID uuid;
        public String name;

        public Addon(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }
    }

    public class Mod {
        public String hash;
        public String name;

        public Mod(String hash, String name) {
            this.hash = hash;
            this.name = name;
        }
    }
}
