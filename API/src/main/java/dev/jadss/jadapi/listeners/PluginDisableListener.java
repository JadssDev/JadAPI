package dev.jadss.jadapi.listeners;

import dev.jadss.jadapi.JadAPIPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class PluginDisableListener implements Listener {

    @EventHandler
    public void onDisable(PluginDisableEvent e) {
        if (!(e.getPlugin() instanceof JavaPlugin))
            return;

        JavaPlugin plugin = (JavaPlugin) e.getPlugin();
        JadAPIPlugin jPluginInstance = JadAPIPlugin.get(plugin.getClass());

        if (jPluginInstance == null)
            return;

        new ArrayList<>(jPluginInstance.getQuickEvents()).forEach(q -> q.register(false));
        new ArrayList<>(jPluginInstance.getPacketHooks()).forEach(h -> h.register(false));
        jPluginInstance.register(false);
    }
}
