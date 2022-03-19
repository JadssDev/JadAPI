package dev.jadss.jadapi;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JHead;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import dev.jadss.jadapi.bukkitImpl.misc.JSkin;
import dev.jadss.jadapi.bukkitImpl.storage.JHeadStorage;
import dev.jadss.jadapi.bukkitImpl.sub.JSignRegister;
import dev.jadss.jadapi.commands.JadAPICommand;
import dev.jadss.jadapi.interfaces.managing.JInformationManager;
import dev.jadss.jadapi.interfaces.managing.JManagement;
import dev.jadss.jadapi.interfaces.managing.JPacketHooker;
import dev.jadss.jadapi.interfaces.managing.JRegisterer;
import dev.jadss.jadapi.listeners.*;
import dev.jadss.jadapi.management.JManager;
import dev.jadss.jadapi.management.channel.SignChangeHandler;
import dev.jadss.jadapi.management.labymod.LabyService;
import dev.jadss.jadapi.tasks.EventHandlersUpdater;
import dev.jadss.jadapi.tasks.RemovalTimer;
import dev.jadss.jadapi.tasks.SkinsStorageUpdater;
import dev.jadss.jadapi.utils.DebugOptions;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings("all")
public class JadAPI extends JavaPlugin {

    protected static JadAPI instance;
    protected JadAPIPlugin jPlugin;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private final HashMap<Player, EntityDamageEvent.DamageCause> ignoreHits = new HashMap<>();
    private final HashMap<UUID, JSignRegister> signWaits = new HashMap<>();

    private final List<JSkin> playerSkinsStorage = new ArrayList<>();
    private final List<String> headsToDownload = new ArrayList<>();

    private BukkitTask headsUpdater;
    private BukkitTask eventHandlersUpdater;

    private EventHandlersUpdater handlersUpdater;
    private JQuickEventsListener jQuickEventListener;
    private final HashMap<EventPriority, RegisteredListener> quickEventHandlers = new HashMap<>();

    private DebugOptions debug;
    public DebugOptions getDebug() { return debug; }

    public static final boolean serverConnectionEnabled = false;
    public static final boolean isFork = false; //You should change this if the API is a fork.. wow incredible.

    //Prohibitions since I fucking hate both of these... even if reloads are actually kinda cool, jadapi does not support them, and for this reason it is true.
    public static final boolean denyHibernatePlugin = true;
    public static final boolean denyReloads = true;

    //Instances.
    private LabyService labyService;
    private JManager managerInstance;

    public boolean isViaVersionPresent() { return this.getServer().getPluginManager().getPlugin("ViaVersion") != null; }

    public JadAPI() throws IOException {
        //Prevent sneaky pants.
        if(instance != null)
            for(int i = 0; i < 5000; i++)
                System.exit(Integer.MAX_VALUE);

        String color = isFork ? "&c" : "&3&l";
        new JSender(Bukkit.getConsoleSender()).sendMessage(color + "     _           _          _____ _____  ");
        new JSender(Bukkit.getConsoleSender()).sendMessage(color + "     | |         | |   /\\   |  __ \\_   _|");
        new JSender(Bukkit.getConsoleSender()).sendMessage(color + "     | | __ _  __| |  /  \\  | |__) || |  ");
        new JSender(Bukkit.getConsoleSender()).sendMessage(color + " _   | |/ _` |/ _` | / /\\ \\ |  ___/ | |  ");
        new JSender(Bukkit.getConsoleSender()).sendMessage(color + "| |__| | (_| | (_| |/ ____ \\| |    _| |_ ");
        new JSender(Bukkit.getConsoleSender()).sendMessage(color + " \\____/ \\__,_|\\__,_/_/    \\_\\_|   |_____|");
        new JSender(Bukkit.getConsoleSender()).sendMessage(" ");
        new JSender(Bukkit.getConsoleSender()).sendMessage("&eInitializing" + (isFork ? " &4&lForked " : " ") + "&3&lJadAPI &bv" + this.getDescription().getVersion() + "&e!");

        try { Thread.sleep(1000);
        } catch(InterruptedException ex) { }

        instance = this;
        jPlugin = new JadAPIAsJPlugin();

        JReflection.setupNMS();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInitializing &3&lNMS &b&lversion &7> &3" + JReflection.getNMSVersion() + " &e."));

        if(JVersion.getServerVersion() == JVersion.UNKNOWN) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m---------------------------------------------------------------"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aJadAPI does not SUPPORT versions lower then 1.7, Bugs may occur."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m---------------------------------------------------------------"));
        } else if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCurrently, &3&l1.7 is majorly supported&e, but bugs may occur on &bNBT Tags&e."));
        }

        addDefaultHeads();

        new JSender(Bukkit.getConsoleSender()).sendMessage("&aInitializing &3&lDebug Options&e!");
        this.debug = new DebugOptions();
