package dev.jadss.jadapi.bukkitImpl.entities;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JInventory;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.misc.JScoreboard;
import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import dev.jadss.jadapi.bukkitImpl.sub.JSignRegister;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.interfaces.inventory.JInventoryAbstract;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.enums.EnumChatFormat;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.other.ScoreboardTeam;
import dev.jadss.jadapi.management.nms.objects.world.block.IBlockData;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntitySign;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.management.nms.packets.*;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Represents a player in the JadAPI, includes <b>EXTRA EXTRA</b> functionality bukkit has not yet provided or did not provide in early versions.
 */
@SuppressWarnings("all")
public final class JPlayer {

    //Thanks, NameTagEdit.
    private static String teamsUUID;
    private static BigDecimal currentTeamsID = new BigDecimal("0");

    private void setupTeamsUUID() {
        if (teamsUUID != null) return;

        //Set teams uuid
        StringBuilder b = new StringBuilder();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder builder = new StringBuilder();
        Random random = new Random(new Random(new Random().nextInt(12457890) * 6 / 1275 - 69).nextInt(69) * 5);
        for (int i = 0; i < 3; i++) b.append(chars[random.nextInt(chars.length)]);
        teamsUUID = b.toString();
    }

    private static final HashMap<UUID, HashMap<String, Object>> playerValues = new HashMap<UUID, HashMap<String, Object>>();

    private Player player;

    /**
     * Create a {@link JPlayer} using their Player instance in bukkit.
     *
     * @param player the Player.
     */
    public JPlayer(Player player) {
        setupTeamsUUID();
        if (player == null) throw new JException(JException.Reason.PLAYER_IS_NULL);

        this.player = player;


        if (!playerValues.containsKey(this.player.getUniqueId()))
            playerValues.put(this.player.getUniqueId(), new HashMap<>());

    }

    /**
     * Create a JPlayer using a JPlayer instance.
     *
     * @param player The JPlayer.
     * @throws RuntimeException <h1><b>Always</b></h1>
     * @deprecated always throws exception.
     */
    @Deprecated
    public JPlayer(JPlayer player) {
        setupTeamsUUID();
        throw new RuntimeException("Why you gotta hurt my brain?");
    }

