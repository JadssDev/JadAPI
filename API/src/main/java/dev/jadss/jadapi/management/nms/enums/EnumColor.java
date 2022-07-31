package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;
import org.bukkit.DyeColor;

import java.util.Arrays;

/**
 * Represents the Colors in the NMS, don't confuse this with {@link EnumChatFormat}, they are different!
 * <p>Introduced in 1.8</p>
 */
public enum EnumColor implements NMSEnum {
    WHITE(DyeColor.WHITE),
    ORANGE(DyeColor.ORANGE),
    MAGENTA(DyeColor.MAGENTA),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE),
    YELLOW(DyeColor.YELLOW),
    LIME(DyeColor.LIME),
    PINK(DyeColor.PINK),
    GRAY(DyeColor.GRAY),
    //Name changed in 1.13 =) from SILVER to LIGHT_GRAY =) yeeeey!
    LIGHT_GRAY(JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? DyeColor.valueOf("LIGHT_GRAY") : DyeColor.valueOf("SILVER")),
    CYAN(DyeColor.CYAN),
    PURPLE(DyeColor.PURPLE),
    BLUE(DyeColor.BLUE),
    BROWN(DyeColor.BROWN),
    GREEN(DyeColor.GREEN),
    RED(DyeColor.RED),
    BLACK(DyeColor.BLACK);

    public static final Class<?> enumColorClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.item" : "server." + NMS.getNMSVersion()) + ".EnumColor");
    private static final EnumColor[] VALUES = values();

    private final DyeColor bukkitColor;

    EnumColor(DyeColor color) {
        this.bukkitColor = color;
    }

    /**
     * Get an object in the NMS enum.
     * @param color the Dye in bukkit.
     * @return the object in the NMS enum.
     */
    public static Object getEnumColor(DyeColor color) {
        return Arrays.stream(VALUES).filter(c -> c.getColor() == color).findAny().orElse(null);
    }

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumColor is not supported in versions below 1.7");
        return JEnumReflector.getEnum(this.ordinal(), enumColorClass);
    }

    public DyeColor getColor() {
        return bukkitColor;
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumColorClass;
    }
}
