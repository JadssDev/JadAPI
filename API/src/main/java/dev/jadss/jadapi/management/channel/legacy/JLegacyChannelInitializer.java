package dev.jadss.jadapi.management.channel.legacy;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.utils.JReflection;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelInitializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@SuppressWarnings("all")
public class JLegacyChannelInitializer extends ChannelInitializer<Channel> {

    private ChannelInitializer<Channel> channelInitializer = null;

    public JLegacyChannelInitializer(Object oldSchool) {
        this.channelInitializer = (ChannelInitializer<Channel>) oldSchool;
    }

    @Override
    protected void initChannel(Channel socketChannel) throws Exception {
        initOldChannel(socketChannel);

        if (socketChannel.pipeline().get("JadAPIChannel") == null)
            socketChannel.pipeline().addBefore("packet_handler", "JadAPIChannel", new JLegacyChannelHandler(0, null));
        else
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eAlready &3&lhooked &einto &b&lpackets&e!?"));
    }

    public void initOldChannel(Channel channel) throws Exception {
        JadAPI.getInstance().getDebug().setReflectionDebug(true);
        JReflection.executeMethod(ChannelInitializer.class, "initChannel", channelInitializer, new Class[] { Channel.class }, channel);
        JadAPI.getInstance().getDebug().setReflectionDebug(false);
    }
}
