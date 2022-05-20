package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.misc.JWorld;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.utils.JReflection;

import java.util.UUID;

public abstract class Entity implements NMSObject, NMSManipulable {

    public static final Class<?> entityClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".Entity");
    public static final Class<?> dataWatcherClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.syncher" : "server." + JReflection.getNMSVersion()) + ".DataWatcher");

    //todo:
    //Cool ideas maybe:
    // - Add invulnerability flag.

    protected final Object entity;

    public Entity(Object nmsEntity) {
        if (nmsEntity == null) throw new NMSException("The entity may not be null!");
        if (!isEntityObject(nmsEntity)) throwExceptionNotClass("Entity");

        this.entity = nmsEntity;
    }

    public static boolean isEntityObject(Object entity) {
        return entityClass.isAssignableFrom(entity.getClass());
    }


    //Custom methods.

    public int getId() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17)) {
            return (int) JReflection.executeMethod(entityClass, "getId", this.entity, new Class[]{});
        } else { // No way of using some type of genius to get it except use the actual method name! Unfortunate.
            return (int) JReflection.executeMethod(entityClass, "ae", this.entity, new Class[]{});
        }
    }

    public Object getDataWatcher() {
        return JReflection.getFieldObject(entityClass, dataWatcherClass, this.entity);
    }

    public IChatBaseComponent getCustomName() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This method is not available on 1.7");

        IChatBaseComponent component = new IChatBaseComponent();
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            component.setMessage(JReflection.executeMethod(entityClass, new Class[]{}, this.entity, String.class, (i) -> i));
            component.setJsonMessage(IChatBaseComponent.defaultSimpleFormat.replace("%text%", component.getMessage()));
            component.setJsonMode(true);
        } else {
            component.parse(JReflection.executeMethod(entityClass, new Class[]{}, this.entity, IChatBaseComponent.iChatBaseComponentClass, (i) -> i));
        }
        return component;
    }

    public void setCustomName(IChatBaseComponent component) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This method is not available on 1.7");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            JReflection.executeMethod(entityClass, new Class[]{String.class}, this.entity, null, (i) -> i, component.getMessage());
        } else {
            JReflection.executeMethod(entityClass, new Class[]{IChatBaseComponent.iChatBaseComponentClass}, this.entity, null, (i) -> i, component.build());
        }
    }

    public boolean isCustomNameVisible() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This method is not available on 1.7");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17)) {
            return (boolean) JReflection.executeMethod(entityClass, "getCustomNameVisible", this.entity, new Class[]{});
        } else {
            //There's no good way to check such name with unspecific methods, so this is the OnLy WAY.
            return (boolean) JReflection.executeMethod(entityClass, "cr", this.entity, new Class[]{});
        }

    }

    public void setCustomNameVisible(boolean visible) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This method is not available on 1.7");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17)) {
            JReflection.executeMethod(entityClass, "setCustomNameVisible", this.entity, new Class[]{boolean.class}, visible);
        } else {
            //There's no good way to check such name with unspecific methods, so this is the OnLy WAY.
            JReflection.executeMethod(entityClass, "n", this.entity, new Class[]{boolean.class}, visible);
        }
    }

    public boolean isInvisible() {
        return this.isFlagged(5);
    }

    public void setInvisible(boolean invisible) {
        if (this.isInvisible() == invisible)
            return;

        this.setFlagged(5, invisible);
    }

    public void setNoGravity(boolean gravity) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_9)) {
            throw new NMSException("You cannot use this method in versions below 1.9!");
        } else {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17)) {
                JReflection.executeMethod(entityClass, "setNoGravity", this.entity, new Class[]{boolean.class}, gravity);
            } else {
                JReflection.executeMethod(entityClass, "e", this.entity, new Class[]{boolean.class}, gravity);
            }
        }
    }

    public boolean isNoGravity() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_9)) {
            throw new NMSException("You cannot use this method in versions below 1.9!");
        } else {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17)) {
                return (boolean) JReflection.executeMethod(entityClass, "isNoGravity", this.entity, new Class[]{});
            } else {
                return (boolean) JReflection.executeMethod(entityClass, "aM", this.entity, new Class[]{});
            }
        }
    }

    /**
     * this is advanced and should not be used unless you know what you are doing
     *
     * @param flag the flag to check.
     * @return if the flag is set to true or false.
     */
    public boolean isFlagged(int flag) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
            return (boolean) JReflection.executeMethod(entityClass, "g", this.entity, new Class[]{int.class}, flag);
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17)) {
            return (boolean) JReflection.executeMethod(entityClass, "getFlag", this.entity, new Class[]{int.class}, flag);
        } else { //Basically 1.18
            return (boolean) JReflection.executeMethod(entityClass, "h", this.entity, new Class[]{int.class}, flag);
        }
    }

    /**
     * this is advanced and should not be used unless you know what you are doing
     *
     * @param flag  the flag to change.
     * @param value the value to set the flag to.
     */
    public void setFlagged(int flag, boolean value) {
        JReflection.executeMethod(entityClass, new Class[]{int.class, boolean.class}, this.entity, null, (i) -> (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17) ? i : 0), flag, value);
    }

    public UUID getUniqueID() {
        return (UUID) JReflection.executeMethod(entityClass, "getUniqueID", this.entity, new Class[]{});
    }

    public org.bukkit.entity.Entity getBukkitEntity() {
        int thisEntityID = this.getId();
        for (JWorld world : JWorld.getJWorlds())
            for (org.bukkit.entity.Entity entities : world.getWorld().getEntities())
                if (entities.getEntityId() == thisEntityID)
                    return entities;
        return null;
    }

    public Object getHandle() {
        return entity;
    }

    protected void throwExceptionNotClass(String clazz) {
        throw new NMSException("This is not a NMS " + clazz + "!");
    }

    protected void throwExceptionUnsupported() {
        throw new NMSException("This server does not contain this entity at their registry.");
    }
}
