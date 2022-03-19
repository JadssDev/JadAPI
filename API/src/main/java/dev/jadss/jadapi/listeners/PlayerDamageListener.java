package dev.jadss.jadapi.listeners;

import dev.jadss.jadapi.JadAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (JadAPI.getInstance().getIgnoreHits().containsKey(player) &&
                    JadAPI.getInstance().getIgnoreHits().get(player) == null ||
                    JadAPI.getInstance().getIgnoreHits().get(player) == e.getCause()) {

                e.setCancelled(true);
                JadAPI.getInstance().getIgnoreHits().remove(player);

            }
        }
    }
}
