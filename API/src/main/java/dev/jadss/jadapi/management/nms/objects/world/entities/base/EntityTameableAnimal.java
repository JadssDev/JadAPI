package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.JReflection;

import java.util.UUID;

public abstract class EntityTameableAnimal extends EntityAnimal {

    public final static Class<?> entityTameableAnimalClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.animal" : "server." + JReflection.getNMSVersion()) + ".EntityTameableAnimal");;

    public EntityTameableAnimal(Object nmsEntityTameableAnimal) {
        super(nmsEntityTameableAnimal);
        if(!isEntityTameableAnimal(nmsEntityTameableAnimal)) throwExceptionNotClass("Entity Tameable Animal");
    }

    public boolean isEntityTameableAnimal(Object entity) { return entityTameableAnimalClass.isAssignableFrom(entity.getClass()); }

    //Custom methods.

    public boolean isTamed() {
        return (boolean) JReflection.executeMethod(entityTameableAnimalClass, (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17) ? "isTamed" : "q"), this.entity, new Class[] {});
    }

    public void setTamed(boolean tamed) {
        JReflection.executeMethod(entityTameableAnimalClass, (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17) ? "setTamed" : "w"), this.entity, new Class[] { boolean.class }, tamed);
    }

    public void setOwnerUUID(UUID uuid) {
        JReflection.executeUnspecificMethod(entityTameableAnimalClass, new Class[] { UUID.class }, this.entity, null, uuid);
    }

    public UUID getOwnerUUID() {
        return JReflection.executeUnspecificMethod(entityTameableAnimalClass, new Class[] {}, this.entity, UUID.class);
    }

    public boolean isSitting() {
        return (boolean) JReflection.executeMethod(entityTameableAnimalClass, (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17) ? "isSitting" : "fy"), this.entity, new Class[] {});
    }

    public void setSitting(boolean sitting) {
        JReflection.executeMethod(entityTameableAnimalClass, (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17) ? "setSitting" : "x"), this.entity, new Class[] { boolean.class }, sitting);
    }
}
