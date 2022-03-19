package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.JReflection;

public abstract class EntityCreature extends EntityInsentient {

    public static final Class<?> entityCreatureClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".EntityCreature");

    public EntityCreature(Object nmsEntityCreature) {
        super(nmsEntityCreature);
        if(!isEntityCreature(nmsEntityCreature)) throwExceptionNotClass("Entity Creature");
    }

    public boolean isEntityCreature(Object entity) { return entityCreatureClass.isAssignableFrom(entity.getClass()); }

    //Custom methods.

}
