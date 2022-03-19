package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.JReflection;

public abstract class EntityInsentient extends EntityLiving {

    public static final Class<?> entityInsentientClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".EntityInsentient");

    public EntityInsentient(Object nmsEntityLiving) {
        super(nmsEntityLiving);
        if(!isEntityInsentient(nmsEntityLiving)) throwExceptionNotClass("Entity Insentient");
    }

    public boolean isEntityInsentient(Object entity) { return entityInsentientClass.isAssignableFrom(entity.getClass()); }

    //Custom methods.

}
