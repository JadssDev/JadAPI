package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public abstract class EntityAnimal extends EntityAgeable {

    public static final Class<?> ENTITY_ANIMAL = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.animal" : "server." + NMS.getNMSVersion()) + ".EntityAnimal");

    public EntityAnimal(Object entity) {
        super(entity);

        if (!isEntityAnimal(entity))
            throwExceptionNotClass("Entity Animal");
    }

    public static boolean isEntityAnimal(Object entity) {
        return ENTITY_ANIMAL.isAssignableFrom(entity.getClass());
    }

    //Custom methods.
}
