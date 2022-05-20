package dev.jadss.jadapi.management.labymod.displays;

import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Show a flag of a player at <b>Tablist</b>.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/displays/tablist")
public class LanguageFlagsPacket extends LabyModPacket {

    public User[] users;

    public LanguageFlagsPacket(User[] users) {
        this.users = users;
    }
    public LanguageFlagsPacket(List<User> users) {
        this.users = users.toArray(new User[0]);
    }

    @Override
    public String getMessageKey() { return "LANGUAGE_FLAG"; }

    public LanguageFlagsPacket parse(String json) {
        return internalParse(json, LanguageFlagsPacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new LanguageFlagsPacket(Arrays.asList(users));
    }

    /**
     * Represents a User at Tablist.
     */
    public static class User implements Copyable<User> {
        public final UUID uuid;
        public final String code;

        public User(UUID uuid, String code) {
            this.uuid = uuid;
            this.code = code;
        }

        @Override
        public User copy() {
            return new User(this.uuid, this.code);
        }
    }
}
