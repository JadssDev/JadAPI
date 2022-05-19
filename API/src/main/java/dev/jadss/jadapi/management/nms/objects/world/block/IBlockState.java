package dev.jadss.jadapi.management.nms.objects.world.block;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.JReflection;

public class IBlockState implements NMSObject, NMSManipulable {

    public static final Class<?> iBlockStateClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + JReflection.getNMSVersion()) + ".IBlockState");

    private final Object handle;

    public IBlockState(Object handle) {
        if(!iBlockStateClass.isAssignableFrom(handle.getClass()))
            throw new NMSException("The given handle is not an IBlockState!");
        this.handle = handle;
    }

    public String getId() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            return JReflection.executeMethod(iBlockStateClass, new Class[] {}, handle, String.class, (i) -> 0);
        } else {
            return JReflection.getFieldObject(iBlockStateClass, String.class, handle);
        }
    }

    @Override
    public Object getHandle() {
        return handle;
    }
}
