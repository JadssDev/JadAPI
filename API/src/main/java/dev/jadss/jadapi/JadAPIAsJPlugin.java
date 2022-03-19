package dev.jadss.jadapi;

import org.bukkit.plugin.java.JavaPlugin;

public class JadAPIAsJPlugin extends JadAPIPlugin {

    @Override
    public JavaPlugin getJavaPlugin() {
        return JadAPI.getInstance();
    }
}
