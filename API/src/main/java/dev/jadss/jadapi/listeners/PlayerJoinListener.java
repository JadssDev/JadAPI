package dev.jadss.jadapi.listeners;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.misc.JHologram;
import dev.jadss.jadapi.bukkitImpl.misc.JSkin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        for (JHologram hologram : JadAPI.getInstance().getInformationManager().getHolograms()) {
            if (hologram.doAutoAdd()) {
                hologram.getPlayersShown().remove(new JPlayer(e.getPlayer()));
                hologram.show(new JPlayer(e.getPlayer()));
            }
        }
        ;

        JPlayer player = new JPlayer(e.getPlayer());
        JSkin.processSkin(player.getPlayer().getName()).thenAccept(skin -> {
            if (skin != null)
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3&lSkin &eof &b" + player.getPlayer().getName() + " &efound!"));
            else
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCouldn't process &3&lskin &efor &b" + player.getPlayer().getName() + "&e, probably &3&lCracked Player&e?"));
        });
    }
}
