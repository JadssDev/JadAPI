package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HelpCommand {

    public HelpCommand(CommandSender sender) {
        if(sender instanceof ConsoleCommandSender || sender.hasPermission("JadAPI.helpCommand")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &eby &b&lJadssDev"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eCommands: "));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/JadAPI &bHelp &7> &bDisplay &ethis &3help&e."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/JadAPI &bActionBar &e<&bPlayer&e/&b&l*&e> &e<&bMSG&e> &7> &b&lDisplay &ea &3message &ein the &b&lActionBar&e!"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/JadAPI &bTitle &e<&bPlayer&e/&b&l*&e> &e<&bMSG&e> &7> &b&lDisplay &ea &3message &ein the &b&lTitle&e!"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/JadAPI &bSubTitle &e<&bPlayer&e/&b&l*&e> &e<&bMSG&e> &7> &b&lDisplay &ea &3message &ein the &b&lSubTitle&e!"));

            if(sender instanceof Player) {
                new JPlayer((Player) sender)
                        .sendTitle("&3&lJadAPI", "&3Help &b&ldisplayed &ein &3chat&e!", 5, 10, 5);
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eNo Permission!"));
        }
    }
}
