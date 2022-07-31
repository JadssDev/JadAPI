package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public abstract class EntityAgeable extends EntityCreature {

    public static final Class<?> ENTITY_AGEABLE = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + NMS.getNMSVersion()) + ".EntityAgeable");

    public EntityAgeable(Object entity) {
        super(entity);

        if (!isEntityAgeable(entity))
            throwExceptionNotClass("Entity Ageable");
    }

    public static boolean isEntityAgeable(Object entity) {
        return ENTITY_AGEABLE.isAssignableFrom(entity.getClass());
    }

    //Custom methods.
}
