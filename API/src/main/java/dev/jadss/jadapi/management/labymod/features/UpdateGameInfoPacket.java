package dev.jadss.jadapi.management.labymod.features;

import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

/**
 * Update the <b>Game Info Status</b> of LabyMod.
 */
public class UpdateGameInfoPacket extends LabyModPacket {

    public boolean hasGame;
    public String gamemode;
    public int game_startTime;
    public int game_endTime;

    public UpdateGameInfoPacket(boolean hasGame, String gamemode, int game_startTime, int game_endTime) {
        this.hasGame = hasGame;
        this.gamemode = gamemode;
        this.game_startTime = game_startTime;
        this.game_endTime = game_endTime;
    }

    @Override
    public String getMessageKey() {
        return "discord_rpc";
    }

    @Override
    public void parse(JsonObject object) {
        this.hasGame = object.get("hasGame").getAsBoolean();
        this.gamemode = object.get("gamemode").getAsString();
        this.game_startTime = object.get("game_startTime").getAsInt();
        this.game_endTime = object.get("game_endTime").getAsInt();
    }

    @Override
    public String buildString() {
        return g.toJson(this);
    }

    @Override
    public LabyModPacket copy() {
        return new UpdateGameInfoPacket(this.hasGame, this.gamemode, this.game_startTime, this.game_endTime);
    }
}
