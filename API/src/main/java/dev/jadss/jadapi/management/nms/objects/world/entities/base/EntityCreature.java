package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public abstract class EntityCreature extends EntityInsentient {

    public static final Class<?> ENTITY_CREATURE = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + NMS.getNMSVersion()) + ".EntityCreature");

    public EntityCreature(Object entity) {
        super(entity);

        if (!isEntityCreature(entity))
            throwExceptionNotClass("Entity Creature");
    }

    public static boolean isEntityCreature(Object entity) {
        return ENTITY_CREATURE.isAssignableFrom(entity.getClass());
    }

    //Custom methods.

}
