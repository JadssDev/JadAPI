package dev.jadss.jadapi.management;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Random;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class JPacketHook {

    private final JadAPIPlugin plugin;

    private final Class<?>[] hookedPackets;
    private final boolean hookEveryPacket;

    private final Consumer<PacketEvent> packetHandler;

    public int ticksToUnregister;
    private int executions;
    private final int executionsDefault;

    private String ID;

    private JPlayer hookedPlayer;

    /**
     * Create a Packet Hook.
     *
     * @param plugin              Your plugin instance.
     * @param packetHandler       the Runnable that should be executed.
     * @param ticksToUnregister   Ticks to unregister this {@link JPacketHook}
     * @param executionsRemaining how many times should your event be executed.
     * @param ID                  It's id, which can be get from "JQuickEvent.generateID();"
     * @param hookedPlayer        The hooked player if any.
     * @param hookedPackets       The packets this Hook should be called on.
     */
    public JPacketHook(JadAPIPlugin plugin, Consumer<PacketEvent> packetHandler, int ticksToUnregister, int executionsRemaining, String ID, JPlayer hookedPlayer, Class<?>... hookedPackets) {
        this.plugin = plugin;
        this.packetHandler = packetHandler;
        this.ticksToUnregister = ticksToUnregister;
        this.executions = 0;
        this.executionsDefault = executionsRemaining;
        this.ID = ID;
        this.hookedPackets = hookedPackets;
        this.hookEveryPacket = false;
        this.hookedPlayer = hookedPlayer;
    }

    /**
     * Create a Packet Hook.
     *
     * @param plugin              Your plugin instance.
     * @param packetHandler       the Runnable that should be executed.
     * @param ticksToUnregister   Ticks to unregister this {@link JPacketHook}
     * @param executionsRemaining how many times should your event be executed.
     * @param ID                  It's id, which can be get from "JQuickEvent.generateID();"
     * @param hookEveryPacket     Should this JPacketHook hook to every packet we receive?
     * @param hookedPlayer        The player to hook SPECIFICALLY, if you want to hook to everything, just set this to "null"!
     */
    public JPacketHook(JadAPIPlugin plugin, Consumer<PacketEvent> packetHandler, int ticksToUnregister, int executionsRemaining, String ID, boolean hookEveryPacket, JPlayer hookedPlayer) {
        this.plugin = plugin;
        this.packetHandler = packetHandler;
        this.ticksToUnregister = ticksToUnregister;
        this.executions = 0;
        this.executionsDefault = executionsRemaining;
        this.ID = ID;
        this.hookedPackets = null;
        this.hookEveryPacket = hookEveryPacket;
        this.hookedPlayer = hookedPlayer;
    }

    /**
     * Create a Packet Hook.
     *
     * @param plugin              Your plugin instance.
     * @param packetHandler       the Runnable that should be executed.
     * @param ticksToUnregister   Ticks to unregister this {@link JPacketHook}
     * @param executionsRemaining how many times should your event be executed.
     * @param hookedPlayer        The hooked player if any.
     * @param hookedPackets       The packets this Hook should be called on.
     */
    public JPacketHook(JadAPIPlugin plugin, Consumer<PacketEvent> packetHandler, int ticksToUnregister, int executionsRemaining, JPlayer hookedPlayer, Class<?>... hookedPackets) {
        this.plugin = plugin;
        this.packetHandler = packetHandler;
        this.ticksToUnregister = ticksToUnregister;
        this.executions = 0;
        this.executionsDefault = executionsRemaining;
        this.ID = generateID();
        this.hookedPackets = hookedPackets;
        this.hookEveryPacket = false;
        this.hookedPlayer = hookedPlayer;
    }

    /**
     * Register your PacketHook to the JadChannelHandler.<p>
     * if you set to true, it will be registered and will run as normal, but if it already reached the ticks to unregister value, it will not work.<P>
     * if you set to false, it will be unregisterd and will not run more.
     *
     * @param registerORunregister if the event should be registered or unregistered.
     * @return itselfffff.
     */
    public JPacketHook register(boolean registerORunregister) {
        if (!this.plugin.isRegistered()) throw new JException(JException.Reason.UNREGISTERED);
        if (registerORunregister) {
            this.executions = this.executionsDefault;
            JadAPI.getInstance().getInformationManager().getPacketHooks().add(this);
            this.plugin.getPacketHooks().add(this);
        } else {
            this.executions = 0;
            JadAPI.getInstance().getInformationManager().getPacketHooks().remove(this);
            this.plugin.getPacketHooks().remove(this);
        }
        return this;
    }

    /**
     * Run the event.
     *
     * @param packetEvent Packet that it should be ran with.
     * @return itself.
     */
    public JPacketHook run(PacketEvent packetEvent) {
        if (packetEvent == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        if (executions != -1) {
            if (executions <= 0) {
                this.register(false);
                return null;
            }

            executions--;
        }

        try {
            this.runConsumer(packetEvent);
        } catch (Throwable ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3&lJadAPI &7>> &eThis &3&lerror &eis &c&lNOT &efrom &3&lJadAPI&e, &b" + plugin.getJavaPlugin().getName() + " &3thrown this &c&lerror &eplease &b&lcontact &ethem! "));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lDump &eof &bThread&e: (" + Thread.currentThread().getName() + ")"));
            Thread.dumpStack();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
        }

        return this;
    }

    protected void runConsumer(PacketEvent packetEvent) throws Exception {
        packetHandler.accept(packetEvent);
        return;
    }

    /**
     * Generate an ID for your PacketHook.
     *
     * @return A string with some randomized characters as ID.
     */
    public static String generateID() {
        char[] chars = ("abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789").toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 25; i++) builder.append(chars[new Random().nextInt(chars.length)]);
        return builder.toString();
    }

    public JadAPIPlugin getProviderPlugin() {
        return plugin;
    }

    public int getExecutionsLeft() {
        return this.executions;
    }

    public boolean hookEveryPacket() {
        return hookEveryPacket;
    }

    public Class<?>[] getHookedPackets() {
        return this.hookedPackets;
    }

    public String getID() {
        return this.ID;
    }

    public JPlayer getHookedPlayer() {
        return hookedPlayer;
    }

    /**
     * Get a PacketHook by it's ID.
     *
     * @param ID the id to search for.
     * @return The JPacketHook object OR null if not found.
     */
    public static JPacketHook getPacketHook(String ID) {
        if (ID == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        for (JPacketHook packetHook : JadAPI.getInstance().getInformationManager().getPacketHooks()) {
            if (packetHook.getID().equals(ID)) return packetHook;
        }
        return null;
    }
}
