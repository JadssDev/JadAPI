package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.JReflection;

import java.util.UUID;

public abstract class EntityTameableAnimal extends EntityAnimal {

    public final static Class<?> entityTameableAnimalClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.animal" : "server." + JReflection.getNMSVersion()) + ".EntityTameableAnimal");
    ;

    public EntityTameableAnimal(Object nmsEntityTameableAnimal) {
        super(nmsEntityTameableAnimal);
        if (!isEntityTameableAnimal(nmsEntityTameableAnimal)) throwExceptionNotClass("Entity Tameable Animal");
    }

    public boolean isEntityTameableAnimal(Object entity) {
        return entityTameableAnimalClass.isAssignableFrom(entity.getClass());
    }

    //Custom methods.

    public boolean isTamed() {
        return JReflection.executeMethod(entityTameableAnimalClass, new Class[]{}, this.entity, boolean.class, (i) -> 0);
    }

    public void setTamed(boolean tamed) {
        JReflection.executeMethod(entityTameableAnimalClass, new Class[]{boolean.class}, this.entity, null, (i) -> 0, tamed);
    }

    public void setOwnerUUID(UUID uuid) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
            JReflection.executeMethod(entityTameableAnimalClass, new Class[]{String.class}, this.entity, null, (i) -> 0, uuid.toString());
        } else {
            JReflection.executeMethod(entityTameableAnimalClass, new Class[]{UUID.class}, this.entity, null, (i) -> 0, uuid);
        }
    }

    public UUID getOwnerUUID() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
            return UUID.fromString(JReflection.executeMethod(entityTameableAnimalClass, new Class[]{}, this.entity, String.class, (i) -> 0));
        } else {
            return JReflection.executeMethod(entityTameableAnimalClass, new Class[]{}, this.entity, UUID.class, (i) -> 0);
        }
    }

    public boolean isSitting() {
        return JReflection.executeMethod(entityTameableAnimalClass, new Class[]{}, this.entity, boolean.class, (i) -> i);
    }

    public void setSitting(boolean sitting) {
        JReflection.executeMethod(entityTameableAnimalClass, new Class[]{boolean.class}, this.entity, null, (i) -> i, sitting);
    }
}
