package dev.jadss.jadapi.management.labymod.features;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * When clicking middle mouse wheel, it will bring a <b>menu</b>, this menu can be <b>configured here</b>.
 */
public class ActionMenuPacket extends LabyModPacket {

    public MenuAction[] actions;

    public ActionMenuPacket(MenuAction[] actions) { this.actions = actions; }
    public ActionMenuPacket(List<MenuAction> actions) { this.actions = actions.toArray(new MenuAction[0]); }

    @Override
    public String getMessageKey() {
        return "user_menu_actions";
    }

    @Override
    public void parse(JsonObject obj) {
        JsonArray object = obj.getAsJsonArray();
        List<MenuAction> actions = new ArrayList<>();
        object.forEach(entry -> actions.add(new MenuAction(entry.getAsJsonObject().get("displayName").getAsString(), EnumActionType.valueOf(entry.getAsJsonObject().get("type").getAsString()), entry.getAsJsonObject().get("value").getAsString())));
        this.actions = actions.toArray(new MenuAction[0]);
    }

    @Override
    public String buildString() {
        return g.toJson(actions);
    }

    @Override
    public LabyModPacket copy() {
        return new ActionMenuPacket(Arrays.asList(actions));
    }

    /**
     * Represents a button at the menu.
     */
    public static class MenuAction {
        public String displayName;
        public String type;
        public String value;

        /**
         * Create a MenuAction!
         * @param displayName The display name on the Menu!
         * @param type What it should do.
         * @param value What command, {name} - placeholder.
         */
        public MenuAction(String displayName, EnumActionType type, String value) {
            this.displayName = displayName;
            this.type = type.name();
            this.value = value;
        }
    }

    public enum EnumActionType {
        NONE,
        CLIPBOARD,
        RUN_COMMAND,
        SUGGEST_COMMAND,
        OPEN_BROWSER
    }
}
