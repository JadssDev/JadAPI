package dev.jadss.jadapi.management.labymod.displays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Show a flag of a player at <b>Tablist</b>.
 */
public class LanguageFlagsPacket extends LabyModPacket {

    public User[] users;

    public LanguageFlagsPacket(User[] users) {
        this.users = users;
    }
    public LanguageFlagsPacket(List<User> users) {
        this.users = users.toArray(new User[0]);
    }

    @Override
    public String getMessageKey() { return "language_flag"; }

    @Override
    public void parse(JsonObject object) {
        JsonArray array = object.get("users").getAsJsonArray();

        List<User> list = new ArrayList<>();
        array.forEach(element -> list.add(new User(UUID.fromString(element.getAsJsonObject().get("uuid").getAsString()), element.getAsJsonObject().get("code").getAsString())));

        this.users = list.toArray(new User[0]);
    }

    @Override
    public String buildString() {
        return g.toJson(this);
    }

    @Override
    public LabyModPacket copy() {
        return new LanguageFlagsPacket(Arrays.asList(users));
    }

    /**
     * Represents a User at Tablist.
     */
    public static class User {
        public UUID uuid;
        public String code;

        public User(UUID uuid, String code) {
            this.uuid = uuid;
            this.code = code;
        }
    }
}
