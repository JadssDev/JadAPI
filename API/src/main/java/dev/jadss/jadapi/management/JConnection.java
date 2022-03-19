package dev.jadss.jadapi.management;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.JReflection;
import dev.jadss.server.Packet;
import dev.jadss.server.Protocol;
import dev.jadss.server.packets.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * PLEASE DO NOT USE THIS! this is to connect to the JadAPI Server! PLEASE DO NOT USE THIS!
 * @deprecated Must be re-written...
 */
@Deprecated
public class JConnection {

    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final ScheduledExecutorService services = Executors.newScheduledThreadPool(1);
    private final Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();
    private static long jadServersID = -1;
    private static JConnection instance = null;
    private boolean shuttingDown = false;
    private boolean blacklisted = false;

    private final String address = "localhost";
    private final int port = 17781;

    protected JConnection(boolean connect) {
        if (instance != null) throw new RuntimeException("NOPE! NOT CREATING THIS! =) CAN'T TOUCH THIS =)");

        instance = this;

        if(connect) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTrying to &b&lconnect &eto &3&lJadAPI Servers&e..."));
            scheduledFutureMap.put("Connection Starter", services.scheduleAtFixedRate(() -> {
                try {
                    this.connection = new Socket(address, port);

                    if (this.connection.isConnected() && !this.connection.isClosed()) {
                        getOut();
                        getIn();

                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &aSuccessfully &econnected to &3&lJadAPI &3Servers&e."));

                        sendPacket(new InitServerDataPacket(JadAPI.getInstance().getDescription().getVersion(), JReflection.getNMSVersion()));

                        scheduledFutureMap.put("Packet Processor", services.scheduleWithFixedDelay(this::handlePacket, 0, 25, TimeUnit.MILLISECONDS));
                        scheduledFutureMap.get("Connection Starter").cancel(true);
                    }
                } catch (Exception ex) {}
            }, 1, 5, TimeUnit.SECONDS));
        }
    }

    public static long getJadServersId() {
        return jadServersID;
    }

    public static boolean isBlacklisted() {
        if(instance == null) return false;

        return instance.blacklisted;
    }

    public ObjectInputStream getIn() {
        try {
            if (in != null) return in;
            else return (in = new ObjectInputStream(connection.getInputStream()));
        } catch (IOException | NullPointerException ex) {
//            ex.printStackTrace();
            return null;
        }
    }

    public ObjectOutputStream getOut() {
        try {
            if (out != null) return out;
            else return (out = new ObjectOutputStream(connection.getOutputStream()));
        } catch (IOException | NullPointerException ex) {
//            ex.printStackTrace();
            return null;
        }
    }

    protected static void shuttingDown() {
        if(instance == null)
            return;
        instance.shuttingDown = true;
        instance.sendPacket(new ServerShutdownPacket(jadServersID,
                (double[]) JReflection.getFieldObject(NMS.getMinecraftServer().getClass(), "recentTps", NMS.getMinecraftServer()),
                (HashMap<String, Long>) JadAPI.getInstance().getInformationManager().getEventsCalled(), JadAPI.getInstance().getInformationManager().getEventsCalledTotal()));
    }

    private void disconnect() {
        try {
            this.connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void startConnectionReviver() {
        if (scheduledFutureMap.get("Connection reviver") != null) return;

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eThe &3&lJad server &ewent &cdown&e!"));

        scheduledFutureMap.get("Packet Processor").cancel(false);

        jadServersID = -1;
        scheduledFutureMap.put("Connection reviver", services.scheduleWithFixedDelay(() -> {
            try {
                this.connection = new Socket(address, port);
                this.out = null;
                this.in = null;

                if (!this.connection.isConnected()) return;

                getOut();
                getIn();

                this.sendPacket(new InitServerDataPacket(JadAPI.getInstance().getDescription().getVersion(), JReflection.getNMSVersion()));

                scheduledFutureMap.put("Packet Processor", services.scheduleWithFixedDelay(this::handlePacket, 0, 25, TimeUnit.MILLISECONDS));

                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eReconnected to &3&lJad server&e!"));

                scheduledFutureMap.get("Connection reviver").cancel(false);
                scheduledFutureMap.remove("Connection reviver");
            } catch (IOException x) {
                if (JadAPI.getInstance().getDebug().doMiscDebug()) x.printStackTrace();
            }
        }, 10, 10, TimeUnit.SECONDS));
    }

    private void handlePacket() {
        try {
            if (getOut() == null || getIn() == null) throw new IOException("Connection lost");

            Object inputObject = getIn().readObject();
            if (inputObject == null) return;

            Protocol protocol = Packet.getProtocolFromPacket(inputObject);
            if (protocol == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPIServers &7>> &eThe protocol received from a packet was null! Please contact jadss."));
                return;
            }


            switch (protocol) {
                case SET_ID:
                    SetIDPacket packet1 = (SetIDPacket) new SetIDPacket().parsePacket(inputObject);
                    if (jadServersID == -1)
                        jadServersID = packet1.jadID;
                    else
                        disconnect();
                    return;
                case BLACKLISTED:
                    BlacklistedPacket packet = (BlacklistedPacket) new BlacklistedPacket().parsePacket(inputObject);
                    if (jadServersID == packet.jadID) {
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&3&l      _           _          _____ _____  ");
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&3&l      | |         | |   /\\   |  __ \\_   _|");
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&3&l      | | __ _  __| |  /  \\  | |__) || |  ");
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&3&l  _   | |/ _` |/ _` | / /\\ \\ |  ___/ | |  ");
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&3&l | |__| | (_| | (_| |/ ____ \\| |    _| |_ ");
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&3&l  \\____/ \\__,_|\\__,_/_/    \\_\\_|   |_____|");
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&eThis &3server &eis currently &b&lblacklisted &efrom using &3&lJadAPI&e.");
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&eReason: &3" + packet.reason);
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&eThis &3&lserver &eis gonna &b&lshutdown &ein &320 seconds&e.");
                        new JSender(Bukkit.getConsoleSender()).sendMessage("&eBelieve this is a &3mistake&e? Contact me at &9&lDISCORD&e! &bhttps://dev.jadss.ml/discord");
                        this.blacklisted = true;
                        try {
                            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
                            service.schedule(() -> {
                                service.scheduleWithFixedDelay(() -> {
                                    Bukkit.shutdown();
                                }, 5, 5, TimeUnit.MILLISECONDS);
                            }, 20, TimeUnit.SECONDS);
                        } catch (Exception ex) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eGoodbye."));
                            for (int i = 0; i < 120; i++) Bukkit.shutdown();
                            System.exit(403);
                        }
                    }
                    return;
                case REQUEST_DATA:
                    RequestServerDataPacket packet2 = (RequestServerDataPacket) new RequestServerDataPacket().parsePacket(inputObject);
                    if (jadServersID == packet2.jadID) {
                        String[] currentPlayers = new String[Bukkit.getOnlinePlayers().size()];
                        String[] currentPlugins = new String[Bukkit.getPluginManager().getPlugins().length];
                        String[] currentPluginsVersion = new String[Bukkit.getPluginManager().getPlugins().length];
                        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

                        for (int i = 0; i < currentPlayers.length; i++)
                            currentPlayers[i] = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().toArray().length])[i].getName();
                        for (int i = 0; i < currentPlugins.length; i++) {
                            currentPlugins[i] = plugins[i].getName();
                            currentPluginsVersion[i] = plugins[i].getDescription().getVersion();
                        }

                        this.sendPacket(new ServerDataPacket(jadServersID, JadAPI.getInstance().getDescription().getVersion(), JReflection.getNMSVersion(), currentPlugins, currentPluginsVersion, currentPlayers, JadAPI.getInstance().getInformationManager().getUptime(), JadAPI.getInstance().getInformationManager().getEventsCalledTotal(),
                                (double[]) JReflection.getFieldObject(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".MinecraftServer"), "recentTps", NMS.getMinecraftServer()), (HashMap<String, Long>) JadAPI.getInstance().getInformationManager().getEventsCalled()));
                    }
                    return;
                case REQUEST_PLAYER_DATA:
                    RequestPlayerDataPacket packet3 = (RequestPlayerDataPacket) new RequestPlayerDataPacket().parsePacket(inputObject);
                    JPlayer player = null;
                    boolean exists = true;
                    boolean operator = false;
                    boolean online = false;
                    try {
                        player = new JPlayer(packet3.playerName);
                    } catch (JException ex) {
                        exists = false;
                    }
                    if (exists) operator = player.getPlayer().isOp();
                    if (exists) online = player.getPlayer().isOnline();
                    this.sendPacket(new PlayerDataPacket(jadServersID, packet3.playerName, operator, exists, online));
                    return;
                case SEND_MESSAGE:
                    SendMessagePacket packet4 = (SendMessagePacket) new SendMessagePacket().parsePacket(inputObject);
                    switch (packet4.sender) {
                        case CONSOLE:
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', packet4.message));
                            return;
                        case PLAYER:
                            JPlayer pl;
                            try {
                                pl = new JPlayer(packet4.playerName);
                            } catch (JException ex) {
                                return;
                            }
                            switch (packet4.type) {
                                case CHAT:
                                    pl.sendMessage(packet4.message);
                                    return;
                                case TITLE:
                                    pl.sendTitle(packet4.message, "", 20, 60, 20);
                                    return;
                                case ACTIONBAR:
                                    pl.sendActionBar(packet4.message);
                                    return;
                            }
                            return;
                        case EVERYONE:
                            for (JPlayer all : JPlayer.getJPlayers()) {
                                switch (packet4.type) {
                                    case CHAT:
                                        all.sendMessage(packet4.message);
                                        return;
                                    case TITLE:
                                        all.sendTitle(packet4.message, "", 20, 60, 20);
                                        return;
                                    case ACTIONBAR:
                                        all.sendActionBar(packet4.message);
                                        return;
                                }
                            }
                            return;
                        default:
                            return;
                    }
                default:
                    return;
            }
        } catch (IOException | ClassNotFoundException ex) {
            if (JadAPI.getInstance().getDebug().doMiscDebug()) ex.printStackTrace();
            startConnectionReviver();
        }
    }

    private void sendPacket(Packet packet) {
        try {
            if (getOut() == null || getIn() == null) throw new IOException("Connection lost");
            getOut().writeObject(packet);
            getOut().flush();
        } catch (IOException ex) {
            if (shuttingDown) return;
            startConnectionReviver();
        }
    }
}
