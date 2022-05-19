package dev.jadss.jadapi.management;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.annotations.ForRemoval;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class JPacketHook {

    private final JadAPIPlugin registerer;

    private final List<Class<?>> hookedPackets;
    private final boolean hookEveryPacket;
    private final JPlayer hookedPlayer;

    public int ticksToUnregister;

    private final int executionsDefault;
    private int executions;

    private final Consumer<PacketEvent> packetHandler;
    private final Predicate<PacketEvent> checker;

    private final String id;


    public JPacketHook(JadAPIPlugin registerer, Consumer<PacketEvent> packetHandler, Predicate<PacketEvent> checker, int ticksToUnregister, int executions,
                       Class<?>[] hookedPackets, JPlayer hookedPlayer, String id) {
        this.registerer = registerer;
        this.hookedPackets = Arrays.asList(hookedPackets);
        this.hookEveryPacket = false;

        this.hookedPlayer = hookedPlayer;

        this.ticksToUnregister = ticksToUnregister;

        this.executionsDefault = executions;
        this.executions = executions;

        this.packetHandler = packetHandler;
        this.checker = checker;

        if (id == null)
            id = JPacketHook.generateID();

        if (getPacketHook(id) != null)
            throw new JException(JException.Reason.OTHER);

        this.id = id;
    }

    public JPacketHook(JadAPIPlugin registerer, Consumer<PacketEvent> packetHandler, Predicate<PacketEvent> checker, int ticksToUnregister, int executions, JPlayer hookedPlayer, String id) {
        this.registerer = registerer;
        this.hookedPackets = null;
        this.hookEveryPacket = true;

        this.hookedPlayer = hookedPlayer;

        this.ticksToUnregister = ticksToUnregister;

        this.executionsDefault = executions;
        this.executions = executions;

        this.packetHandler = packetHandler;
        this.checker = checker;

        if (id == null)
            id = JPacketHook.generateID();

        if (getPacketHook(id) != null)
            throw new JException(JException.Reason.OTHER);

        this.id = id;
    }

    public JPacketHook(JadAPIPlugin registerer, Consumer<PacketEvent> packetHandler, Predicate<PacketEvent> checker, int ticksToUnregister, int executions,
                       List<Class<?>> hookedPackets, JPlayer hookedPlayer, String id) {
        this(registerer, packetHandler, checker, ticksToUnregister, executions, hookedPackets.toArray(new Class[hookedPackets.size()]), hookedPlayer, id);
    }

    @Deprecated
    @ForRemoval(willBeRemoved = true, expectedVersionForRemoval = "1.23.1", reason = "A new constrcutor was created to fit all needs.")
    public JPacketHook(JadAPIPlugin registerer, Consumer<PacketEvent> packetHandler, int ticksToUnregister, int executionsRemaining, String id, JPlayer hookedPlayer, Class<?>... hookedPackets) {
        this(registerer, packetHandler, null, ticksToUnregister, executionsRemaining, hookedPackets, hookedPlayer, id);
    }

    @Deprecated
    @ForRemoval(willBeRemoved = true, expectedVersionForRemoval = "1.23.1", reason = "A new constrcutor was created to fit all needs.")
    public JPacketHook(JadAPIPlugin registerer, Consumer<PacketEvent> packetHandler, int ticksToUnregister, int executionsRemaining, String id, boolean hookEveryPacket, JPlayer hookedPlayer) {
        this(registerer, packetHandler, null, ticksToUnregister, executionsRemaining, hookedPlayer, id);
    }

    @Deprecated
    @ForRemoval(willBeRemoved = true, expectedVersionForRemoval = "1.23.1", reason = "A new constrcutor was created to fit all needs.")
    public JPacketHook(JadAPIPlugin registerer, Consumer<PacketEvent> packetHandler, int ticksToUnregister, int executionsRemaining, JPlayer hookedPlayer, Class<?>... hookedPackets) {
        this(registerer, packetHandler, null, ticksToUnregister, executionsRemaining, hookedPackets, hookedPlayer, null);
    }

    /**
     * <p>Register the PacketHook to JadEventHandler</p>
     * <p>Set this to true to start this PacketHook, starting this QuickEvent will instantly register it and making it able to be called in a nano second later, but it will unregister itself if the executions run out or ticks to unregister reaches 0!</p>
     * <p>Set this to false to stop this PacketHook from running.</p>
     * @param enable if this QuickEvent should be registered.
     * @return itself.
     */
    public JPacketHook register(boolean enable) {
        if (!this.registerer.isRegistered()) {
            throw new JException(JException.Reason.UNREGISTERED);
        }

        if (enable) {
            if (this.isRegistered())
                return this;

            this.setExecutions(this.executionsDefault);
            JadAPI.getInstance().getRegisterer().registerPacketHook(this);
        } else {
            this.setExecutions(0);
            JadAPI.getInstance().getRegisterer().unregisterPacketHook(this);
        }

        return this;
    }

    /**
     * Checks the predicate using the specified Event.
     * @param event The event to check.
     * @return if the predicate is true or false.
     */
    public boolean check(PacketEvent event) {
        return checker.test(event);
    }

    /**
     * Run the event.
     * @param packetEvent Packet that it should be ran with.
     * @return itself.
     */
    public JPacketHook run(PacketEvent packetEvent) {
        if (packetEvent == null)
            throw new JException(JException.Reason.VALUE_IS_NULL);

        if (executions != -1) {
            if (executions <= 0) {
                this.register(false);
                return null;
            }

            executions--;
        }

        try {
            packetHandler.accept(packetEvent);
        } catch (Throwable ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3&lJadAPI &7>> &eThis &3&lerror &eis &c&lNOT &efrom &3&lJadAPI&e, &b" + registerer.getJavaPlugin().getName() + " &3thrown this &c&lerror &eplease &b&lcontact &ethem! "));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lDump &eof &bThread&e: (" + Thread.currentThread().getName() + ")"));
            Thread.dumpStack();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
        }

        return this;
    }

    //Getters and Setters.

    /**
     * Check if this PacketHook is Registered.
     * @return if this PacketHook is registered!
     */
    public boolean isRegistered() {
        return JadAPI.getInstance().getInformationManager().getPacketHooks().contains(this);
    }

    /**
     * Get the Registerer of this PacketHook.
     * @return the Registerer of this PacketHook.
     */
    public JadAPIPlugin getRegisterer() {
        return this.registerer;
    }

    /**
     * Get the classes that this PacketHook listens for.
     * @return the classes that this PacketHook listens for.
     */
    public List<Class<?>> getHookedPackets() {
        return new ArrayList<>(this.hookedPackets);
    }

    /**
     * Get if this PacketHook is hooking every packet.
     * @return if this PacketHook is hooking every packet.
     */
    public boolean isHookingEveryPacket() {
        return this.hookEveryPacket;
    }

    /**
     * Get the player that this PacketHook is listening specifically for.
     * @return the player that this PacketHook is listening for.
     */
    public JPlayer getHookedPlayer() {
        return this.hookedPlayer;
    }

    /**
     * Get how many ticks for this PacketHook to unregister itself.
     * @return how many ticks for this PacketHook to unregister itself.
     */
    public int getTicksToUnregister() {
        return this.ticksToUnregister;
    }

    /**
     * Set how many ticks for this PacketHook to unregister itself.
     * @param ticks the ticks to set to.
     */
    public void setTicksToUnregister(int ticks) {
        this.ticksToUnregister = ticks;
    }

    /**
     * Get the default number of executions!
     * @return the default number of executions!
     */
    public int getExecutionsDefault() {
        return this.executionsDefault;
    }

    /**
     * Get the number of executions of this PacketHook until it unregisters itself.
     * @return the number of executions of this PacketHook until it unregisters itself.
     */
    public int getExecutions() {
        return this.executions;
    }

    /**
     * Set the number of executions of this PacketHook until it unregisters itself.
     * @param executions the number of executions to set to.
     */
    public void setExecutions(int executions) {
        this.executions = executions;
    }

    /**
     * Get the ID of this PacketHook.
     * @return the id of this PacketHook!
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the provider plugin!
     * @return the provider plugin of this quick event!
     * @deprecated Please use {@link JQuickEvent#getRegisterer} instead.
     */
    @Deprecated
    @ForRemoval(willBeRemoved = false, expectedVersionForRemoval = "?", reason = "It is not really necessary to use this, it is prefered to use the getRegisterer() method, for now this is annotated as ForRemoval!")
    public JadAPIPlugin getProviderPlugin() {
        return registerer;
    }

    //End.


    private static final char[] ID_CHARACTERS = ("abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "!\"#$%&/(*)").toCharArray();

    /**
     * Generate an ID for your JPacketHook.
     * @return A string with some randomized characters as ID.
     */
    public static String generateID() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            builder.append(ID_CHARACTERS[new Random().nextInt(ID_CHARACTERS.length)]);
        }
        return builder.toString();
    }

    /**
     * Get a PacketHook by its ID.
     * @param id the id to search for.
     * @return The {@link JPacketHook} OR null if not found.
     */
    public static JPacketHook getPacketHook(String id) {
        if (id == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        for (JPacketHook packetHook : JadAPI.getInstance().getInformationManager().getPacketHooks()) {
            if (packetHook.getId().equals(id)) return packetHook;
        }
        return null;
    }
}
