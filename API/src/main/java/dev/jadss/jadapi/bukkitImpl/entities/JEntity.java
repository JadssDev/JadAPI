package dev.jadss.jadapi.bukkitImpl.entities;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import dev.jadss.jadapi.bukkitImpl.misc.JWorld;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

/**
 * A JEntity is like a Bukkit entity but with <b>More methods</b>!
 * <h1>Note: </h1> <b>- JEntity cannot be created using a player</b>
 */
public class JEntity {

    protected final Entity entity;

    /**
     * Create a JEntity with an already <b>existing entity</b>.
     * @param entity The already existing entity.
     * @throws JException if entity is null.
     */
    public JEntity(Entity entity) {
        if(entity == null) throw new JException(JException.Reason.ENTITY_IS_NULL);

        if(entity.getType() == EntityType.PLAYER) throw new JException(JException.Reason.CANNOT_BE_PLAYER);

        this.entity = entity;
    }

    /**
     * Create an <b>entity</b> on a specific location on a world.
     * @param entityType What type of entity should we spawn.
     * @param location The location to spawn.
     */
    public JEntity(EntityType entityType, Location location) {
        if(entityType == null) throw new JException(JException.Reason.VALUE_IS_NULL);
        if(location == null) throw new JException(JException.Reason.LOCATION_IS_NULL);

        if(entityType == EntityType.PLAYER) throw new JException(JException.Reason.CANNOT_BE_PLAYER);

        this.entity = location.getWorld().spawnEntity(location, entityType);
    }


    /**
     * Set the <b>display name</b> of this <b>entity</b>.
     * <h1>Note: </h1> <b>- Defining null as displayName will disable the Display Name.</b>
     * @param displayName The display name to edit to.
     * @return itself.
     */
    public JEntity setDisplayName(String displayName) {
        if (displayName != null) {
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', displayName));
            entity.setCustomNameVisible(true);
        } else {
            entity.setCustomNameVisible(false);
            entity.setCustomName(null);
        }
        return this;
    }

    /**
     * Set the <b>Artificial Intelligence</b>.
     * @param noAI if we should disable the <b>Artificial Intelligence</b> of the entity.
     * @return itself.
     */
    public JEntity setNoAI(boolean noAI) {
        if (noAI) {
            NBTEntity nbt = new NBTEntity(entity);
            nbt.setByte("NoAI", (byte) 1);
        } else {
            NBTEntity nbt = new NBTEntity(entity);
            nbt.setByte("NoAI", (byte) 0);
        }
        return this;
    }

    /**
     * Do you want to disable the AI of this entity?
     * @param noAI eslaf ro eurt?
     * @return itself.
     * @deprecated Please use {@link JEntity#setNoAI(boolean)} =)
     */
    @Deprecated
    public JEntity disableAI(boolean noAI) {
        return setNoAI(noAI);
    }

    /**
     * Teleport this entity to <b>another location</b>.
     * @param location The location to teleport to.
     * @return itself.
     * @throws JException if location is null.
     */
    public JEntity teleport(Location location) {
        if(location == null) throw new JException(JException.Reason.LOCATION_IS_NULL);

        entity.teleport(location);
        return this;
    }

    /**
     * Get the type of this entity.
     * @return A {@link EntityType} with the entity type.
     */
    public EntityType getEntityType() { return this.entity.getType(); }

    /**
     * Check if this entity is <b>Alive</b>!
     * @return if the entity is alive.
     */
    public boolean isAlive() { return !this.entity.isDead(); }

    /**
     * Get the Unique Identifier of this entity.
     * @return the Unique ID as {@link UUID}!
     */
    public UUID getUniqueID() { return entity.getUniqueId(); }

    /**
     * Get the bukkit entity of this entity.
     * @return Bukkit's entity object.
     */
    public Entity getEntity() { return entity; }


    /**
     * Remove this entity from existing.
     */
    public void despawn() { entity.remove(); }

    /**
     * Remove this entity from existing.
     */
    public void remove() { despawn(); }

    //STATICS

    /**
     * Get an entity by its Bukkit ID.
     * @param entityID the Bukkit's entity id.
     * @return the Entity as {@link JEntity}.
     */
    public static JEntity getEntity(int entityID) {
        for(JWorld world : JWorld.getJWorlds())
            for(Entity entity : world.getWorld().getEntities())
                if(entity.getEntityId() == entityID)
                    return new JEntity(entity);
        return null;
    }
}
