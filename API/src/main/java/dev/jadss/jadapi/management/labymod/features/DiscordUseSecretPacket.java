package dev.jadss.jadapi.management.labymod.features;

import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.UUID;

/**
 * Sent when a user uses a token of his friend on <b>Discord</b>.
 */
public class DiscordUseSecretPacket extends LabyModPacket {

    public UUID joinSecret;
    public UUID spectateSecret;

    public DiscordUseSecretPacket(UUID joinSecret, UUID spectateSecret) {
        this.joinSecret = joinSecret;
        this.spectateSecret = spectateSecret;
    }

    @Override
    public String getMessageKey() {
        return "discord_rpc";
    }

    @Override
    public void parse(JsonObject object) {
        if(object.has("spectateSecret")) {
            this.spectateSecret = UUID.fromString(object.get("spectateSecret").getAsString());
        }
        if(object.has("joinSecret")) {
            this.joinSecret = UUID.fromString(object.get("joinSecret").getAsString());;
        }
    }

    @Override
    public String buildString() {
        return g.toJson(this);
    }

    @Override
    public LabyModPacket copy() {
        return new DiscordUseSecretPacket(joinSecret, spectateSecret);
    }
}
