package dev.jadss.jadapi.management.labymod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.management.JQuickEvent;
import dev.jadss.jadapi.management.labymod.features.DiscordUseSecretPacket;
import dev.jadss.jadapi.management.labymod.protocol.InfoPacket;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.management.nms.packets.InCustomPayloadPacket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Service of LabyMod of users.
 */
public class LabyService {

    private final List<LabyModListener> listeners = new ArrayList<>();
    private final List<LabyUser> users = new ArrayList<>();
    private final List<LabyModPacket> classes = new ArrayList<>();
    private JQuickEvent userRemover;

    private static boolean instantiated = false;

    public LabyService() {
        if(instantiated) throw new JException(JException.Reason.NOT_AVAILABLE);
        instantiated = true;

        classes.add(new DiscordUseSecretPacket(null, null));
        classes.add(new InfoPacket(null, null, null, null, null));
    }


    /**
     * Send a {@link LabyModPacket} to the specified Player!
     * @param player The player to send the {@link LabyModPacket} to.
     * @param packet The packet, may not be null!
     */
    public void sendPacket(JPlayer player, LabyModPacket packet) {
        if(packet == null) throw new JException(JException.Reason.NOT_A_PACKET);

        player.sendPacket(packet.buildOutgoingPacket().build());
    }

    /**
     * Called to handle a packet Payload.
     * <p>Note: Please do not use this, the <b>JadAPI</b> already automatically does this.</p>
     * @param player the Player who sent this packet.
     * @param payload the payload.
     */
    public void handle(JPlayer player, InCustomPayloadPacket payload) {
        if(userRemover == null)
            userRemover = new JQuickEvent(JadAPI.getInstance().getJadPlugin(), PlayerQuitEvent.class, e ->
                    users.removeIf(user -> user.getPlayer().getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId())),
            EventPriority.MONITOR, -1, -1, JQuickEvent.generateID()).register(true);

        if(!payload.getChannel().equalsIgnoreCase("labymod3:main")) return;

        PacketDataSerializer data = payload.getData();
        String key = data.readString();
        String json = data.readString();
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);

        if(key.equalsIgnoreCase("info")) {
            InfoPacket packet = new InfoPacket();
            packet.parse(jsonObject);
            createUser(player, packet);
        } else {
            LabyModPacket parser = classes.stream().filter(packet -> packet.getMessageKey().equalsIgnoreCase(key)).findFirst().orElse(null);
            if(parser == null) return;
            parser = parser.copy();
            parser.parse(jsonObject);

            getUser(player).addPacket(parser.copy());
            callListeners(player, parser);
        }
    }

    private void callListeners(JPlayer player, LabyModPacket packet) {
        for(LabyModListener listener : new ArrayList<>(listeners)) {
            try {
                if(listener.getOwner() != null && listener.getOwner().isRegistered()) {
                    listener.handlePacket(player, packet);
                } else listeners.remove(listener);
            } catch(Exception ex) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&3&lJadAPI &7>> &eThis &3&lerror &eis &c&lNOT &efrom &3&lJadAPI&e. &b&l" + listener.getOwner().getJavaPlugin().getName() + " &ecaused this while trying to handle a &3&lLabyModPacket&e!"));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
                ex.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            }
        }
    }

    /**
     * Register a <b>Listener</b> to <b>{@link LabyService}</b>.
     * @param listener the Listener to register
     * @see LabyModListener
     */
    public void registerListener(LabyModListener listener) {
        if(listener.getOwner() != null && listener.getOwner().isRegistered())
            this.listeners.add(listener);
    }

    /**
     * Create a User information using the player and a {@link InfoPacket}
     * @param player the Player in specific.
     * @param packet the Packet.
     */
    public void createUser(JPlayer player, InfoPacket packet) { users.add(new LabyUser(player, packet)); }

    /**
     * Get a user using the JPlayer instnace.
     * @param player the Player in specific.
     * @return the LabyUser if any was found.
     */
    public LabyUser getUser(JPlayer player) { return users.stream().filter(user -> user.getPlayer().equals(player)).findFirst().orElse(null); }

}
