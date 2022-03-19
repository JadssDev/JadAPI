package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.ChatColor;

import java.util.Arrays;

/**
 * Represents the colors in chat in NMS.
 */
public enum EnumChatFormat implements NMSEnum {
    BLACK(ChatColor.BLACK),
    DARK_BLUE(ChatColor.DARK_BLUE),
    DARK_GREEN(ChatColor.DARK_GREEN),
    DARK_AQUA(ChatColor.DARK_AQUA),
    DARK_RED(ChatColor.DARK_RED),
    DARK_PURPLE(ChatColor.DARK_PURPLE),
    GOLD(ChatColor.GOLD),
    GRAY(ChatColor.GRAY),
    DARK_GRAY(ChatColor.DARK_GRAY),
    BLUE(ChatColor.BLUE),
    GREEN(ChatColor.GREEN),
    AQUA(ChatColor.AQUA),
    RED(ChatColor.RED),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE),
    YELLOW(ChatColor.YELLOW),
    WHITE(ChatColor.WHITE),
    //Fun fact: this was renamed to "OBFUSCATED" in 1.8.8
    OBFUSCATED(ChatColor.MAGIC),
    BOLD(ChatColor.BOLD),
    STRIKETHROUGH(ChatColor.STRIKETHROUGH),
    UNDERLINE(ChatColor.UNDERLINE),
    ITALIC(ChatColor.ITALIC),
    RESET(ChatColor.RESET);

    public static final Class<?> enumChatFormat = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "" : "server." + JReflection.getNMSVersion() + ".") + "EnumChatFormat");
    private static final EnumChatFormat[] VALUES = values();

    private final ChatColor bukkitColor;

    EnumChatFormat(ChatColor color) {
        this.bukkitColor = color;
    }

    @Override
    public Object getNMSObject() { return JReflection.getEnum(this.ordinal(), enumChatFormat); }

    public ChatColor getChatColor() {
        return bukkitColor;
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumChatFormat;
    }

    public int getNetworkInt() {
        return JReflection.getUnspecificFieldObject(enumChatFormat, int.class, getNMSObject());
    }

    public static EnumChatFormat getByNetworkInt(int id) {
        return Arrays.stream(VALUES).filter(e -> e.getNetworkInt() == id).findFirst().orElse(null);
    }

    /**
     * Get an object in the NMS enum for chat enums.
     * @param color the color in bukkit.
     * @return the object in the NMS enum.
     */
    public static EnumChatFormat getEnumChatFormat(ChatColor color) {
        return Arrays.stream(VALUES).filter(format -> format.getChatColor() == color).findAny().orElse(null);
    }
}
