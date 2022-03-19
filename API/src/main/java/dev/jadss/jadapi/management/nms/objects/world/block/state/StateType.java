package dev.jadss.jadapi.management.nms.objects.world.block.state;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.objects.world.block.IBlockState;

/**
 * Represents what this state actually needs to be completed.
 * @param <R> The type of the state.
 */
public interface StateType<R> {

    /**
     * Get the state id in the NMS.
     * @return the State ID that is used to identify this state.
     */
    String getStateId();

    /**
     * Gets the Type of this BlockState.
     * @return The Type of this BlockState.
     */
    Type getType();

    /**
     * Get the object in NMS responsible for this state.
     * @return The object in NMS responsible for this state.
     */
    Object getNMSObject();

    /**
     * Get the IBlockState class in a parsed form from JadAPI.
     * @return The IBlockState class in a parsed form from JadAPI.
     */
    IBlockState getNMSBlockState();

    /**
     * Checks if this state is supported.
     * @return the result.
     */
    boolean isValid();

    /**
     * Are the block states supported!
     * @return test if they are.
     */
    static boolean isStatesSupported() {
        return JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8);
    }

    /**
     * Which type of state is this type.
     */
    enum Type {
        BOOLEAN,
        INTEGER,
        ENUM;
    }
}
