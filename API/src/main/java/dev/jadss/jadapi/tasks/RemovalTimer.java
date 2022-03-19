package dev.jadss.jadapi.tasks;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.management.JPacketHook;
import dev.jadss.jadapi.management.JQuickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class RemovalTimer extends BukkitRunnable {


    @Override
    public void run() {
        for (JQuickEvent quickEvent : new ArrayList<>(JadAPI.getInstance().getInformationManager().getQuickEvents())) {
            if (quickEvent.ticksToUnregister == -1) continue;
            if (quickEvent.ticksToUnregister <= 0) {
                quickEvent.register(false);
                if (JadAPI.getInstance().getDebug().doQuickEventsDebug())
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cRemoved &ea &3&lQuickEvent&e of &a" + quickEvent.getProviderPlugin().getJavaPlugin().getName() + "&e!"));
                return;
            }
            quickEvent.ticksToUnregister--;
        }

        for (JPacketHook packetHook : new ArrayList<>(JadAPI.getInstance().getInformationManager().getPacketHooks())) {
            if (packetHook.ticksToUnregister == -1) continue;
            if (packetHook.ticksToUnregister <= 0) {
                packetHook.register(false);
                if (JadAPI.getInstance().getDebug().doPacketHooksDebug())
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cRemoved &ea &3&lPacketHook &e of &a" + packetHook.getProviderPlugin().getJavaPlugin().getName() + "&e!"));
                return;
            }
            packetHook.ticksToUnregister--;
        }
    }

}
