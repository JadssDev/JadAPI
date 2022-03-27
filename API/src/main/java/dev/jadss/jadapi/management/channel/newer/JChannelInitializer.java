package dev.jadss.jadapi.management.channel.newer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Method;

@SuppressWarnings("all")
public class JChannelInitializer extends ChannelInitializer<Channel> {

    private ChannelInitializer<Channel> channelInitializer = null;

    public JChannelInitializer(Object oldSchool) {
        this.channelInitializer = (ChannelInitializer<Channel>) oldSchool;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        initOldChannel(channel);

        if (channel.pipeline().get("JadAPIChannel") == null)
            channel.pipeline().addBefore("packet_handler", "GlobalJadAPIHandler", new JChannelHandler(0, null));
        else
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eAlready &3&lhooked &einto &b&lpackets&e!?"));
    }

    public void initOldChannel(Channel channel) throws Exception {
        Method method = null;
        for (Method m : channelInitializer.getClass().getDeclaredMethods())
            if (m.getName().equals("initChannel")) method = m;
        if (method == null)
            return;
        method.setAccessible(true);
        method.invoke(channelInitializer, channel);
    }
}
