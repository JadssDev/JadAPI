package dev.jadss.jadapi.tasks;

import dev.jadss.jadapi.JadAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitRunnable;

public class EventHandlersUpdater extends BukkitRunnable {

    @Override
    public void run() {
        for (HandlerList handler : HandlerList.getHandlerLists()) { // for every event.
            for (EventPriority priority : JadAPI.getInstance().getQuickEventHandlers().keySet()) { // for every priority JadAPI has.
                RegisteredListener listener = getListener(priority);
                if (!hasHandler(handler.getRegisteredListeners(), listener)) {
                    log(handler);
                    handler.register(getListener(priority));
                }
            }
        }
    }

    private RegisteredListener getListener(EventPriority priority) {
        return JadAPI.getInstance().getQuickEventHandlers().get(priority);
    }

    public boolean hasHandler(RegisteredListener[] listeners, RegisteredListener jadListener) {
        for (RegisteredListener registeredListener : listeners)
            if (registeredListener.equals(jadListener))
                return true;
        return false;
    }

//    public boolean isBlockedHandler(HandlerList handler) {
//        if(PlayerMoveEvent.getHandlerList().equals(handler)) return true;
//        return false;
//    }

    public void log(HandlerList handler) {
        if (JadAPI.getInstance().getDebug().doEventsDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7> &eRegistering an &3&lEvent&e!"));
//        if(PlayerJoinEvent.getHandlerList().equals(handler)) Bukkit.getConsoleSender().sendMessage("It's registered.");
    }

}
