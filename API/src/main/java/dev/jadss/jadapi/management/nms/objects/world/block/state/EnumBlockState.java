package dev.jadss.jadapi.management.nms.objects.world.block.state;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.objects.world.block.state.impl.EnumState;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents an NMS BlockState which uses Enums!
 * @param <N> The NMS Enum type.
 */
public interface EnumBlockState<N extends NMSEnum> extends StateType<N> {

    public static final Class<?> blockStateEnumClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + JReflection.getNMSVersion()) + ".BlockStateEnum");

    /**
     * Create an instance of a BlockState.
     * @param nmsName the Name of the NMS Handle!
     * @param cls the Class of the Enum.
     * @param nmsValue Give an example of this enum!
     * @param <A> the type of the Enum.
     * @return the Parsed BlockState in JadAPI form.
     */
    static <A extends NMSEnum> EnumBlockState<A> createInstance(String nmsName, Class<A> cls, A nmsValue) {
        return new EnumState<>(nmsName, cls, nmsValue.getNMSEnumClass());
    }

    /**
     * Create an instance of a BlockState.
     * @param nmsName the Name of the NMS Handle!
     * @param cls the Class of the Enum.
     * @param nmsClass the Class in the NMS library!
     * @param <A> the type of the Enum.
     * @return the Parsed BlockState in JadAPI form.
     */
    static <A extends NMSEnum> EnumBlockState<A> createInstance(String nmsName, Class<A> cls, Class<?> nmsClass) {
        return new EnumState<>(nmsName, cls, nmsClass);
    }

    /**
     * Get the parsed enum of the {@link NMSEnum} in JadAPI!
     * @return The parsed enum of the {@link NMSEnum} in JadAPI!
     */
    Class<N> getParsedClass();

    default Type getType() {
        return Type.ENUM;
    }
}
