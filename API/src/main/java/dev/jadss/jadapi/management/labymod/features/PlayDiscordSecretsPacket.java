package dev.jadss.jadapi.management.labymod.features;

import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.UUID;

/**
 * Play out the <b>Discord tokens</b> to the <b>LabyMod Client</b> so it can allow other players to join through <b>Discord</b>.
 */
public class PlayDiscordSecretsPacket extends LabyModPacket {

    public boolean hasMatchSecret = false;
    public boolean hasSpectateSecret = false;
    public boolean hasJoinSecret = false;

    public String matchSecret;
    public String spectateSecret;
    public String joinSecret;

    public String domain;

    public PlayDiscordSecretsPacket(String matchSecret, String spectateSecret, String joinSecret, String domain) {
        this.matchSecret = matchSecret;
        if(matchSecret != null) this.hasMatchSecret = true;
        this.spectateSecret = spectateSecret;
        if(spectateSecret != null) this.hasSpectateSecret = true;
        this.joinSecret = joinSecret;
        if(joinSecret != null) this.hasJoinSecret = true;

        this.domain = domain;
    }

    @Override
    public String getMessageKey() {
        return "discord_rpc";
    }

    @Override
    public void parse(JsonObject object) {
        if(object.has("matchSecret")) {
            this.matchSecret = object.get("matchSecret").getAsString().split(":")[0];
            this.hasMatchSecret = true;
            this.domain = object.get("matchSecret").getAsString().split(":")[1];
        }
        if(object.has("joinSecret")) {
            this.joinSecret = object.get("joinSecret").getAsString().split(":")[0];
            this.hasJoinSecret = true;
            this.domain = object.get("joinSecret").getAsString().split(":")[1];
        }
        if(object.has("spectateSecret")) {
            this.spectateSecret = object.get("spectateSecret").getAsString().split(":")[0];
            this.hasSpectateSecret = true;
            this.domain = object.get("spectateSecret").getAsString().split(":")[1];
        }
    }

    @Override
    public String buildString() {
        JsonObject object = new JsonObject();
        if(hasMatchSecret)
            addSecret(object, "hasMatchSecret", "matchSecret", UUID.fromString(this.matchSecret), this.domain);
        if(hasJoinSecret)
            addSecret(object, "hasJoinSecret", "joinSecret", UUID.fromString(this.joinSecret), this.domain);
        if(hasSpectateSecret)
            addSecret(object, "hasSpectateSecret", "spectateSecret", UUID.fromString(this.spectateSecret), this.domain);
        return object.toString();
    }

    private void addSecret(JsonObject jsonObject, String hasKey, String key, UUID secret, String domain) {
        jsonObject.addProperty( hasKey, true );
        jsonObject.addProperty( key, secret.toString() + ":" + domain );
    }

    @Override
    public LabyModPacket copy() {
        return new PlayDiscordSecretsPacket(this.matchSecret, this.spectateSecret, this.joinSecret, this.domain);
    }
}
