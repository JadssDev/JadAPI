package dev.jadss.jadapi.management.nms.objects.world.block.state;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.objects.world.block.state.impl.BooleanState;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

/**
 * Represents a Boolean BlockState!
 */
public interface BooleanBlockState extends StateType<Boolean> {

    public static final Class<?> blockStateBooleanClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + ".BlockStateBoolean");

    /**
     * Create an instance of a BlockState.
     * @param nmsName the name of the NMS Handle!
     * @param defaultState the default state of this block state!
     * @return the Parsed BlockState in JadAPI form.
     */
    static BooleanBlockState createInstance(String nmsName, boolean defaultState) {
        return new BooleanState(nmsName, defaultState);
    }

    /**
     * The default value for this block state.
     * @return the default value.
     */
    Boolean getDefaultValue();

    default Type getType() {
        return Type.BOOLEAN;
    }
}
