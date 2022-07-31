package dev.jadss.jadapi.management.nms.objects.world.block;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

public class IBlockState implements NMSObject, NMSManipulable {

    public static final Class<?> iBlockStateClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + ".IBlockState");

    private final Object handle;

    public IBlockState(Object handle) {
        if(!iBlockStateClass.isAssignableFrom(handle.getClass()))
            throw new NMSException("The given handle is not an IBlockState!");
        this.handle = handle;
    }

    public String getId() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            return JMethodReflector.executeUnspecificMethod(iBlockStateClass, new Class[] {}, String.class, this.handle, null);
        } else {
            return JFieldReflector.getObjectFromUnspecificField(iBlockStateClass, String.class, this.handle);
        }
    }

    @Override
    public Object getHandle() {
        return handle;
    }
}
