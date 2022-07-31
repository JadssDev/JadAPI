package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.misc.JWorld;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.utils.reflection.JMappings;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

import java.util.UUID;

public abstract class Entity implements NMSObject, NMSManipulable {

    public static final Class<?> ENTITY_CLASS = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + NMS.getNMSVersion()) + ".Entity");
    public static final Class<?> DATA_WATCHER_CLASS = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.syncher" : "server." + NMS.getNMSVersion()) + ".DataWatcher");

    //todo:
    //Cool ideas maybe:
    // - Add invulnerability flag.

    protected final Object entity;

    public Entity(Object entity) {
        if (entity == null)
            throw new NMSException("The entity may not be null!");
        if (!isEntityObject(entity))
            throwExceptionNotClass("Entity");

        this.entity = entity;
    }

    public static boolean isEntityObject(Object object) {
        return ENTITY_CLASS.isAssignableFrom(object.getClass());
    }


    //Custom methods.

    public static final JMappings ID_METHOD = JMappings.create(Entity.ENTITY_CLASS)
            .add(JVersion.v1_7, "getId")
            .add(JVersion.v1_18, "ae")
            .finish();

    public int getEntityId() {
        return (int) JMethodReflector.executeMethod(ENTITY_CLASS, ID_METHOD.get(), this.entity, null);
    }

    public Object getDataWatcher() {
        return JFieldReflector.getObjectFromUnspecificField(ENTITY_CLASS, DATA_WATCHER_CLASS, this.entity);
    }

    public static final JMappings GET_CUSTOM_NAME_METHOD = JMappings.create(Entity.ENTITY_CLASS)
            .add(JVersion.v1_8, "getCustomName")
            .add(JVersion.v1_18, "Z")
            .finish();

    public IChatBaseComponent getCustomName() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This method is not available on 1.7");

        IChatBaseComponent component = new IChatBaseComponent();
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            component.setMessage((String) JMethodReflector.executeMethod(ENTITY_CLASS, GET_CUSTOM_NAME_METHOD.get(), this.entity, null));
            component.setJsonMessage(IChatBaseComponent.defaultSimpleFormat.replace("%text%", component.getMessage()));
            component.setJsonMode(false);
        } else {
            component.parse(JMethodReflector.executeMethod(ENTITY_CLASS, GET_CUSTOM_NAME_METHOD.get(), this.entity, null));
        }
        return component;
    }

    public static final JMappings SET_CUSTOM_NAME_METHOD = JMappings.create(Entity.ENTITY_CLASS)
            .add(JVersion.v1_8, "setCustomName")
            .add(JVersion.v1_18, "a")
            .add(JVersion.v1_19, "b")
            .finish();

    public void setCustomName(IChatBaseComponent component) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This method is not available on 1.7");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            JMethodReflector.executeMethod(ENTITY_CLASS, SET_CUSTOM_NAME_METHOD.get(), new Class[]{String.class}, this.entity, new Object[]{component.getMessage()});
        } else {
            JMethodReflector.executeMethod(ENTITY_CLASS, SET_CUSTOM_NAME_METHOD.get(), new Class[]{IChatBaseComponent.iChatBaseComponentClass}, this.entity, new Object[]{component.build()});
        }
    }

    public static final JMappings IS_CUSTOM_NAME_VISIBLE_METHOD = JMappings.create(Entity.ENTITY_CLASS)
            .add(JVersion.v1_8, "getCustomNameVisible")
            .add(JVersion.v1_18, "cr")
            .add(JVersion.v1_19, "cu")
            .finish();

    public boolean isCustomNameVisible() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This method is not available on 1.7");

        return (boolean) JMethodReflector.executeMethod(ENTITY_CLASS, IS_CUSTOM_NAME_VISIBLE_METHOD.get(), this.entity, null);
    }

    public static final JMappings SET_CUSTOM_NAME_VISIBLE_METHOD = JMappings.create(Entity.ENTITY_CLASS)
            .add(JVersion.v1_8, "setCustomNameVisible")
            .add(JVersion.v1_18, "n")
            .finish();

    public void setCustomNameVisible(boolean visible) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This method is not available on 1.7");

        JMethodReflector.executeMethod(ENTITY_CLASS, SET_CUSTOM_NAME_VISIBLE_METHOD.get(), new Class[]{boolean.class}, this.entity, new Object[]{visible});
    }

    public boolean isInvisible() {
        return this.isFlagged(5);
    }

    public void setInvisible(boolean invisible) {
        if (this.isInvisible() == invisible)
            return;

        this.setFlagged(5, invisible);
    }

    public static final JMappings SET_NO_GRAVITY_METHOD = JMappings.create(Entity.ENTITY_CLASS)
            .add(JVersion.v1_9, "setNoGravity")
            .add(JVersion.v1_18, "e")
            .finish();

    public void setNoGravity(boolean noGravity) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_9)) {
            throw new NMSException("You cannot use this method in versions below 1.9!");
        } else {
            JMethodReflector.executeMethod(ENTITY_CLASS, SET_NO_GRAVITY_METHOD.get(), new Class[]{boolean.class}, this.entity, new Object[]{noGravity});
        }
    }

    public static final JMappings IS_NO_GRAVITY_METHOD = JMappings.create(Entity.ENTITY_CLASS)
            .add(JVersion.v1_9, "isNoGravity")
            .add(JVersion.v1_18, "aM")
            .add(JVersion.v1_19, "aN")
            .finish();

    public boolean isNoGravity() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_9)) {
            throw new NMSException("You cannot use this method in versions below 1.9!");
        } else {
            return (boolean) JMethodReflector.executeMethod(ENTITY_CLASS, IS_NO_GRAVITY_METHOD.get(), new Class[]{}, this.entity, null);
        }
    }

    public static final JMappings IS_FLAGGED_METHOD = JMappings.create(ENTITY_CLASS)
            .add(JVersion.v1_7, "g")
            .add(JVersion.v1_9, "getFlag")
            .add(JVersion.v1_18, "h")
            .finish();

    /**
     * this is advanced and should not be used unless you know what you are doing
     *
     * @param flag the flag to check.
     * @return if the flag is set to true or false.
     */
    public boolean isFlagged(int flag) {
        return (boolean) JMethodReflector.executeMethod(ENTITY_CLASS, IS_FLAGGED_METHOD.get(), new Class[]{int.class}, this.entity, new Object[]{flag});
    }

    public static final JMappings SET_FLAGGED_METHOD = JMappings.create(ENTITY_CLASS)
            .add(JVersion.v1_7, "a")
            .add(JVersion.v1_8, "b")
            .add(JVersion.v1_9, "setFlag")
            .add(JVersion.v1_18, "b")
            .finish();

    /**
     * this is advanced and should not be used unless you know what you are doing
     *
     * @param flag  the flag to change.
     * @param value the value to set the flag to.
     */
    public void setFlagged(int flag, boolean value) {
        JMethodReflector.executeMethod(ENTITY_CLASS, SET_FLAGGED_METHOD.get(), new Class[]{int.class, boolean.class}, this.entity, new Object[]{flag, value});
    }

    public static final JMappings UNIQUE_ID_METHOD = JMappings.create(ENTITY_CLASS)
            .add(JVersion.v1_7, "getUniqueID")
            .add(JVersion.v1_18, "cm")
            .add(JVersion.v1_19, "cp")
            .finish();

    public UUID getUniqueID() { //1.18 - cm //1.19 - cp
        return (UUID) JMethodReflector.executeMethod(ENTITY_CLASS, UNIQUE_ID_METHOD.get(), this.entity, null);
    }

    public org.bukkit.entity.Entity getBukkitEntity() {
        int thisEntityID = this.getEntityId();
        for (JWorld world : JWorld.getJWorlds())
            for (org.bukkit.entity.Entity entities : world.getWorld().getEntities())
                if (entities.getEntityId() == thisEntityID)
                    return entities;
        return null;
    }

    public Object getHandle() {
        return entity;
    }

    protected static void throwExceptionNotClass(String clazz) {
        throw new NMSException("This is not a NMS " + clazz + "!");
    }

    protected static void throwExceptionUnsupported() {
        throw new NMSException("This server does not contain this entity at their registry.");
    }
}
