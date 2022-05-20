package dev.jadss.jadapi.management.nms.objects.world.entities.types;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.JReflection;

public class EntityTypesInstance implements NMSObject, NMSManipulable, NMSCopyable {

    public static final Class<?> ENTITY_TYPE = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".EntityTypes");

    private final Object instance;

    public EntityTypesInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public NMSObject copy() {
        return new EntityTypesInstance(instance);
    }

    @Override
    public Object getHandle() {
        return instance;
    }
}
