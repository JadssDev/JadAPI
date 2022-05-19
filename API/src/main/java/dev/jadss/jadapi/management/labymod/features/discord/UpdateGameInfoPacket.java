package dev.jadss.jadapi.management.labymod.features.discord;

import dev.jadss.jadapi.management.labymod.LabyModPacket;

/**
 * Update the <b>Game Info Status</b> of LabyMod.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/labymod/discord_rich_presence")
public class UpdateGameInfoPacket extends LabyModPacket {

    public final boolean hasGame;
    public final String gamemode;
    /**
     * The start time of this game. Set to 0 for countdown.
     */
    public final int game_startTime;
    /**
     * The end time of this game. Set to 0 for timer.
     */
    public final int game_endTime;

    public UpdateGameInfoPacket() {
        this(false, null, 0, 0);
    }

    public UpdateGameInfoPacket(boolean hasGame, String gamemode, int game_startTime, int game_endTime) {
        this.hasGame = hasGame;
        this.gamemode = gamemode;
        this.game_startTime = game_startTime;
        this.game_endTime = game_endTime;
    }

    @Override
    public String getMessageKey() {
        return "DISCORD_RPC";
    }

    public static UpdateGameInfoPacket parse(String json) {
        return internalParse(json, UpdateGameInfoPacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new UpdateGameInfoPacket(this.hasGame, this.gamemode, this.game_startTime, this.game_endTime);
    }
}
