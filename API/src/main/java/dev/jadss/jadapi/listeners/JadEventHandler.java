package dev.jadss.jadapi.listeners;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.management.JQuickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;

import java.util.ArrayList;

public class JadEventHandler implements Listener {

    @EventHandler //Quite worthless but why not?
    public void onEvent(Event event, EventPriority priority) {
        try {
            if(priority == EventPriority.LOWEST) {
                JadAPI.getInstance().getInformationManager().addEventCall(event.getClass().getSimpleName());
                if(JadAPI.getInstance().getDebug().doEventsDebug()) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eAn &3&lEvent &ewas &b&lcalled &e-> &b" + event.getClass().getName() + " " + debugOptions(event)));
                }
            }

            JadAPI.getInstance().getInformationManager().getQuickEvents().forEach(quickEvent -> {
                if (quickEvent.getPriority() != priority)
                    return;
                if (quickEvent.getEventType() == JQuickEvent.ALL_EVENTS || quickEvent.getEventType().isInstance(event)) {
                    //Is instance.
                    if (quickEvent.check(event))
                        quickEvent.run(event);
                }
            });
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3&lJadEventHandler &eExploded and has stopped working! Please contact support."));
            ex.printStackTrace();
        }
    }

    private String debugOptions(Event event) {
        if(event instanceof BlockEvent) {
            BlockEvent blockEvent = (BlockEvent) event;
            return "(" + "x->" + blockEvent.getBlock().getX() + ";y->" + blockEvent.getBlock().getY() + ";z->" + blockEvent.getBlock().getZ() +
                    (blockEvent instanceof BlockPlaceEvent ? ";player->" + ((BlockPlaceEvent)blockEvent).getPlayer().getName() : blockEvent instanceof BlockBreakEvent ? ";player->" + ((BlockBreakEvent)blockEvent).getPlayer().getName() : "")
                    + ")";
        } else if(event instanceof PlayerEvent) {
            return "(" + "player->" + ((PlayerEvent) event).getPlayer().getName() + ")";
        } else return "";
    }
}
