package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class VersionCommand {

    public VersionCommand(CommandSender bukkitSender) {
        JSender sender = new JSender(bukkitSender);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&3&lJadGens &7>> &eThis &bserver &eis &a&lrunning &3&lJadAPI &bv" + JadAPI.getInstance().getDescription().getVersion() + " &eby &3Jadss"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThere is &3currently &b" + JadAPI.getInstance().getInformationManager().getQuickEvents().size() + " &aquick events &aregistered&e."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThere is &3currently &b" + JadAPI.getInstance().getInformationManager().getPacketHooks().size() + " &apacket hooks &aregistered&e."));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThere is &3currently &b" + JadAPIPlugin.size() + " &aplugins &aregistered&e."));
    }
}
