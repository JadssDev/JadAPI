package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.JadAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DebugCommand {

    public DebugCommand(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            String debugType = args[0];
            if (debugType.equalsIgnoreCase("channel")) {
                JadAPI.getInstance().getDebug().setChannelDebug(!JadAPI.getInstance().getDebug().doChannelDebug());
            } else if (debugType.equalsIgnoreCase("events")) {
                JadAPI.getInstance().getDebug().setEventsDebug(!JadAPI.getInstance().getDebug().doEventsDebug());
            } else if (debugType.equalsIgnoreCase("reflection")) {
                JadAPI.getInstance().getDebug().setReflectionDebug(!JadAPI.getInstance().getDebug().doReflectionDebug());
            } else if (debugType.equalsIgnoreCase("packet_hooks")) {
                JadAPI.getInstance().getDebug().setPacketHooksDebug(!JadAPI.getInstance().getDebug().doPacketHooksDebug());
            } else if (debugType.equalsIgnoreCase("quick_events")) {
                JadAPI.getInstance().getDebug().setQuickEventsDebug(!JadAPI.getInstance().getDebug().doQuickEventsDebug());
            } else if (debugType.equalsIgnoreCase("misc")) {
                JadAPI.getInstance().getDebug().setMiscDebug(!JadAPI.getInstance().getDebug().doMiscDebug());
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &fCouldn't &aidentify &ethe &bdebug type &atyped&e!"));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eList of &aDebuggings &eavailable: "));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eChannel Debugging &a&m->&f " + (JadAPI.getInstance().getDebug().doChannelDebug() ? "&a&lYes" : "&c&lNo")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eEvents Debugging &a&m->&f " + (JadAPI.getInstance().getDebug().doEventsDebug() ? "&a&lYes" : "&c&lNo")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eReflection Debugging &a&m->&f " + (JadAPI.getInstance().getDebug().doReflectionDebug() ? "&a&lYes" : "&c&lNo")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &ePacket Hooks Debugging &a&m->&f " + (JadAPI.getInstance().getDebug().doPacketHooksDebug() ? "&a&lYes" : "&c&lNo")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eQuick Events Debugging &a&m->&f " + (JadAPI.getInstance().getDebug().doQuickEventsDebug() ? "&a&lYes" : "&c&lNo")));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eMisc Debugging &a&m->&f " + (JadAPI.getInstance().getDebug().doMiscDebug() ? "&a&lYes" : "&c&lNo")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eYou cannot execute this &b&lcommand&e. Only the &c&lConsole &ecan &bexecute &ethis!"));
        }
    }
}
