package dev.jadss.jadapi.utils.reflection.reflectors;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import org.bukkit.Bukkit;

public class ReflectionUtils {

    protected static final JSender CONSOLE = new JSender(Bukkit.getConsoleSender());

    protected static boolean isDebugEnabled() {
        return JadAPI.getInstance().getDebug().doReflectionDebug();
    }
}
