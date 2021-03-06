package dev.jadss.jadapi.management.nms.objects.world.entities.types;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public class TileEntityTypesInstance implements NMSObject, NMSManipulable, NMSCopyable {

    public static final Class<?> TILE_ENTITY_TYPE = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.entity" : "server." + NMS.getNMSVersion()) + ".TileEntityTypes");

    private final Object instance;

    public TileEntityTypesInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public NMSObject copy() {
        return new TileEntityTypesInstance(instance);
    }

    @Override
    public Object getHandle() {
        return instance;
    }
}
