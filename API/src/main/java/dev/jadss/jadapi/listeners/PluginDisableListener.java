package dev.jadss.jadapi.listeners;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.management.JPacketHook;
import dev.jadss.jadapi.management.JQuickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class PluginDisableListener implements Listener {

    @EventHandler
    public void onDisable(PluginDisableEvent e) {
        if(JadAPIPlugin.get((Class<JavaPlugin>) e.getPlugin().getClass()) != null) {
            JadAPIPlugin plugin = JadAPIPlugin.get((Class<JavaPlugin>) e.getPlugin().getClass());
            for(JQuickEvent jEvent : new ArrayList<>(plugin.getQuickEvents())) jEvent.register(false);
            for(JPacketHook jHook : new ArrayList<>(plugin.getPacketHooks())) jHook.register(false);
            plugin.register(false);
        }
    }
}
