package dev.jadss.jadapi.management.nms.objects.world.block.state;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.objects.world.block.state.impl.IntegerState;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents an integer value!
 */
public interface IntegerBlockState extends StateType<Integer> {

    public static final Class<?> blockStateIntegerClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + JReflection.getNMSVersion()) + ".BlockStateInteger");

    /**
     * Create an instance of a BlockState.
     * @param nmsName the name of the NMS Handle!
     * @param minimum the minimum value.
     * @param maximum the maximum value.
     * @return the Parsed BlockState from NMS in JadAPI form.
     */
    static IntegerBlockState createInstance(String nmsName, int minimum, int maximum) {
        return new IntegerState(nmsName, minimum, maximum);
    }

    /**
     * Get the minimum that this BlockState can be set to.
     * @return The minimum value.
     */
    int getMinimum();

    /**
     * Get the maximum that this BlockState can be set to.
     * @return The maximum value.
     */
    int getMaximum();

    @Override
    default Type getType() {
        return Type.INTEGER;
    }
}
