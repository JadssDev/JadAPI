package dev.jadss.jadapi.listeners;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.misc.JHologram;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        for (JHologram holo : JadAPI.getInstance().getInformationManager().getHolograms()) {
            holo.getPlayersShown().remove(new JPlayer(e.getPlayer()));
        }

        JadAPI.getInstance().getSigns().remove(e.getPlayer().getUniqueId());

        new JPlayer(e.getPlayer()).setMeta("", "", ChatColor.WHITE, "zz");
        new JPlayer(e.getPlayer()).resetPlayerDataValues();

    }
}
