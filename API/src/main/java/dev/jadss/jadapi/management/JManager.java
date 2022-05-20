package dev.jadss.jadapi.management;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enchantments.EnchantmentInstance;
import dev.jadss.jadapi.bukkitImpl.enchantments.JEnchantmentInfo;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.misc.JHologram;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.interfaces.managing.JInformationManager;
import dev.jadss.jadapi.interfaces.managing.JManagement;
import dev.jadss.jadapi.interfaces.managing.JPacketHooker;
import dev.jadss.jadapi.interfaces.managing.JRegisterer;
import dev.jadss.jadapi.interfaces.other.PacketListener;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public final class JManager implements JPacketHooker, JInformationManager, JRegisterer, JManagement {

    //COLLECTIONS
    private long uptime = 0L;

    private Map<String, Long> eventsCalled = new HashMap<>();

    private Map<UUID, Long> packetsSentToPlayers = new HashMap<>();
    private Map<UUID, Long> packetsReceivedByPlayers = new HashMap<>();

    private List<JQuickEvent<?>> quickEvents = new ArrayList<>();
    private List<JPacketHook> packetHooks = new ArrayList<>();
    private List<JHologram> holograms = new ArrayList<>();

    private List<PacketListener<?>> specifiedPacketListener = new ArrayList<>();

    private Map<String, Object> channelLookup = new HashMap<String, Object>();
    //COLLECTIONS

    //InformationManager type of job.
    public List<JQuickEvent<?>> getQuickEvents() {
        return new ArrayList<>(quickEvents);
    }

    public List<JPacketHook> getPacketHooks() {
        return new ArrayList<>(packetHooks);
    }

    public List<JHologram> getHolograms() {
        return new ArrayList<>(holograms);
    }

    public List<PacketListener<?>> getPacketListeners() {
        List<PacketListener<?>> list = new ArrayList<>();
        list.addAll(getHolograms());
        list.addAll(this.specifiedPacketListener);
        return list;
    }

    public void addPacketListener(PacketListener<?> listener) {
        this.specifiedPacketListener.add(listener);
    }

    public void removePacketListener(PacketListener<?> listener) {
        this.specifiedPacketListener.remove(listener);
    }

    //////////////////////////////////////////
    public Map<String, Long> getEventsCalled() {
        return eventsCalled;
    }

    public long getEventsCalledTotal() {
        long value = 0;
        for (long l : eventsCalled.values()) value += l;
        return value;
    }

    public void addEventCall(String event) {
        eventsCalled.put(event, eventsCalled.getOrDefault(event, 0L) + 1);
    }

    //////////////////////////////////////////
    public Map<UUID, Long> getPacketsSentToPlayers() {
        return packetsSentToPlayers;
    }

    public Map<UUID, Long> getPacketsReceivedByPlayers() {
        return packetsReceivedByPlayers;
    }

    //////////////////////////////////////////
    public long getUptime() {
        return uptime;
    }
    //InformationManager finish.










    //PacketHooker type of job.
    public EventResult callPacketHooks(Object packet, JPlayer player, boolean cancellable, boolean editable) {
        PacketEvent packetEvent = new PacketEvent(!Bukkit.getServer().isPrimaryThread(), packet, player, cancellable, editable);

        for (JPacketHook packetHook : new ArrayList<>(this.getPacketHooks())) {
            if (packetHook.isHookingEveryPacket() && (packetHook.getHookedPlayer() == null || packetHook.getHookedPlayer().equals(player))) {
                packetHook.run(packetEvent);
            } else {
                for (Class<?> classy : packetHook.getHookedPackets()) {
                    if (classy.equals(packet.getClass())) {
                        if (packetHook.getHookedPlayer() == null || packetHook.getHookedPlayer().equals(player)) {
                            packetHook.run(packetEvent);
                            if (packetEvent.getResult() != null) {
                                //Packet hook finished the CompletableFuture with reflection.
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&3&lJadAPI &7>> &3" + packetHook.getProviderPlugin().getJavaPlugin().getName() + " &ehas &battempted &eto &c&lfinish &ea &aPacketHook &ewith &b&lreflection &due to this, this &3plugin &ehas been &c&ldisabled&e!"));
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
                                Thread.dumpStack();
                                packetHook.getProviderPlugin().register(false);
                                try {
                                    Field field = PacketEvent.class.getDeclaredField("result");
                                    field.setAccessible(true);
                                    field.set(packetEvent, new CompletableFuture<>());
                                } catch (NoSuchFieldException | IllegalAccessException ex) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eError attempting to patch EventResult field!"));
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        EventResult result = new EventResult(packetEvent.getPacket(), packetEvent.hasBeenEdited(), packetEvent.isCancelled());
        try {
            Field field = PacketEvent.class.getDeclaredField("result");
            field.setAccessible(true);
            ((CompletableFuture<EventResult>) field.get(packetEvent)).complete(result);
        } catch(NoSuchFieldException | IllegalAccessException ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eError attempting to complete Event!"));
            ex.printStackTrace();
        }

        return result;
    }

    //PacketHooker finish.


    //JManagement type of job.
    public void shutdown() {
        //todo...
    }

    public void addChannelLookup(String playerName, Object channel) {
        this.channelLookup.put(playerName, channel);
    }

    public void injectPlayer(JPlayer player) {
        if (player == null) Bukkit.shutdown();

        boolean success = injectInternal(player.getPlayer(), true);

        if (success) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &bInjected &3&lChannel &eto &3" + player.getPlayer().getName() + "&e!"));
            return;
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCouldn't &binject &3&lChannel &eto &3" + player.getPlayer().getName() + "&e! Doing loop..."));

        Bukkit.getScheduler().runTaskAsynchronously(JadAPI.getInstance(), () -> {
            while (!injectInternal(player.getPlayer(), false)) ;
        });

        return;
    }

    //private util to inject.

    private boolean isLegacy = JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7);

    private Class<?> legacyChannelClass = JReflection.getReflectionClass("net.minecraft.util.io.netty.channel.Channel");
    private Class<?> legacyChannelHandlerClass = JReflection.getReflectionClass("net.minecraft.util.io.netty.channel.ChannelHandler");
    private Class<?> legacyChannelPipelineClass = JReflection.getReflectionClass("net.minecraft.util.io.netty.channel.ChannelPipeline");
    private Class<?> legacyChannelFutureClass = JReflection.getReflectionClass("net.minecraft.util.io.netty.channel.ChannelFuture");
    private Class<?> legacyChannelInitializerClass = JReflection.getReflectionClass("net.minecraft.util.io.netty.channel.ChannelInitializer");

    private Class<?> newerChannelClass = JReflection.getReflectionClass("io.netty.channel.Channel");
    private Class<?> newerChannelHandlerClass = JReflection.getReflectionClass("io.netty.channel.ChannelHandler");
    private Class<?> newerChannelPipelineClass = JReflection.getReflectionClass("io.netty.channel.ChannelPipeline");
    private Class<?> newerChannelFutureClass = JReflection.getReflectionClass("io.netty.channel.ChannelFuture");
    private Class<?> newerChannelInitializerClass = JReflection.getReflectionClass("io.netty.channel.ChannelInitializer");

    private boolean injectInternal(Player player, boolean log) {
        if (log)
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &bInjecting &3&lChannel &eto &3" + player.getPlayer().getName() + "&e!"));

        Object channel = channelLookup.get(player.getName());

        if (channel == null) {
            if (log)
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCould not &bacquire &ecached &3&lChannel &efor &3" + player.getPlayer().getName() + "&e! Acquiring it now!"));

            Object entityPlayer = JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.craftbukkit." + JReflection.getNMSVersion() + ".entity.CraftPlayer"), "getHandle", player, new Class[]{});
            if (entityPlayer == null) return false;
            Object playerConnection = JReflection.getFieldObject(entityPlayer.getClass(), JReflection.getReflectionClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network" : JReflection.getNMSVersion()) + ".PlayerConnection"), entityPlayer);
            if (playerConnection == null) return false;
            Object networkManager = JReflection.getFieldObject(playerConnection.getClass(), JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network" : "server." + JReflection.getNMSVersion()) + ".NetworkManager"), playerConnection);
            if (networkManager == null) return false;

            if (isLegacy)
                channel = JReflection.getFieldObject(networkManager.getClass(), legacyChannelClass, networkManager);
            else
                channel = JReflection.getFieldObject(networkManager.getClass(), newerChannelClass, networkManager);

            if (channel == null) {
                if (log)
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCould not &bacquire &ethe &3&lChannel&e..."));
                return false;
            }

            if (log)
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eSuccessfully acquired &3&lChannel&e."));
        } else if (log) {
            channelLookup.remove(player.getName());
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound &bcached &3&lChannel&e!"));
        }

        Object pipeline = JReflection.executeMethod((isLegacy ? legacyChannelClass : newerChannelClass), "pipeline", channel, new Class[]{});
        Class<?> pipelineClass = (isLegacy ? legacyChannelPipelineClass : newerChannelPipelineClass);
        if (JReflection.executeMethod(pipelineClass, "get", pipeline, new Class[]{String.class}, "JadAPI_Player_Handler") != null)
            return true;

        { //register. (String, String, ChannelHandler) ((Add Handler))
            JReflection.executeMethod(isLegacy ? legacyChannelPipelineClass : newerChannelPipelineClass, "addBefore", pipeline,
                    new Class[]{String.class, String.class, isLegacy ? legacyChannelHandlerClass : newerChannelHandlerClass},
                    "packet_handler", "JadAPI_Player_Handler",
                    JReflection.executeConstructor(isLegacy ? JReflection.getReflectionClass("dev.jadss.jadapi.management.channel.legacy.JLegacyChannelHandler") : JReflection.getReflectionClass("dev.jadss.jadapi.management.channel.newer.JChannelHandler"),
                            new Class[]{int.class, JPlayer.class}, new Object[]{1, new JPlayer(player)}));

        }

        return true;
    }

    //private util to inject.

    //JManagement finish.







    //UTILS
    private Thread registerer = null;
    //UTILS

    private static boolean instantiated = false;

    public JManager() {
        if (instantiated) throw new JException(JException.Reason.NOT_AVAILABLE);
        instantiated = true;

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInitializing &3&lManager&e! &b(CodeName: JPacketHooker, JInformationManager, JRegisterer, JManagement"));

        if (JadAPI.serverConnectionEnabled) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInitializing &3&lJadAPI Servers &aconnection&e! (it doesn't connect, it's disabled)"));
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eConnection to &3&lServers &eis &c&ldisabled&e!"));
        }

        JadAPI.getPrivateScheduler().scheduleWithFixedDelay(() -> {
            uptime++;
        }, 1, 1, TimeUnit.SECONDS);

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInitializing &3Netty &b&linjection&e..."));
        registerHandler();
    }

    @Override
    public EnchantmentInstance registerEnchantment(JEnchantmentInfo info) {
        if (info.getEnchantmentOwner() != null && info.getEnchantmentOwner().isRegistered()) {
            EnchantmentInstance instance = null;
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_9)) {
                instance = (EnchantmentInstance) JReflection.executeConstructor(JReflection.getReflectionClass("dev.jadss.jadapi.enchmodules.RevisionOneEnchantment"), new Class[]{JEnchantmentInfo.class}, info);
            } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_10)) {
                instance = (EnchantmentInstance) JReflection.executeConstructor(JReflection.getReflectionClass("dev.jadss.jadapi.enchmodules.RevisionTwoEnchantment"), new Class[]{JEnchantmentInfo.class}, info);
            } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
                instance = (EnchantmentInstance) JReflection.executeConstructor(JReflection.getReflectionClass("dev.jadss.jadapi.enchmodules.RevisionThreeEnchantment"), new Class[]{JEnchantmentInfo.class}, info);
            } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
                instance = (EnchantmentInstance) JReflection.executeConstructor(JReflection.getReflectionClass("dev.jadss.jadapi.enchmodules.RevisionFourEnchantment"), new Class[]{JEnchantmentInfo.class}, info);
            } else if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
                instance = (EnchantmentInstance) JReflection.executeConstructor(JReflection.getReflectionClass("dev.jadss.jadapi.enchmodules.RevisionFiveEnchantment"), new Class[]{JEnchantmentInfo.class}, info);
            } else throw new JException(JException.Reason.NOT_AVAILABLE);
            info.getEnchantmentOwner().getEnchantments().add(instance);
            //Register it....
            Enchantment.registerEnchantment(instance.asEnchantment());
            info.setRegistered(instance);

            return instance;
        } else throw new JException(JException.Reason.UNREGISTERED);
    }

    @Override
    public void unregisterEnchantment(EnchantmentInstance instance) {
        if (instance.isRegistered()) {
            instance.unregister();
        }

        ((Map<String, Enchantment>) JReflection.getFieldObjectByName(Enchantment.class, "byName", null)).remove(instance.getEnchantmentInformation().getName(), instance.asEnchantment());
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            ((Map<Integer, Enchantment>) JReflection.getFieldObjectByName(Enchantment.class, "byId", null)).remove(instance.getEnchantmentInformation().getId());
        } else {
            Map<?, Enchantment> map = (Map<?, Enchantment>) JReflection.getFieldObjectByName(Enchantment.class, "byKey", null);
            AtomicReference<Object> nameSpace = null;
            map.forEach((object, enchantment) -> {
                if (enchantment.equals(instance.asEnchantment()))
                    nameSpace.set(object);
            });
            if (nameSpace.get() == null) {
                throw new JException(JException.Reason.SOMETHING_WENT_WRONG);
            }
            map.remove(nameSpace);
        }
    }

    @Override
    public JQuickEvent<?> registerQuickEvent(JQuickEvent<?> quickEvent) {
        if (quickEvent.isRegistered())
            return quickEvent;

        this.quickEvents.add(quickEvent);
        quickEvent.getRegisterer().getQuickEvents().add(quickEvent);

        if (JadAPI.getInstance().getDebug().doQuickEventsDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eRegistered a &3&lQuickEvent&e! &bEvent &e&m->&a " + quickEvent.getEventType().getSimpleName() + "&e; &bRegisterer &e&m->&a " + quickEvent.getRegisterer().getJavaPlugin().getName()));

        return quickEvent;
    }

    @Override
    public void unregisterQuickEvent(JQuickEvent<?> quickEvent) {
        this.quickEvents.remove(quickEvent);
        quickEvent.getRegisterer().getQuickEvents().remove(quickEvent);

        if (JadAPI.getInstance().getDebug().doQuickEventsDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eUnregistered a &3&lQuickEvent&e! &bEvent &e&m->&a " + quickEvent.getEventType().getSimpleName() + "&e; &bRegisterer &e&m->&a " + quickEvent.getRegisterer().getJavaPlugin().getName()));
    }

    @Override
    public JPacketHook registerPacketHook(JPacketHook packetEvent) {
        if (packetEvent.isRegistered())
            return packetEvent;

        this.packetHooks.add(packetEvent);
        packetEvent.getRegisterer().getPacketHooks().add(packetEvent);

        if (JadAPI.getInstance().getDebug().doPacketHooksDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eRegistered a &3&lPacketEvent&e! &bPackets &e&m->&a " + packetEvent.getHookedPackets().stream().map(c -> c.getSimpleName()).collect(Collectors.joining(", ")) + "&e; &bRegisterer &e&m->&a " + packetEvent.getRegisterer().getJavaPlugin().getName()));

        return packetEvent;
    }

    @Override
    public void unregisterPacketHook(JPacketHook packetEvent) {
        this.packetHooks.remove(packetEvent);
        packetEvent.getRegisterer().getPacketHooks().remove(packetEvent);

        if (JadAPI.getInstance().getDebug().doPacketHooksDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eUnregistered a &3&lPacketEvent&e! &bPackets &e&m->&a " + packetEvent.getHookedPackets().stream().map(c -> c.getSimpleName()).collect(Collectors.joining(", ")) + "&e; &bRegisterer &e&m->&a " + packetEvent.getRegisterer().getJavaPlugin().getName()));
    }


    //Other.

    private boolean registered = false;
    private boolean tryingToRegister = false;

    @Override
    public void registerHandler() {
        if (tryingToRegister || registered) throw new JException(JException.Reason.ALREADY_REGISTERED);

        this.tryingToRegister = true;
        registerer = new Thread(() -> {
            try {
                while (true) {
                    Object minecraftServer = NMS.getMinecraftServer();
                    if (minecraftServer == null) continue;

                    Object serverConnection = JReflection.getFieldObject(JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "server" : "server." + JReflection.getNMSVersion()) + ".MinecraftServer"),
                            JReflection.getReflectionClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network" : JReflection.getNMSVersion()) + ".ServerConnection"), minecraftServer);
                    if (serverConnection == null) continue;

                    List<Object> listObjects = (List<Object>) JReflection.getFieldObject(serverConnection.getClass(), List.class, serverConnection);
                    if (listObjects == null) continue;

                    if (listObjects.size() == 0) continue;

                    Thread thread = new Thread(() -> {
                        for (Object channelFuture : listObjects) {
                            try {
                                Object channel = JReflection.executeMethod(isLegacy ? legacyChannelFutureClass : newerChannelFutureClass, "channel", channelFuture, new Class[]{});
                                Object pipeline = JReflection.executeMethod(isLegacy ? legacyChannelClass : newerChannelClass, "pipeline", channel, new Class[]{});
                                for (String handlerName : (List<String>) JReflection.executeMethod(isLegacy ? legacyChannelPipelineClass : newerChannelPipelineClass, "names", pipeline, new Class[]{})) {
                                    Object bootstrapAcceptor;
                                    Object handler = JReflection.executeMethod(isLegacy ? legacyChannelPipelineClass : newerChannelPipelineClass, "get", pipeline, new Class[]{String.class}, handlerName);
                                    if (handler == null) continue;

                                    Field bootstrapAcceptorField = handler.getClass().getDeclaredField("childHandler");
                                    bootstrapAcceptorField.setAccessible(true);
                                    bootstrapAcceptorField.get(handler);
                                    bootstrapAcceptor = handler;

                                    Object channelInitializer = JReflection.executeConstructor(
                                            isLegacy ? JReflection.getReflectionClass("dev.jadss.jadapi.management.channel.legacy.JLegacyChannelInitializer") : JReflection.getReflectionClass("dev.jadss.jadapi.management.channel.newer.JChannelInitializer"),
                                            new Class[]{Object.class}, bootstrapAcceptorField.get(bootstrapAcceptor));

                                    for (Player player : Bukkit.getOnlinePlayers())
                                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &ePlease &breconnect&e."));

                                    //Replace the old channel initializer with our own.
                                    bootstrapAcceptorField.set(bootstrapAcceptor, channelInitializer);

                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3&lJadAPI &ehas successfully &binjected &einto &b&lNetty&e!"));
                                }
                                this.registered = true;
                            } catch (Exception ex) {
                                this.registered = false;
                                ex.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCould not &bhook &einto the &3&lNetty channel&e, cannot analyze &3packets&e! &b&lSadge&e!"));
                            }
                        }
                        this.tryingToRegister = false;
                    });
                    thread.run();
                    registerer.stop();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFor some reason, we got an exception.. I don't get it, dumping..."));
                Thread.dumpStack();
                Bukkit.shutdown();
            }
        }, "Injector - JADAPI");
        registerer.start();
    }

    public void unregisterHandler() {
        throw new UnsupportedOperationException("Unsupported for now!");
    }
}
