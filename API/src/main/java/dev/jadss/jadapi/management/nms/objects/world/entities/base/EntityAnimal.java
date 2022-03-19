package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.JReflection;

public abstract class EntityAnimal extends EntityAgeable {

    public final static Class<?> entityAnimalClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.animal" : "server." + JReflection.getNMSVersion()) + ".EntityAnimal");;

    public EntityAnimal(Object nmsEntityAgeable) {
        super(nmsEntityAgeable);
        if(!isEntityAnimal(nmsEntityAgeable)) throwExceptionNotClass("Entity Animal");
    }

    public boolean isEntityAnimal(Object entity) { return entityAnimalClass.isAssignableFrom(entity.getClass()); }

    //Custom methods.
}