//        this.debug.setReflectionDebug(true);

        new JSender(Bukkit.getConsoleSender()).sendMessage("&aInitializing &3&lMain Worker Manager&e!");
        managerInstance = new JManager();
        labyService = new LabyService();

        new JSender(Bukkit.getConsoleSender()).sendMessage("&fIf any errors occurr doing this initialization they are for debugging purposes and are not a problem.");
//        this.debug.setReflectionDebug(false);


        new JSender(Bukkit.getConsoleSender()).sendMessage("&aProcessing &3&L" + Material.values().length + " &eMaterials... &bPlease wait&e..");
        JMaterial.getRegistryMaterials();
        try { Thread.sleep(2500); } catch(Exception ignored) { }
        new JSender(Bukkit.getConsoleSender()).sendMessage("&3&lMaterials &bProcessed&e!");

        new JSender(Bukkit.getConsoleSender()).sendMessage("&ePatching &b&lEnchantments&e...");
        JReflection.setFieldObject(Enchantment.class, "acceptingNew", null, true);
        new JSender(Bukkit.getConsoleSender()).sendMessage("&b&lEnchantments &3Patched&e! GG");

        new JSender(Bukkit.getConsoleSender()).sendMessage("&eInitialization &3&lCompleted&e!");

        if(1==1) return;
        {//TEST ZONE!
            try {
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onLoad() {
        if(ReloadChecker.reloadExists()) {
            if(this.denyReloads) {
                new JSender(Bukkit.getConsoleSender()).sendMessage("&3&lJadAPI &7>> &3JadAPI &edoesn't support &b&lReloads&e!");
                new JSender(Bukkit.getConsoleSender()).sendMessage("&3&lJadAPI &7>> &eEnding Bukkit instance...");
                System.exit("JadAPI does not like reloads.".getBytes().length);
            }
        }

        try {
            jQuickEventListener = new JQuickEventsListener();
            for(EventPriority priority : EventPriority.values())
                quickEventHandlers.put(priority,
                        new RegisteredListener(jQuickEventListener, (listener, event) -> jQuickEventListener.onEvent(event, priority), priority, this, false));

            try {
                new NBTItem(new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.DIAMOND_BLOCK)).buildItemStack());
            } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("Error occurred while loading nbtapi.");
            }
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCould &cnot &b&lload&e!"));
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        try {
            this.jPlugin.register(true);

            getCommand("JadAPI").setExecutor(new JadAPICommand());
            getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerWorldChangeListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
            getServer().getPluginManager().registerEvents(new PluginDisableListener(), this);

            headsUpdater = new SkinsStorageUpdater().runTaskTimerAsynchronously(this, 600, 600);
            eventHandlersUpdater = new EventHandlersUpdater().runTaskTimer(this, 0, 5);
            new RemovalTimer().runTaskTimerAsynchronously(this, 1, 1);

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eInitiating &3&lLabyMod &bListeners&e!"));

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eDetecting &3&lJadAPI &bDependent Plugins&e...."));
            Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    List<String> depend = plugin.getDescription().getDepend();
                    List<String> softDepend = plugin.getDescription().getSoftDepend();
                    if(depend.contains("JadAPI") || softDepend.contains("JadAPI")) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eDetected &3" + plugin.getName() + "&e!"));
                    }
                }
            }, 0);

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eDetecting &3&lOther &bplugins&e..."));
            if(denyHibernatePlugin) {
                if(getServer().getPluginManager().getPlugin("Hibernate") != null) {
                    JavaPlugin plugin = (JavaPlugin) getServer().getPluginManager().getPlugin("Hibernate");
                    Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                        getServer().getScheduler().cancelTasks(plugin);
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eTask finished&e!"));
                    }, 20*5);
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lBungo!!!!"));
                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCould &cnot &b&lenable&e!"));
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        managerInstance.shutdown();

        //Remove instance. erase it.
        instance = null;

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eThe JadAPI is &c&lshutting down&e!"));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cDisabling plugins which require &3&lJadAPI&e!"));
        try {
            for(JadAPIPlugin plugin : (List<JadAPIPlugin>) JReflection.getFieldObject(JadAPIPlugin.class, "PLUGINS", null))
                this.getServer().getPluginManager().disablePlugin(plugin.getJavaPlugin());
        } catch(Exception ex) {
            if(this.getDebug().doMiscDebug()) {
                Bukkit.getConsoleSender().sendMessage("&3&lJadAPI &7>> &eProblem disabling all plugins.");
                ex.printStackTrace();
            }
        }
    }

    private void addDefaultHeads() {
        JHeadStorage.addToStorage(jPlugin, "default_green", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5YjI3ZmNjZDgwOTIxYmQyNjNjOTFkYzUxMWQwOWU5YTc0NjU1NWU2YzljYWQ1MmU4NTYyZWQwMTgyYTJmIn19fQ=="));
        JHeadStorage.addToStorage(jPlugin, "default_red", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZkZTNiZmNlMmQ4Y2I3MjRkZTg1NTZlNWVjMjFiN2YxNWY1ODQ2ODRhYjc4NTIxNGFkZDE2NGJlNzYyNGIifX19"));
        JHeadStorage.addToStorage(jPlugin, "default_white", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY2YTVjOTg5MjhmYTVkNGI1ZDViOGVmYjQ5MDE1NWI0ZGRhMzk1NmJjYWE5MzcxMTc3ODE0NTMyY2ZjIn19fQ=="));
        JHeadStorage.addToStorage(jPlugin, "default_grass-block", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ5YzYzYmM1MDg3MjMzMjhhMTllNTk3ZjQwODYyZDI3YWQ1YzFkNTQ1NjYzYWMyNDQ2NjU4MmY1NjhkOSJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_chest", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQwNWYzNzE3Y2FlY2E2ODFlMjkzZDU5ODQzMDU5NDVhZWQ4OGJjZDM5NTkyOTUwOGE5NmU1MjMxZmEwYTk0ZSJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_legendary-chest", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQwYmRmMzliNTRmNDk2OTJmYjM3OWI0ZWIwNGQxZWI0YTAwZTc4ZWQzOTExYWQzYjYzYTdlNWJmMzE3NjgzNyJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default-legendary-egg", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODNkZjY1MmIwYzdkZGI5NDQ2MWZjNmMwNzRhYTVlNWZkMTU3MzFkN2RkM2Q1MzMzNTFjNzA2NWJkODJiYTU1NCJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_rubiks-cube", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWIxZWYyYTQ4MjlhMTFmZDkwM2I1ZTMxMDg4NjYyYThjNTZlNDcxYmI0ODY0M2MwZDlmOTUwMDZkMTgyMDIxMCJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_red-money", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQxMTEwOWY0YWIwM2FhNmM1Yjc2Y2FkMTI5MTc2ZmZiMWZjZThjMTc0ZTY5YzllOGJhMDZiOWY4MDYxZTVhZCJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_gold-money", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ2N2E3YjlkNzZiYTZkMGZlZDc0MzYwMjUzM2ZjOThjODdhZjBjNjBmODBmMzhkYTc3NGY3YTAxYTIwOTNmYSJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_aqua-money", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMyNGE3ZDYxY2NkNDRiMDMxNzQ0YjUxN2Y5MTFhNWM0NjE2MTRiOTUzYjE3ZjY0ODI4MmUxNDdiMjlkMTBlIn19fQ=="));
        JHeadStorage.addToStorage(jPlugin, "default_wood-furnace", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQ5ZGU0MjQyNTAyMDBlZWI2ZmUzNmZhZDcwOTQ3NjJkYWQ4N2I0MmFmNjJmY2MxYzg3ZjhiMTQyYTViZDU0ZiJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_blue-ball", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTExNWRjODhlMzIxNGMzODI0M2Q3ODJkNjNlZGIwYTZlMDYyOTFlYjZkYThlNjAwYzdlMmVhMzZlN2Y2MWIzMSJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_red-ball", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIwNTVjODEwYmRkZmQxNjI2NGVjOGQ0MzljNDMyODNlMzViY2E3MWE1MDk4M2UxNWUzNjRjZDhhYjdjNjY4ZiJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_green-ball", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc1N2NmYzdjMTFiMWU2ZjEwMjhiODA0NjJkMWU0MzAyZGNhNWFiODU5MjIyNWVhOGM4Yjc2MzljNzBlNjFmOSJ9fX0="));
        JHeadStorage.addToStorage(jPlugin, "default_yellow-ball", new JHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTIwNzE2MjAzZGEwMzljYWZjYTI0YmJkOWYzZTliZDVjNjk4NWMzYzI1NTI3YmNkNTA2ZDM4OGU5OWJiN2FmZSJ9fX0="));
    }

    public static JadAPI getInstance() { return instance; }

    public JadAPIPlugin getJadPlugin() { return jPlugin; }

    public JManagement getManagement() { return managerInstance; }
    public JPacketHooker getPacketHooker() { return managerInstance; }
    public JInformationManager getInformationManager() { return managerInstance; }
    public JRegisterer getRegisterer() { return managerInstance; }

    public LabyService getLabyService() {return labyService;}

    public static ScheduledExecutorService getPrivateScheduler() { return instance.executor; }

    public HashMap<Player, EntityDamageEvent.DamageCause> getIgnoreHits() { return ignoreHits; }

    public HashMap<EventPriority, RegisteredListener> getQuickEventHandlers() { return quickEventHandlers; }
    public HashMap<UUID, JSignRegister> getSigns() { return signWaits; }

    public List<JSkin> getSkinsStorage() { return playerSkinsStorage; }
    public List<String> getSkinsToDownload() { return headsToDownload; }
}
