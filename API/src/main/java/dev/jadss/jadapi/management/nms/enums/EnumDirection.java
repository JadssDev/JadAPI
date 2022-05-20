package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.block.BlockFace;

/**
 * Represents the direction of a block.
 */
public enum EnumDirection implements NMSEnum {
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0);

    public static final Class<?> enumDirectionClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + JReflection.getNMSVersion()) + ".EnumDirection");

    /**
     * Represents the value of X that has to be added to this Direction.
     */
    public final int x;
    /**
     * Represents the value of Y that has to be added to this Direction.
     */
    public final int y;
    /**
     * Represents the value of Z that has to be added to this Direction.
     */
    public final int z;

    EnumDirection(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EnumDirection opposite() {
        switch(this) {
            case DOWN:
                return UP;
            case UP:
                return DOWN;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case EAST:
                return WEST;
            default: //Get this to happen, to get 5 €.
                return null;
        }
    }

    public static EnumDirection fromBlockFace(BlockFace face) {
        if (face == null) {
            return null;
        }

        switch(face) {
            case DOWN:
                return EnumDirection.DOWN;
            case UP:
                return EnumDirection.UP;
            case NORTH:
                return EnumDirection.NORTH;
            case SOUTH:
                return EnumDirection.SOUTH;
            case WEST:
                return EnumDirection.WEST;
            case EAST:
                return EnumDirection.EAST;
            default:
                return null;
        }
    }

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumDirection is not supported by versions below 1.7");
        return enumDirectionClass.getEnumConstants()[this.ordinal()];
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumDirectionClass;
    }
}
