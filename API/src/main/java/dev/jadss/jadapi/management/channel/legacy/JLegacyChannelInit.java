package dev.jadss.jadapi.management.channel.legacy;

import net.minecraft.util.io.netty.channel.ChannelInitializer;
import net.minecraft.util.io.netty.channel.socket.SocketChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Method;

@SuppressWarnings("all")
public class JLegacyChannelInit extends ChannelInitializer<SocketChannel> {

    ChannelInitializer<SocketChannel> channelInitializer = null;

    public JLegacyChannelInit(Object oldSchool) {
        this.channelInitializer = (ChannelInitializer<SocketChannel>) oldSchool;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        initOldChannel(socketChannel);

        if (socketChannel.pipeline().get("JadAPIChannel") == null)
            socketChannel.pipeline().addBefore("packet_handler", "JadAPIChannel", new JLegacyChannelHandler(0, null));
        else
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eAlready &3&lhooked &einto &b&lpackets&e!?"));
    }

    public void initOldChannel(SocketChannel channel) throws Exception {
        Method method = null;
        for (Method m : channelInitializer.getClass().getDeclaredMethods())
            if (m.getName().equals("initChannel")) method = m;
        if (method == null)
            return;
        method.setAccessible(true);
        method.invoke(channelInitializer, channel);
    }
}
