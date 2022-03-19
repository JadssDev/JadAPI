package dev.jadss.jadapi.listeners;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.misc.JHologram;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayerWorldChangeListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        for (JHologram hologram : JadAPI.getInstance().getInformationManager().getHolograms().stream()
                .filter(hologram -> hologram.getLocation().getWorld().equals(e.getPlayer().getWorld()))
                .collect(Collectors.toCollection(ArrayList::new)))
            if (hologram.doAutoAdd())
                hologram.show(new JPlayer(e.getPlayer()));

        for (JHologram hologram : JadAPI.getInstance().getInformationManager().getHolograms().stream()
                .filter(hologram -> hologram.getLocation().getWorld().equals(e.getFrom()))
                .collect(Collectors.toCollection(ArrayList::new)))
            hologram.hide(new JPlayer(e.getPlayer()));
    }
}
