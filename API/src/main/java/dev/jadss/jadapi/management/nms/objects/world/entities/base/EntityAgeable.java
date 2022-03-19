package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.JReflection;

public abstract class EntityAgeable extends EntityCreature {

    public static final Class<?> entityAgeableClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".EntityAgeable");

    public EntityAgeable(Object nmsEntityAgeable) {
        super(nmsEntityAgeable);
        if(!isEntityAgeable(nmsEntityAgeable)) throwExceptionNotClass("Entity Ageable");
    }

    public boolean isEntityAgeable(Object entity) { return entityAgeableClass.isAssignableFrom(entity.getClass()); }

    //Custom methods.
}
