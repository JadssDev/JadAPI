package dev.jadss.jadapi.management.labymod.features;

import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.List;

/**
 * When clicking middle mouse wheel, it will bring a <b>menu</b>, this menu can be <b>configured here</b>.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/labymod/action_menu")
public class ActionMenuPacket extends LabyModPacket {

    public final MenuAction[] actions;

    public ActionMenuPacket() {
        this((MenuAction[]) null);
    }

    public ActionMenuPacket(MenuAction[] actions) {
        this.actions = actions;
    }

    public ActionMenuPacket(List<MenuAction> actions) {
        this.actions = actions.toArray(new MenuAction[0]);
    }

    @Override
    public String getMessageKey() {
        return "USER_MENU_ACTIONS";
    }

    public static ActionMenuPacket parse(String json) {
        return internalParse(json, ActionMenuPacket.class);
    }

    @Override
    public LabyModPacket copy() {
        return new ActionMenuPacket(actions.clone()); //Sometimes instead of clone, it should be "clown()" for me - Jadss
    }

    /**
     * Represents a button at the menu.
     */
    public static class MenuAction {
        public final String displayName;
        public final String type;
        public final String value;

        public MenuAction() {
            this(null, null, null);
        }

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
