package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.JMappings;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

import java.util.UUID;

public abstract class EntityTameableAnimal extends EntityAnimal {

    public static final Class<?> ENTITY_TAMEABLE_ANIMAL = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.animal" : "server." + NMS.getNMSVersion()) + ".EntityTameableAnimal");

    public EntityTameableAnimal(Object entity) {
        super(entity);

        if (!isEntityTameableAnimal(entity))
            throwExceptionNotClass("Entity Tameable Animal");
    }

    public boolean isEntityTameableAnimal(Object entity) {
        return ENTITY_TAMEABLE_ANIMAL.isAssignableFrom(entity.getClass());
    }

    //Custom methods.

    public static final JMappings IS_TAMED_METHOD = JMappings.create(ENTITY_TAMEABLE_ANIMAL)
            .add(JVersion.v1_7, "isTamed")
            .add(JVersion.v1_18, "q")
            .finish();

    public boolean isTamed() {
        return (boolean) JMethodReflector.executeMethod(ENTITY_TAMEABLE_ANIMAL, IS_TAMED_METHOD.get(), new Class[]{}, this.entity, null);
    }


    public static final JMappings SET_TAMED_METHOD = JMappings.create(ENTITY_TAMEABLE_ANIMAL)
            .add(JVersion.v1_7, "setTamed")
            .add(JVersion.v1_18, "w")
            .finish();

    public void setTamed(boolean tamed) {
        JMethodReflector.executeMethod(ENTITY_TAMEABLE_ANIMAL, SET_TAMED_METHOD.get(), new Class[]{boolean.class}, this.entity, new Object[]{tamed});
    }


    public static final JMappings GET_OWNER_UUID_METHOD = JMappings.create(ENTITY_TAMEABLE_ANIMAL)
            .add(JVersion.v1_7, "getOwnerUUID")
            .add(JVersion.v1_18, "d")
            .finish();

    public UUID getOwnerUUID() {
        Object uuid = JMethodReflector.executeMethod(ENTITY_TAMEABLE_ANIMAL, GET_OWNER_UUID_METHOD.get(), new Class[]{}, this.entity, null);
        return (uuid instanceof UUID ? (UUID) uuid : UUID.fromString((String) uuid));
    }


    public static final JMappings SET_OWNER_UUID_METHOD = JMappings.create(ENTITY_TAMEABLE_ANIMAL)
            .add(JVersion.v1_7, "setOwnerUUID")
            .add(JVersion.v1_18, "b")
            .finish();

    public void setOwnerUUID(UUID uuid) {
        boolean outdated = JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8);
        JMethodReflector.executeMethod(ENTITY_TAMEABLE_ANIMAL, SET_OWNER_UUID_METHOD.get(), outdated ? new Class[]{String.class} : new Class[]{UUID.class}, this.entity, new Object[]{outdated ? uuid.toString() : uuid});
    }


    public static final JMappings IS_SITTING_METHOD = JMappings.create(ENTITY_TAMEABLE_ANIMAL)
            .add(JVersion.v1_7, "isSitting")
            .add(JVersion.v1_18, "fz")
            .add(JVersion.v1_19, "fK")
            .finish();

    public boolean isSitting() {
        return (boolean) JMethodReflector.executeMethod(ENTITY_TAMEABLE_ANIMAL, IS_SITTING_METHOD.get(), new Class[]{}, this.entity, null);
    }


    public static final JMappings SET_SITTING_METHOD = JMappings.create(ENTITY_TAMEABLE_ANIMAL)
            .add(JVersion.v1_7, "setSitting")
            .add(JVersion.v1_18, "x")
            .finish();

    public void setSitting(boolean sitting) {
        JMethodReflector.executeMethod(ENTITY_TAMEABLE_ANIMAL, SET_SITTING_METHOD.get(), new Class[]{boolean.class}, this.entity, new Object[]{sitting});
    }
}
