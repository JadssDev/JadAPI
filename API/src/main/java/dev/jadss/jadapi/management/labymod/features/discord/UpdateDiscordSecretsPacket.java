package dev.jadss.jadapi.management.labymod.features.discord;

import dev.jadss.jadapi.management.labymod.LabyModPacket;

/**
 * Play out the <b>Discord tokens</b> to the <b>LabyMod Client</b> so it can allow other players to join through <b>Discord</b>.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/labymod/discord_rich_presence")
public class UpdateDiscordSecretsPacket extends LabyModPacket {

    public final boolean hasMatchSecret;
    public final boolean hasSpectateSecret;
    public final boolean hasJoinSecret;

    public final String matchSecret;
    public final String spectateSecret;
    public final String joinSecret;

    public final String domain;

    public UpdateDiscordSecretsPacket() {
        this(null, null, null, null);
    }

    public UpdateDiscordSecretsPacket(String matchSecret, String spectateSecret, String joinSecret, String domain) {
        { //ID
            if(matchSecret != null) {
                this.hasMatchSecret = true;
            } else {
                this.hasMatchSecret = false;
            }
            this.matchSecret = matchSecret;
        }

        { //Spectate
            if(spectateSecret != null) {
                this.hasSpectateSecret = true;
            } else {
                this.hasSpectateSecret = false;
            }
            this.spectateSecret = spectateSecret;
        }

        { //Join
            if(joinSecret != null) {
                this.hasJoinSecret = true;
            } else {
                this.hasJoinSecret = false;
            }
            this.joinSecret = joinSecret;
        }

        //Domain
        this.domain = domain;
    }

    @Override
    public String getMessageKey() {
        return "DISCORD_RPC";
    }

    public static UpdateDiscordSecretsPacket parse(String json) {
        return internalParse(json, UpdateDiscordSecretsPacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new UpdateDiscordSecretsPacket(this.matchSecret, this.spectateSecret, this.joinSecret, this.domain);
    }
}
