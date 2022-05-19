package dev.jadss.jadapi.management.labymod.features.discord;

import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.UUID;

/**
 * Sent when a user uses a token of his friend on <b>Discord</b>.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/labymod/discord_rich_presence")
public class UseDiscordSecretPacket extends LabyModPacket {

    public final UUID joinSecret;
    public final UUID spectateSecret;

    public UseDiscordSecretPacket() {
        this(null, null);
    }

    public UseDiscordSecretPacket(UUID joinSecret, UUID spectateSecret) {
        this.joinSecret = joinSecret;
        this.spectateSecret = spectateSecret;
    }

    @Override
    public String getMessageKey() {
        return "DISCORD_RPC";
    }

    public static UseDiscordSecretPacket parse(String json) {
        return internalParse(json, UseDiscordSecretPacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new UseDiscordSecretPacket(joinSecret, spectateSecret);
    }
}
