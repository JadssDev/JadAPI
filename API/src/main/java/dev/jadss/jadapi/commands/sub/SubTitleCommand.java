package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SubTitleCommand {

    public SubTitleCommand(CommandSender sender, String[] args) {
        if(sender instanceof ConsoleCommandSender || sender.hasPermission("JadAPI.sendTitle")) {
            String message = Arrays.stream(args).skip(2).map(s -> " " + s).collect(Collectors.joining()).substring(1);
            if(args[1].equalsIgnoreCase("*")) {
                for(JPlayer p : JPlayer.getJPlayers())
                    p.sendTitle(null, ChatColor.translateAlternateColorCodes('&', message));
            } else {
                Player player = Bukkit.getPlayer(args[1]);
                if(player != null && player.isOnline()) {
                    new JPlayer(player).sendTitle(null, ChatColor.translateAlternateColorCodes('&', message));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cNo &esuch &3&lplayer &bfound&e."));
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eNo Permission!"));
        }
    }
}
