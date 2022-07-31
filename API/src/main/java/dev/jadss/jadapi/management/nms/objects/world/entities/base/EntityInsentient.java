package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public abstract class EntityInsentient extends EntityLiving {

    public static final Class<?> ENTITY_INSENTIENT = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + NMS.getNMSVersion()) + ".EntityInsentient");

    public EntityInsentient(Object entity) {
        super(entity);

        if (!isEntityInsentient(entity))
            throwExceptionNotClass("Entity Insentient");
    }

    public static boolean isEntityInsentient(Object entity) {
        return ENTITY_INSENTIENT.isAssignableFrom(entity.getClass());
    }

    //Custom methods.

}
