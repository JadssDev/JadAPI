package dev.jadss.jadapi.management.channel.newer;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.interfaces.other.PacketListener;
import dev.jadss.jadapi.management.EventResult;
import dev.jadss.jadapi.management.JManager;
import dev.jadss.jadapi.management.JQuickEvent;
import dev.jadss.jadapi.management.channel.SignChangeHandler;
import dev.jadss.jadapi.management.nms.enums.EnumProtocol;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.network.ByteBufWorker;
import dev.jadss.jadapi.management.nms.packets.*;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import us.myles.ViaVersion.api.Via;

import java.util.List;
import java.util.function.BiConsumer;

@SuppressWarnings("all")
public class JChannelHandler extends ChannelDuplexHandler {

    private int mode;
    private JPlayer player;
    private JManager manager;

    /**
     * Start a channel handler.
     *
     * @param mode   <p>0 = Global<p>1 = Player.
     * @param player the player, may be null if mode is 0.
     */
    public JChannelHandler(int mode, JPlayer player) {
        this.mode = mode;
        this.player = player;
        this.manager = (JManager) JadAPI.getInstance().getManagement();
    }

    public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
        if (mode == 1) {
            JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().put(this.player.getPlayer().getUniqueId(), 1L + JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().getOrDefault(this.player.getPlayer().getUniqueId(), 0L));
        }

        if (!JadAPI.getInstance().isEnabled())
            return;

        EventResult result;
        try {
            result = checkPacket(packet, channelHandlerContext);
        } catch (Throwable ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3&lJadAPI &7>> &3JadAPI &emessed up somewhere and &cthrown &ean &3&lexception&e, information bellow to &b&lreport &ethis &bissue&e!"));

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lDump &eof &bthread&e: "));
            Thread.dumpStack();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            super.write(channelHandlerContext, packet, channelPromise);
            return;
        }

