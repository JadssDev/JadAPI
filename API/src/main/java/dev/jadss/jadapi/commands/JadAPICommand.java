package dev.jadss.jadapi.commands;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.commands.sub.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class JadAPICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            new WelcomeCommand(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            new HelpCommand(sender);
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("actionbar")) {
            new ActionBarCommand(sender, args);
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("title")) {
            new TitleCommand(sender, args);
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("subtitle")) {
            new SubTitleCommand(sender, args);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("type_racer")) {
            new TypeRacerCommand(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("test")) {
            new TestCommand(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            new InfoCommand(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            new VersionCommand(sender);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("debug")) {
            if(sender instanceof ConsoleCommandSender) {
                String debugType = args[1];
                if(debugType.equalsIgnoreCase("channel")) {
                    JadAPI.getInstance().getDebug().setChannelDebug(!JadAPI.getInstance().getDebug().doChannelDebug());
                } else if(debugType.equalsIgnoreCase("events")) {
                    JadAPI.getInstance().getDebug().setEventsDebug(!JadAPI.getInstance().getDebug().doEventsDebug());
                } else if(debugType.equalsIgnoreCase("reflection")) {
                    JadAPI.getInstance().getDebug().setReflectionDebug(!JadAPI.getInstance().getDebug().doReflectionDebug());
                } else if(debugType.equalsIgnoreCase("packet_hooks")) {
                    JadAPI.getInstance().getDebug().setPacketHooksDebug(!JadAPI.getInstance().getDebug().doPacketHooksDebug());
                } else if(debugType.equalsIgnoreCase("quick_events")) {
                    JadAPI.getInstance().getDebug().setQuickEventsDebug(!JadAPI.getInstance().getDebug().doQuickEventsDebug());
                } else if(debugType.equalsIgnoreCase("misc")) {
                    JadAPI.getInstance().getDebug().setMiscDebug(!JadAPI.getInstance().getDebug().doMiscDebug());
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &c&lInvalid debug type."));
                    return true;
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eUpdated List of debugs: "));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eChannel Debugging: &" + (JadAPI.getInstance().getDebug().doChannelDebug() ? "&a&lYes" : "&c&lNo")));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eEvents Debugging: &" + (JadAPI.getInstance().getDebug().doEventsDebug() ? "&a&lYes" : "&c&lNo")));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eReflection Debugging: &" + (JadAPI.getInstance().getDebug().doReflectionDebug() ? "&a&lYes" : "&c&lNo")));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &ePacket Hooks Debugging: &" + (JadAPI.getInstance().getDebug().doPacketHooksDebug() ? "&a&lYes" : "&c&lNo")));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eQuick Events Debugging: &" + (JadAPI.getInstance().getDebug().doQuickEventsDebug() ? "&a&lYes" : "&c&lNo")));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eMisc Debugging: &" + (JadAPI.getInstance().getDebug().doMiscDebug() ? "&a&lYes" : "&c&lNo")));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eYou &c&lcan't &euse &bthis&e!"));
            }
        } else {
            new WelcomeCommand(sender);
        }

        return true;
    }
}
