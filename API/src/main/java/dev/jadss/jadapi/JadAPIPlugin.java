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

public abstract class JadAPIPlugin {

    private static final List<JadAPIPlugin> PLUGINS = new ArrayList<>();

    public static <J extends JavaPlugin> JadAPIPlugin get(Class<J> clazz) {
        for (JadAPIPlugin pl : new ArrayList<>(PLUGINS)) if (pl.getJavaPlugin().getClass().equals(clazz)) return pl;
        return null;
    }

    private final List<JQuickEvent> quickEvents = new ArrayList<>();
    public List<JQuickEvent> getQuickEvents() {
        return quickEvents;
    }

    private final List<JPacketHook> packetHooks = new ArrayList<>();
    public List<JPacketHook> getPacketHooks() {
        return packetHooks;
    }

    private final List<EnchantmentInstance> enchantments = new ArrayList<>();
    public List<EnchantmentInstance> getEnchantments() {
        return enchantments;
    }

    private final List<JShapedCraft> crafts = new ArrayList<>();
    public List<JShapedCraft> getCrafts() {
        return crafts;
    }

    public abstract JavaPlugin getJavaPlugin();

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

    public boolean isRegistered() {
        return PLUGINS.contains(this);
    }
}