        if (!result.isCancelled())
            super.write(channelHandlerContext, result.getPacket(), channelPromise);
    }

    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
        if (mode == 1) {
            JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().put(this.player.getPlayer().getUniqueId(), 1L + JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().getOrDefault(this.player.getPlayer().getUniqueId(), 0L));
        }

        if (!JadAPI.getInstance().isEnabled()) return;

        EventResult result;
        try {
            result = checkPacket(packet, channelHandlerContext);
        } catch (Throwable ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3&lJadAPI &7>> &3JadAPI &emessed up somewhere and &cthrown &ean &3&lexception&e, information bellow to &b&lreport &ethis &bissue&e!"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lDump &eof &bthread&e: "));
            Thread.dumpStack();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            super.channelRead(channelHandlerContext, packet);
            return;
        }

        if (!result.isCancelled())
            super.channelRead(channelHandlerContext, result.getPacket());
    }

    InLoginStart loginStartParser = new InLoginStart();
    InHandshakePacket handshakeParser = new InHandshakePacket();
    InInteractAtEntityPacket interactPacket = new InInteractAtEntityPacket();
    InCustomPayloadPacket payloadPacket = new InCustomPayloadPacket();
    InUpdateSign updateSignPacket = new InUpdateSign();

    private EventResult checkPacket(Object packet, ChannelHandlerContext channelHandlerContext) {
        if (mode == 0) {
            if (JadAPI.getInstance().getDebug().doChannelDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCaught &3" + packet.getClass().getSimpleName() + " &ePacket from &b&l" + "Global" + "&e!"));

            if (handshakeParser.canParse(packet)) {
                handshakeParser.parse(packet);

                if (handshakeParser.getProtocolEnum() == EnumProtocol.LOGIN)
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCaptured a &3&lHandShaking Packet &eon Protocol \"&b&lLOGIN&e\"! Trying to find &3Player&e...."));
            } else if (loginStartParser.canParse(packet)) {
                loginStartParser.parse(packet);

                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eReceived &3&lMojang Authentication &bpacket &efrom &3" + loginStartParser.getGameProfile().getName() + "&e!"));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound &3&lHandshaking Packet &bowner&e! &a&lMerging&e..."));

                JadAPI.getInstance().getManagement().addChannelLookup(loginStartParser.getGameProfile().getName(), channelHandlerContext.channel());

                JadAPI.getInstance().getPacketHooker().callPacketHooks(handshakeParser.build(), null, false, false);
                EventResult result = JadAPI.getInstance().getPacketHooker().callPacketHooks(loginStartParser.build(), null, false, true);
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &3&lPackets &ecalls &bfinished&e!"));

                new JQuickEvent<>(JadAPI.getInstance().getJadPlugin(), PlayerLoginEvent.class, EventPriority.MONITOR, loginEvent -> {
                    JPlayer player = new JPlayer(loginEvent.getPlayer());

                    int updatedVersion = handshakeParser.getProtocol();
                    if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion"))
                        updatedVersion = Via.getAPI().getPlayerVersion(player.getPlayer().getUniqueId());
                    handshakeParser.setProtocol(updatedVersion);

                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eFound &3" + player.getPlayer().getName() + " &eon &b&lLogin&e! Protocol &a&m->&b&l " + handshakeParser.getProtocol()));
                    player.setValue("version", handshakeParser.getProtocol());

                    JadAPI.getInstance().getPacketHooker().callPacketHooks(handshakeParser.build(), player, false, false);
                    JadAPI.getInstance().getPacketHooker().callPacketHooks(loginStartParser.build(), player, false, false);

                    JadAPI.getInstance().getManagement().injectPlayer(player);
                }, 60, -1, e -> e.getPlayer().getName().equalsIgnoreCase(loginStartParser.getGameProfile().getName()), JQuickEvent.generateID()).register(true);
            }

            return new EventResult(packet, false, false);
        } else if (mode == 1) {
            if (JadAPI.getInstance().getDebug().doChannelDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCaught &3" + packet.getClass().getSimpleName() + " &ePacket from &b&l" + player.getPlayer().getName() + "&e!"));

            if (interactPacket.canParse(packet)) {
                interactPacket.parse(packet);
                for (PacketListener packetListeners : JadAPI.getInstance().getInformationManager().getPacketListeners())
                    if (packetListeners.shouldCallListeners(interactPacket))
                        for (BiConsumer<JPlayer, DefinedPacket> consumer : (List<BiConsumer<JPlayer, DefinedPacket>>) packetListeners.getListeners())
                            Bukkit.getScheduler().runTask(JadAPI.getInstance(), () -> {
                                try {
                                    consumer.accept(player, interactPacket);
                                } catch (Throwable ex) {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            "&3&lJadAPI &7>> &eThis &3&lerror &eis &c&lNOT &efrom &3&lJadAPI&e. &eBut we &bhandled &ethe error so the &3&lplayer &ewouldn't get &c&ldisconnected&e. &3Information &ebellow."));
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
                                    ex.printStackTrace();
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lDump &eof &bthread&e: "));
                                    Thread.dumpStack();
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
                                }
                            });
            } else if (payloadPacket.canParse(packet)) {
                payloadPacket.parse(packet);

                ByteBufWorker byteBuf = payloadPacket.getData().getASByteBuf();

                String readableText = payloadPacket.getData().getASByteBuf().getReadableString();

                if (payloadPacket.getChannel().equalsIgnoreCase("labymod3:main")) {
                    this.player.setValue("labymod_user", true);
                    JadAPI.getInstance().getLabyService().handle(player, payloadPacket);
                }

                if (JadAPI.getInstance().getDebug().doMiscDebug()) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCustom Payload received from " + this.player.getPlayer().getName()));
                    Bukkit.getConsoleSender().sendMessage("Channel -> " + payloadPacket.getChannel());
                    Bukkit.getConsoleSender().sendMessage("Message -> " + readableText);
                }
            } else if (updateSignPacket.canParse(packet)) {
                //means the player wrote a sign.. maybe..
                updateSignPacket.parse(packet);
                boolean playerNotified = SignChangeHandler.handleSignChange(updateSignPacket, this.player);
                if (playerNotified)
                    return new EventResult(packet, false, true);
            }
            return JadAPI.getInstance().getPacketHooker().callPacketHooks(packet, player, true, true);
        } else return new EventResult(null, false, true);
    }
}
