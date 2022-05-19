package dev.jadss.jadapi;

import dev.jadss.jadapi.bukkitImpl.enchantments.EnchantmentInstance;
import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import dev.jadss.jadapi.bukkitImpl.misc.JShapedCraft;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.management.JPacketHook;
import dev.jadss.jadapi.management.JQuickEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the instance of a plugin in the JadAPI.
 */
public abstract class JadAPIPlugin {

    private static final List<JadAPIPlugin> PLUGINS = new ArrayList<>();

    /**
     * Get the size of the amount of plugins registered to JadAPI.
     * @return The amount of plugins registered to JadAPI.
     */
    public static int size() {
        return PLUGINS.size();
    }

    /**
     * Get a plugin instance by its JavaPlugin class.
     * @param clazz The JavaPlugin class.
     * @param <J> The JavaPlugin class.
     * @return The plugin instance.
     */
    public static <J extends JavaPlugin> JadAPIPlugin get(Class<J> clazz) {
        for (JadAPIPlugin pl : new ArrayList<>(PLUGINS))
            if (pl.getJavaPlugin() == null) {
                PLUGINS.remove(pl);
            } else if (pl.getJavaPlugin().getClass().equals(clazz)) {
                return pl;
            }
        return null;
    }

    private final List<JQuickEvent<?>> quickEvents = new ArrayList<>();
    private final List<JPacketHook> packetHooks = new ArrayList<>();
    private final List<EnchantmentInstance> enchantments = new ArrayList<>();
    private final List<JShapedCraft> crafts = new ArrayList<>();

    public List<JQuickEvent<?>> getQuickEvents() {
        return this.quickEvents;
    }

    public List<JPacketHook> getPacketHooks() {
        return this.packetHooks;
    }

    public List<EnchantmentInstance> getEnchantments() {
        return this.enchantments;
    }

    public List<JShapedCraft> getCrafts() {
        return this.crafts;
    }

    public abstract JavaPlugin getJavaPlugin();

    /**
     * Register this plugin to JadAPI.
     * @param register If true, register this plugin to JadAPI, if false, it will unregister it.
     */
    public void register(boolean register) {
        if (!JadAPI.getInstance().isEnabled()) throw new JException(JException.Reason.JADAPI_NOT_ENABLED);
        if (this.getJavaPlugin() == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        if (register) {
            if (JadAPIPlugin.PLUGINS.contains(this) || PLUGINS.
                    stream().
                    anyMatch(jadAPIPlugin -> jadAPIPlugin.getJavaPlugin().equals(this.getJavaPlugin())))
                throw new JException(JException.Reason.ALREADY_REGISTERED);

            new JSender(Bukkit.getConsoleSender()).sendMessage("&3&lJadAPI &7>> &3&lRegistered &a" + this.getJavaPlugin() + "'s &eJadAPI &bInstance&e!");
            JadAPIPlugin.PLUGINS.add(this);
        } else {
            new JSender(Bukkit.getConsoleSender()).sendMessage("&3&lJadAPI &7>> &3&lUnregistering &a" + this.getJavaPlugin() + "'s &eJadAPI &bInstance&e!");
            new JSender(Bukkit.getConsoleSender()).sendMessage("&eUnregistered " + this.quickEvents.size() + " &bQuickEvents&e!");
            new JSender(Bukkit.getConsoleSender()).sendMessage("&eUnregistered " + this.packetHooks.size() + " &bPacketHooks&e!");
            new JSender(Bukkit.getConsoleSender()).sendMessage("&eUnregistered " + this.enchantments.size() + " &bEnchantments&e!");

            for (JQuickEvent quickEvent : this.quickEvents)
                quickEvent.register(false);
            for (JPacketHook packetHook : this.packetHooks)
                packetHook.register(false);
            for (EnchantmentInstance enchantments : this.enchantments)
                enchantments.unregister();

            this.quickEvents.clear();
            this.packetHooks.clear();
            this.enchantments.clear();

            JadAPIPlugin.PLUGINS.remove(this);
            new JSender(Bukkit.getConsoleSender()).sendMessage("&3&lJadAPI &7>> &3&lUnregistered &a" + this.getJavaPlugin() + "'s &eJadAPI &bInstance&e!");
        }
    }

    /**
     * Check if this instance is registered to JadAPI.
     * @return True if this instance is registered to JadAPI, false otherwise.
     */
    public boolean isRegistered() {
        return PLUGINS.contains(this);
    }
}