    /**
     * Create a {@link JPlayer} using the Player's name.
     *
     * @param player The player's name.
     * @throws JException If the Value is null or the player is not online!
     * @deprecated Please use UUID or Player ojbect, tho JadAPI DOES support this.
     */
    @Deprecated
    public JPlayer(String player) {
        setupTeamsUUID();
        if (player == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        this.player = Bukkit.getPlayerExact(player);

        if (this.player == null)
            throw new JException(JException.Reason.PLAYER_IS_NULL);

        if (!playerValues.containsKey(this.player.getUniqueId()))
            playerValues.put(this.player.getUniqueId(), new HashMap<>());
    }

    /**
     * Create a {@link JPlayer} using the Player's UUID.
     *
     * @param uuid The UUID of the player!
     * @throws JException if UUID is null <b>OR</b> The player is not online.
     */
    public JPlayer(UUID uuid) {
        setupTeamsUUID();
        if (uuid == null) throw new JException(JException.Reason.UUID_IS_NULL);

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if (player != null && player.isOnline())
            this.player = player.getPlayer();
        else
            throw new JException(JException.Reason.PLAYER_IS_NULL);

        if (!playerValues.containsKey(this.player.getUniqueId()))
            playerValues.put(this.player.getUniqueId(), new HashMap<>());
    }

    /**
     * Removes all data from the player, you shouldn't call this!
     */
    public void resetPlayerDataValues() {
        playerValues.remove(this.getPlayer().getUniqueId());
    }

    /**
     * Force the player to <b>respawn</b>.
     *
     * @return itself.
     */
    public JPlayer forceRespawn() {
        this.player.spigot().respawn();
        return this;
    }

    /**
     * <p>Gets the version this player is connecting with as a {@link JVersion}.</p>
     * <b>Note: </b> returns {@link JVersion}.UNKNOWN if no Version was found.
     * <p>The JadAPI only knows the version after the {@link PlayerJoinEvent} was called in {@link EventPriority}.LOWEST</p>
     *
     * @return The version as a {@link JVersion}!
     */
    public JVersion getPlayerVersion() {
        if (this.getValue("version") != null)
            return JVersion.parseProtocol((int) this.getValue("version"));
        else
            return JVersion.UNKNOWN;
    }

    /**
     * <p>Gets the <b>Protocol Version</b> this player is connecting with as an {@link Integer}.</p>
     * <b>Note: </b> returns -1 if no Version was found.
     * <p>The JadAPI only knows the version after the {@link PlayerJoinEvent} was called in {@link EventPriority}.LOWEST</p>
     *
     * @return The version as a {@link JVersion}!
     */
    public int getPlayerProtocolVersion() {
        if (this.getValue("version") != null)
            return (int) this.getValue("version");
        else
            return -1;
    }

    //maybe later!

//    /**
//     * Get the ModList using the Forge Client of the player!
//     * <p><b>Note:</b> Potentially null!</p>
//     * @return The player's ModList as {@link ml.jadss.jadapi.management.forge.Forge.ModList}
//     */
//    @Deprecated
//    public Forge.ModList getModList() {
//        return (Forge.ModList) this.getValue("forge_mods");
//    }

    /**
     * Check if this player is a LabyMod user!
     *
     * @return If he is a labymod user.
     */
    public boolean isLabyModUser() {
        return this.getValue("labymod_user") != null;
    }

    /**
     * This sends data to forge of the player to start a handshake, the ModList may not be available as soon as this is proceessed.
     *
     * @return itself.
     * @deprecated Doesn't work! Need fixing.
     */
    @Deprecated
    public JPlayer requestModList() {
        if (1 == 1) return this;

        if (this.getValue("requestedMods") instanceof Boolean)
            throw new JException(JException.Reason.NOT_AVAILABLE);

        boolean updated = this.getPlayerVersion().isNewerOrEqual(JVersion.v1_13);
        String channel = (updated ? "fml:handshake" : "FML|HS");

        Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
            if (updated) {

            } else {
                this.sendPacket(new OutCustomPayloadPacket(channel, new byte[]{-2, 0, 0, 0, 0}).build()); //Reset Handshake.

                this.sendPacket(new OutCustomPayloadPacket(channel, new byte[]{0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).build()); //ServerHello packet.
                Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                    System.out.println("ok.");
                    this.sendPacket(new OutCustomPayloadPacket(channel, new byte[]{2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).build()); //ModList
                    Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                        System.out.println("ok v2");
                        this.sendPacket(new OutCustomPayloadPacket(channel, new byte[]{3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).build()); //REGISTRY DATA.

                        //Aknewledgements!
                        Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                            System.out.println("ok v3");
                            this.sendPacket(new OutCustomPayloadPacket(channel, new byte[]{-1, 2}).build()); //WAITING CACKNELEDGEMENT

                            Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                                System.out.println("ok v4");
                                this.sendPacket(new OutCustomPayloadPacket(channel, new byte[]{-1, 3}).build()); //COMPLETE
                            }, 20L);

                        }, 20L);

                    }, 20L);

                }, 20L);
            }
        }, 20L);

        this.setValue("requestedMods", true);
        return this;
    }

    /**
     * Update the scoreboard of a player.
     *
     * @param score the JScoreboard you want to apply to the player.
     * @return itself.
     */
    public JPlayer updateScoreboard(JScoreboard score) {
        if (score == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        score.updatePlayer(this);

        return this;
    }

    /**
     * If you're on a bungeecord, you can send a player to another server using this method.
     *
     * @param server the server you want to send them to.
     * @return itself.
     */
    public JPlayer sendToServer(String server) {
        if (server == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(out);

        try {
            data.writeUTF("Connect");
            data.writeUTF(server); // Target Server
        } catch (IOException e) {
            // Can never happen
            // Yes. Fuck yes.
        }
        this.player.sendPluginMessage(JadAPI.getInstance(), "BungeeCord", out.toByteArray());

        return this;
    }

    /**
     * Teleport the player to somewhere.
     *
     * @param entity the entity to teleport to.
     * @return itself.
     */
    public JPlayer teleport(Entity entity) {
        if (entity == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        if (entity instanceof LivingEntity) {
            this.player.teleport(entity.getLocation());
        }

        return this;
    }

    /**
     * Teleport the player to somewhere.
     *
     * @param location the location to teleport
     * @return itself.
     */
    public JPlayer teleport(Location location) {
        if (location == null) throw new JException(JException.Reason.LOCATION_IS_NULL);

        this.player.teleport(location);
        return this;
    }

    /**
     * Teleport the player to somewhere.
     *
     * @param world the world to teleport to.
     * @param x     the x in the world.
     * @param y     the y in the world.
     * @param z     the z in the world.
     * @return tiself.
     */
    public JPlayer teleport(World world, double x, double y, double z) {
        if (world == null) throw new JException(JException.Reason.LOCATION_IS_NULL);

        this.player.teleport(new Location(world, x, y, z));
        return this;
    }

    /**
     * Set if a player should collide with an entity.
     *
     * @param colission boolean value.
     * @return itself.
     */
    public JPlayer setCollisionWithEntities(boolean colission) {
        this.player.spigot().setCollidesWithEntities(colission);
        return this;
    }

    /**
     * Ignore the next attack.
     *
     * @param cause the cause of damage that should be cancelled, if null, it will cancell any next damage event.
     * @return itself.
     */
    public JPlayer ignoreNextAttack(EntityDamageEvent.DamageCause cause) {
        JadAPI.getInstance().getIgnoreHits().put(this.player, cause);
        return this;
    }

    /**
     * Set every player hidden except.
     *
     * @param players except these players.
     * @return itself.
     */
    public JPlayer setONLYVisiblePlayers(Player... players) {
        if (players == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        hideAllPlayers();

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
            for (Player player : players)
                this.player.showPlayer(player);
        } else {
            JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.entity.Player"), "showPlayer", this.player, new Class[]{Plugin.class, Player.class}, JadAPI.getInstance(), player);
        }

        return this;
    }

    /**
     * Set every player shown except.
     *
     * @param players except these players.
     * @return itself.
     */
    public JPlayer setONLYInvisiblePlayers(Player... players) {
        if (players == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        showAllPlayers();

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
            for (Player player : players)
                this.player.hidePlayer(player);
        } else {
            JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.entity.Player"), "hidePlayer", this.player, new Class[]{Plugin.class, Player.class}, JadAPI.getInstance(), player);
        }

        return this;
    }

    /**
     * Set certain players hidden.
     *
     * @param players the players that should be hidden.
     * @return itself.
     */
    public JPlayer hidePlayers(Player... players) {
        if (players == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
            for (Player player : players)
                this.player.hidePlayer(player);
        } else {
            for (Player player : players)
                JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.entity.Player"), "hidePlayer", this.player, new Class[]{Plugin.class, Player.class}, JadAPI.getInstance(), player);
        }
        return this;
    }

    /**
     * Show certain players.
     *
     * @param players the certain players to show.
     * @return itself.
     */
    public JPlayer showPlayer(Player... players) {
        if (players == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
            for (Player player : players)
                this.player.showPlayer(player);
        } else {
            for (Player player : players)
                JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.entity.Player"), "showPlayer", this.player, new Class[]{Plugin.class, Player.class}, JadAPI.getInstance(), player);
        }
        return this;
    }

    /**
     * Hide all the players for this player.
     *
     * @return itself.
     */
    public JPlayer hideAllPlayers() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
            for (Player player : Bukkit.getOnlinePlayers())
                this.player.hidePlayer(player);
        } else {
            for (Player player : Bukkit.getOnlinePlayers())
                JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.entity.Player"), "hidePlayer", this.player, new Class[]{Plugin.class, Player.class}, JadAPI.getInstance(), player);
        }

        return this;
    }

    /**
     * Show all the players.
     *
     * @return itself.
     */
    public JPlayer showAllPlayers() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
            for (Player player : Bukkit.getOnlinePlayers())
                this.player.showPlayer(player);
        } else {
            for (Player player : Bukkit.getOnlinePlayers())
                JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.entity.Player"), "showPlayer", this.player, new Class[]{Plugin.class, Player.class}, JadAPI.getInstance(), player);
        }
        return this;
    }

    /**
     * [NMS] send a packet using the Player's connection.
     *
     * @param packet the packet to send.
     * @return itself
     * @throws JException if packet == null || packet DOESN'T extends Packet @ NMS.
     */
    public JPlayer sendPacket(Object packet) {
        if (packet == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        final Class<?> packetClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol" : "server." + JReflection.getNMSVersion()) + ".Packet");

        if (JReflection.cast(packetClass, packet) == null)
            throw new JException(JException.Reason.NOT_A_PACKET);

        final Class<?> playerConnectionClass = JReflection.getReflectionClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network" : JReflection.getNMSVersion()) + ".PlayerConnection");
        final Class<?> entityPlayerClass = JReflection.getReflectionClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "level" : JReflection.getNMSVersion()) + ".EntityPlayer");

        Object playerConnection = JReflection.getUnspecificFieldObject(entityPlayerClass, playerConnectionClass,
                JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.craftbukkit." + JReflection.getNMSVersion() + ".entity.CraftPlayer"), "getHandle", this.player, new Class[]{}));
        if (playerConnection == null) {
            if (JadAPI.getInstance().getDebug().doMiscDebug()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3Player Connection &eis null! Couldn't send packet."));
                Thread.dumpStack();
            }
            return this;
        }
        JReflection.executeMethod(playerConnectionClass, "sendPacket", playerConnection, new Class[]{packetClass}, packet);
        if (JadAPI.getInstance().getDebug().doMiscDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eSent a &3&lPacket &eto &b" + this.player.getName() + "&e. Type -> " + packet.getClass().getSimpleName() + "!"));
        return this;
    }

    /**
     * Send a packet using the Player's connection.
     *
     * @param packet the packet to send.
     * @return itself.
     * @see JPlayer#sendPacket(Object)
     * @see DefinedPacket
     * @see DefinedPacket#build()
     * @deprecated Please use {@link JPlayer#sendPacket(Object)} instead using {@link DefinedPacket#build()}
     */
    public JPlayer sendPacket(DefinedPacket packet) {
        return this.sendPacket(packet.build());
    }

    /**
     * Send a message to the player like sendMessage on bukkit's player.
     *
     * @param message the message to send.
     * @return itself.
     */
    public JPlayer sendMessage(String message) {
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        return this;
    }

    /**
     * Show the red corners like the WorldBorder when you're close to it, or set it to green (TO BE DONE) ... or smth else =)
     *
     * @param color The color of the corners.
     * @return itself.
     */
    public JPlayer setCorners(CornerColor color) {
        OutWorldBorderPacket packet = new OutWorldBorderPacket(OutWorldBorderPacket.WorldBorderAction.SET_WARNING_BLOCKS, 0, 0D, 0D, 0D, 0D, 0L, 0, 0);

        if (color == CornerColor.RED)
            packet.setWarningDistance(Integer.MAX_VALUE);
        else if (color == CornerColor.CLEAR)
            packet.setWarningDistance(this.player.getWorld().getWorldBorder().getWarningDistance());
        else throw new JException(JException.Reason.VALUE_IS_NULL);

        return this.sendPacket(packet.build());
    }

    /**
     * The corner color on the screen when a border is changing.
     */
    public enum CornerColor {
        CLEAR,
        RED;
    }

    /**
     * Send an action bar to the player.
     *
     * @param message the message to be sent on the action bar.
     * @return itself.
     */
    public JPlayer sendActionBar(String message) {
        if (message == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        IChatBaseComponent ichatComponent = new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', message), false, "");

        OutChatTypePacket chatPacket = new OutChatTypePacket(OutChatTypePacket.ChatMessageType.ACTION_BAR, new UUID(0L, 0L), ichatComponent, 0, 0, 0);

        this.sendPacket(chatPacket.build());

        return this;
    }

    /**
     * Send a title to this player.
     *
     * @param title    the title.
     * @param subtitle the subtitle.
     * @param fadeIn   the fade in IN TICKS (20 * seconds = ticks)
     * @param stay     the stay IN TICKS (20 * seconds = ticks)
     * @param fadeOut  the fade out IN TICKS (20 * seconds = ticks)
     * @return itself.
     */
    public JPlayer sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (title == null && subtitle == null) throw new JException(JException.Reason.BOTH_ARE_NULL);


        if (title != null) {
            OutChatTypePacket titlePacket = new OutChatTypePacket(OutChatTypePacket.TitleMessageType.TITLE, null, new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', title), false, ""), 0, 0, 0);
            sendPacket(titlePacket.build());
        }
        if (subtitle != null) {
            OutChatTypePacket subtitlePacket = new OutChatTypePacket(OutChatTypePacket.TitleMessageType.SUBTITLE, null, new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', subtitle), false, ""), 0, 0, 0);
            sendPacket(subtitlePacket.build());
        }

        OutChatTypePacket timingsPacket = new OutChatTypePacket(OutChatTypePacket.TitleMessageType.TIMINGS, null, null, fadeIn, stay, fadeOut);
        sendPacket(timingsPacket.build());

//        if(title != null) {
//            Object iChatComponent = new MinecraftIChatBaseComponent(title).build();
//            Enum enumTitleAction = null;
//            for(Enum e : (Enum[]) JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction").getEnumConstants()) if(e.name().equals("TIMES")) enumTitleAction = e;
//            Object packet = JReflection.executeConstructor(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle"),
//                    new Class[] { JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction"), JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IChatBaseComponent"), int.class, int.class, int.class },
//                    enumTitleAction, iChatComponent, fadeIn, stay, fadeOut);
//            this.sendPacket(packet);
//
//            for(Enum e : (Enum[]) JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction").getEnumConstants()) if(e.name().equals("TITLE")) enumTitleAction = e;
//            packet = JReflection.executeConstructor(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle"),
//                    new Class[] { JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction"), JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IChatBaseComponent"), int.class, int.class, int.class },
//                    enumTitleAction, iChatComponent, fadeIn, stay, fadeOut);
//            this.sendPacket(packet);
//        }
//
//        if(subtitle != null) {
//            Object iChatComponent = new MinecraftIChatBaseComponent(subtitle).build();
//            Enum enumTitleAction = null;
//            for(Enum e : (Enum[]) JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction").getEnumConstants()) if(e.name().equals("TIMES")) enumTitleAction = e;
//            Object packet = JReflection.executeConstructor(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle"),
//                    new Class[] { JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction"), JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IChatBaseComponent"), int.class, int.class, int.class },
//                    enumTitleAction, iChatComponent, fadeIn, stay, fadeOut);
//            this.sendPacket(packet);
//
//            for(Enum e : (Enum[]) JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction").getEnumConstants()) if(e.name().equals("SUBTITLE")) enumTitleAction = e;
//            packet = JReflection.executeConstructor(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle"),
//                    new Class[] { JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction"), JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IChatBaseComponent"), int.class, int.class, int.class },
//                    enumTitleAction, iChatComponent, fadeIn, stay, fadeOut);
//            this.sendPacket(packet);
//        }
        return this;
    }

    /**
     * Send a title to this player. (method for persons not interested in changing timings)
     *
     * @param title    the title.
     * @param subtitle the subtitle.
     * @return itself.
     */
    public JPlayer sendTitle(String title, String subtitle) {
        return this.sendTitle(title, subtitle, 20, 40, 20);

//        if(title != null) {
//            Object iChatComponent = NMS.buildChatBaseComponent(title);
//            Enum enumTitleAction = null;
//            for(Enum e : (Enum[]) JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction").getEnumConstants()) if(e.name().equals("TIMES")) enumTitleAction = e;
//            Object packet = JReflection.executeConstructor(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle"),
//                    new Class[] { JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction"), JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IChatBaseComponent"), int.class, int.class, int.class },
//                    enumTitleAction, iChatComponent, 20, 40, 20);
//            this.sendPacket(packet);
//
//            for(Enum e : (Enum[]) JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction").getEnumConstants()) if(e.name().equals("TITLE")) enumTitleAction = e;
//            packet = JReflection.executeConstructor(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle"),
//                    new Class[] { JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction"), JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IChatBaseComponent"), int.class, int.class, int.class },
//                    enumTitleAction, iChatComponent, 20, 40, 20);
//            this.sendPacket(packet);
//        }
//
//        if(subtitle != null) {
//            Object iChatComponent = NMS.buildChatBaseComponent(subtitle);
//            Enum enumTitleAction = null;
//            for(Enum e : (Enum[]) JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction").getEnumConstants()) if(e.name().equals("TIMES")) enumTitleAction = e;
//            Object packet = JReflection.executeConstructor(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle"),
//                    new Class[] { JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction"), JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IChatBaseComponent"), int.class, int.class, int.class },
//                    enumTitleAction, iChatComponent, 20, 40, 20);
//            this.sendPacket(packet);
//
//            for(Enum e : (Enum[]) JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction").getEnumConstants()) if(e.name().equals("SUBTITLE")) enumTitleAction = e;
//            packet = JReflection.executeConstructor(JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle"),
//                    new Class[] { JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction"), JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IChatBaseComponent"), int.class, int.class, int.class },
//                    enumTitleAction, iChatComponent, 20, 40, 20);
//            this.sendPacket(packet);
//        }
    }

    /**
     * With this you can create a sign in the player to execute something!
     * <p><b>Note: To create this sign, we have to place a sign at the player using packets.</b></p>
     *
     * @param register The sign register parameter.
     * @return tiself.
     */
    public JPlayer openTypeSign(JSignRegister register) {
        if (register == null)
            throw new JException(JException.Reason.VALUE_IS_NULL);

        Location location = this.player.getLocation().clone().add(15, 5, 15);

        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
        IBlockData blockData = NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.OAK_WALL_SIGN)).createBlockData();

        TileEntitySign sign = new TileEntitySign(position, blockData);

        //Update sign to our likings...
        IChatBaseComponent[] lines = new IChatBaseComponent[]{new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent()};
        for (int i = 0; i < 4; i++)
            lines[i].setMessage(register.getLines()[i]);

        sign.setLines(lines);

        Object updateBlock = new OutBlockChangePacket(blockData, position).build();
        Object updateSign = sign.getUpdatePacket(position).build();
        Object openSignEditor = new OutOpenSignEditor(position).build();

        //Before sending packets.
        JReflection.setUnspecificField(JSignRegister.class, Location.class, register, location.clone());
        JadAPI.getInstance().getSigns().put(this.player.getUniqueId(), register);

        //Send update packets.

        this.sendPacket(updateBlock);
        Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
            this.sendPacket(updateSign);
            Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                this.sendPacket(openSignEditor);
            }, 10L);
        }, 5L);

        return this;
    }

    /**
     * Set the meta of a player.
     *
     * @param prefix    The prefix of the player.
     * @param suffix    The suffix of the player.
     * @param teamColor the Color of the team of the player.
     * @param highness  the highness of this tag in the tablist.
     * @return itself.
     */
    public JPlayer setMeta(String prefix, String suffix, ChatColor teamColor, String highness) {
        return this.setMeta("", prefix, suffix, teamColor, highness);
    }

    /**
     * Set the prefix on the head of the player. <p>If a value is set to null, the previous value will be used instead.
     *
     * @param displayName The display name of the team.
     * @param prefix      The prefix on the top of the player head (will also apply to the tablist)
     * @param suffix      The perfix on the top of the player head (will also appear on the tablist)
     * @param teamColor   This defines the glow of the player and his name's color...
     * @param highness    Use this to make a player appear higher in the tab.
     * @return itself.
     */
    public JPlayer setMeta(String displayName, String prefix, String suffix, ChatColor teamColor, String highness) {
        ScoreboardTeam team = (ScoreboardTeam) this.getValue("team");

        if (JadAPI.getInstance().getDebug().doMiscDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3Setting &b" + this.player.getName() + "'s &3prefix &eto \"" + prefix + "\" &3suffix &eto\"" + suffix + "\"&e; &3P &b-> &a" + prefix.length() + " &f& &3S &b-> &a" + suffix.length()));

        if (prefix != null && prefix.length() > 16) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eA &3&lPlugin &btried &eto set a &3&lprefix &ethat is &b&lhigher &ethen the &a&llimit&e! " + prefix.length()));
            prefix = prefix.substring(0, 15);
        }

        if (suffix != null && suffix.length() > 16) {
            suffix = suffix.substring(0, 15);
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eA &3&lPlugin &btried &eto set a &3&lsuffix &ethat is &b&lhigher &ethen the &a&llimit&e! " + suffix.length()));
        }

        if (highness == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        if (team == null) {
            team = new ScoreboardTeam();
            team.setName(highness + "_" + teamsUUID + "_" + currentTeamsID.toString());

            currentTeamsID = currentTeamsID.add(new BigDecimal("1"));

            team.getPlayerNameSet().add(this.player.getName());

            if (displayName != null)
                team.setDisplayName(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', displayName), false, ""));
            else
                team.setDisplayName(new IChatBaseComponent("", false, ""));

            if (prefix != null)
                team.setPrefix(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', prefix), false, ""));
            else
                team.setPrefix(new IChatBaseComponent("", false, ""));

            if (suffix != null)
                team.setSuffix(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', suffix), false, ""));
            else
                team.setSuffix(new IChatBaseComponent("", false, ""));

            if (teamColor != null)
                team.setColor(EnumChatFormat.getEnumChatFormat(teamColor));
            else
                team.setColor(EnumChatFormat.getEnumChatFormat(ChatColor.WHITE));

        } else {
            ScoreboardTeam newTeam = (ScoreboardTeam) team.copy();
            newTeam.setName(highness + "_" + teamsUUID + "_" + currentTeamsID.toString());
            currentTeamsID = currentTeamsID.add(new BigDecimal("1"));

            newTeam.setPlayerNameSet(new HashSet<>());
            newTeam.getPlayerNameSet().add(this.player.getName());

            if (displayName != null)
                newTeam.setDisplayName(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', displayName), false, ""));
            else
                newTeam.setDisplayName(team.getDisplayName());

            if (prefix != null)
                newTeam.setPrefix(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', prefix), false, ""));
            else
                newTeam.setPrefix(team.getPrefix());

            if (suffix != null)
                newTeam.setSuffix(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', suffix), false, ""));
            else
                newTeam.setSuffix(team.getSuffix());

            if (teamColor != null)
                newTeam.setColor(EnumChatFormat.getEnumChatFormat(teamColor));
            else
                newTeam.setColor(team.getColor());

            team = newTeam;
        }

        this.setValue("team", team);

        Object packet = new OutScoreboardTeamPacket(team, OutScoreboardTeamPacket.TeamAction.TEAM_ADD).build();

        for (JPlayer p : JPlayer.getJPlayers())
            p.sendPacket(packet);

        return this;
    }

    /**
     * Reset the meta of the player.
     *
     * @return itself.
     */
    public JPlayer resetMeta() {
        this.setMeta("", "", ChatColor.WHITE, "zz");

        return this;
    }

    /**
     * Set the tablist header and footer of the player.
     *
     * @param header the new header.
     * @param footer the new footer.
     * @return itself.
     */
    public JPlayer setTablist(String header, String footer) {
        if (header == null && footer == null) throw new JException(JException.Reason.BOTH_ARE_NULL);

        OutPlayerHeaderFooter packet = new OutPlayerHeaderFooter();

        if (header != null)
            packet.setHeaderComponent(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', header), false, ""));
        else packet.setHeaderComponent(new IChatBaseComponent("", false, ""));

        if (footer != null)
            packet.setFooterComponent(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', footer), false, ""));
        else packet.setFooterComponent(new IChatBaseComponent("", false, ""));

        this.sendPacket(packet.build());
        return this;
    }

    /**
     * Set the tablist header and footer of the player.
     *
     * @param header the new header.
     * @param footer the new footer.
     * @return itself.
     */
    public JPlayer setTablist(List<String> header, List<String> footer) {
        List<String> headerV2 = new ArrayList<>(header);
        List<String> footerV2 = new ArrayList<>(footer);

        StringBuilder h = new StringBuilder();
        StringBuilder f = new StringBuilder();

        for (String s : header) {
            headerV2.remove(s);
            if (headerV2.size() == 0)
                h.append(ChatColor.translateAlternateColorCodes('&', s));
            else
                h.append(ChatColor.translateAlternateColorCodes('&', s) + "\n");
        }

        for (String s : footer) {
            footerV2.remove(s);
            if (footerV2.size() == 0)
                f.append(ChatColor.translateAlternateColorCodes('&', s));
            else
                f.append(ChatColor.translateAlternateColorCodes('&', s) + "\n");
        }

        this.setTablist(h.toString(), f.toString());
        return this;
    }

    /**
     * Send a block change to the player in a specific Location.
     *
     * @param location the Location to change.
     * @param material the Material to Change.
     * @return itself.
     */
    public JPlayer sendBlockChange(Location location, JMaterial material) {
        OutBlockChangePacket packet = new OutBlockChangePacket(NMS.createBlock(material).createBlockData(), new BlockPosition(location.getX(), location.getY(), location.getZ()));
        this.sendPacket(packet.build());

        return this;
    }

    /**
     * Open an inventory on the player.
     *
     * @param inventory The inventory to open.
     * @return itself.
     */
    public JPlayer openInventory(JInventory inventory) {
        this.player.openInventory(inventory.getInventory());

        return this;
    }

    /**
     * Open an inventory on the player.
     *
     * @param jInventoryAbstract The inventory to open.
     * @return itself.
     */
    public JPlayer openInventory(JInventoryAbstract jInventoryAbstract) {
        this.player.openInventory(new JInventory(jInventoryAbstract).getInventory());

        return this;
    }

    /**
     * Open an inventory on the player.
     *
     * @param inventory The inventory to open.
     * @return itself.
     */
    public JPlayer openInventory(Inventory inventory) {
        this.player.openInventory(inventory);

        return this;
    }

    /**
     * Set if a player is allowed to fly.
     *
     * @param fly is he allowed?
     * @return itself.
     */
    public JPlayer setFlying(boolean fly) {
        this.player.setAllowFlight(fly);
        return this;
    }

    /**
     * Check if the player IS allowed to fly.
     *
     * @return boolean value
     */
    public boolean isFlying() {
        return this.player.getAllowFlight();
    }

    /**
     * Get the player object.
     *
     * @return player.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the player as {@link JSender} object.
     *
     * @return {@link JSender} object.
     */
    public JSender getAsSender() {
        return new JSender((CommandSender) this.player);
    }

    /**
     * Get a value from the player variables.
     *
     * @param value the key to get.
     * @return the value.
     */
    public Object getValue(String value) {
        if (!playerValues.containsKey(this.getPlayer().getUniqueId())) return null;
        return playerValues.get(this.getPlayer().getUniqueId()).get(value);
    }

    /**
     * Set the value from the players variables.
     *
     * @param value the key to set
     * @param obj   the object to add to the key.
     */
    public void setValue(String value, Object obj) {
        playerValues.get(this.getPlayer().getUniqueId()).remove(value);
        playerValues.get(this.getPlayer().getUniqueId()).put(value, obj);
        return;
    }

    /**
     * Checks if the JPLayer Object is equals to another using the "UUID" of the player.
     *
     * @param object The other JPlayer.
     * @return if it is equals.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof JPlayer)
            return ((JPlayer) object).getPlayer().getUniqueId().equals(this.getPlayer().getUniqueId());
        else if (object instanceof UUID) return this.getPlayer().getUniqueId().equals(object);
        else return super.equals(object);
    }

    //Statics.

    /**
     * Get all the players in the server
     *
     * @return List with all players.
     */
    public static List<JPlayer> getJPlayers() {
        List<JPlayer> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) players.add(new JPlayer(player));
        return players;
    }
}
