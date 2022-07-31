package dev.jadss.jadapi.bukkitImpl.misc;

import dev.jadss.jadapi.bukkitImpl.entities.JEntity;
import dev.jadss.jadapi.bukkitImpl.enums.JParticle;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a world in JadAPI!
 */
public class JWorld {

    private final World world;

    /**
     * Create a JWorld Object using a world.
     *
     * @param world the world.
     */
    public JWorld(World world) {
        if (world == null) throw new JException(JException.Reason.LOCATION_IS_NULL);

        this.world = world;
    }

    /**
     * Spawn an entity.
     *
     * @param location   the location to spawn
     * @param entityType the entity type.
     * @return the JEntity.
     */
    public JEntity spawnEntity(Location location, EntityType entityType) {
        if (!location.getWorld().equals(world)) throw new JException(JException.Reason.INVALID_CLASS);

        return new JEntity(world.spawnEntity(location, entityType));
    }

    /**
     * Spawn a particle in the world.
     *
     * @param location      the location to spawn.
     * @param particle      the JParticle.
     * @param speed         the speed of the particles (may be laggy)
     * @param particleCount the amount of particles.
     * @return itself.
     */
    public JWorld spawnParticle(Location location, JParticle particle, float speed, int particleCount) {
        Enum<?> parsedParticle = particle.parseParticle();
        if (parsedParticle == null) throw new JException(JException.Reason.INVALID_CLASS);

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
            JMethodReflector.executeMethod(World.Spigot.class, "playEffect",
                    //          Location        Effect        int        int        float        float        float        float        int        int
                    new Class[]{Location.class, Effect.class, int.class, int.class, float.class, float.class, float.class, float.class, int.class, int.class},
                    this.world.spigot(),
                    new Object[]{location, parsedParticle, 0, 0, 0, 0, 0, speed, particleCount, 300});
        } else {
            JMethodReflector.executeMethod(World.class, "spawnParticle",
                    new Class[]{JParticle.PARTICLE_CLASS, Location.class, int.class, double.class, double.class, double.class},
                    this.world,
                    new Object[]{parsedParticle, location, particleCount, 0, 0, 0});
        }

        return this;
    }

    /**
     * Get the name of this world!
     *
     * @return the Name of the world!
     */
    public String getName() {
        return world.getName();
    }

    /**
     * Gets the UUID of this world!
     *
     * @return UUID.
     */
    public UUID getUUID() {
        return world.getUID();
    }

    /**
     * Get the world initially set!
     *
     * @return the world.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets all the worlds currently loaded by bukkit!
     *
     * @return the worlds!
     */
    public static List<JWorld> getJWorlds() {
        return Bukkit.getWorlds().stream().map(JWorld::new).collect(Collectors.toCollection(ArrayList::new));
    }
}
