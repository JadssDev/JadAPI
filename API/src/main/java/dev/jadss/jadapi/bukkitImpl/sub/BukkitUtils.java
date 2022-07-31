package dev.jadss.jadapi.bukkitImpl.sub;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import org.bukkit.plugin.Plugin;

/**
 * Some utilities for bukkit to create compatibility within different versions.
 */
public class BukkitUtils {

    private BukkitUtils() {
        //Utility class
    }

    /**
     * Create a Namespaced Key.
     * @param plugin the plugin that is registering the key
     * @param key the key
     * @return the Namespaced Key object in bukkit.
     */
    public static Object buildNamespacedKey(Plugin plugin, String key) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11))
            throw new RuntimeException("NamespacedKeys in bukkit where not created in versions below 1.11");
        return JConstructorReflector.executeConstructor(JClassReflector.getClass("org.bukkit.NamespacedKey"), new Class[] { Plugin.class, String.class }, plugin, key);
    }
}
