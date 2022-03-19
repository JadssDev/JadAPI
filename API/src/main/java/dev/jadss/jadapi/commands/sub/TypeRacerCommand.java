package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.JadAPIAsJPlugin;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.sub.JSignRegister;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class TypeRacerCommand {

    public TypeRacerCommand(CommandSender sender) {
        if(sender instanceof Player) {
            JPlayer player = new JPlayer((Player) sender);

            String[] f = new String[] { "potato", "baguette", "banana", "apple", "strawberry", "tomato", "pineapple", "kiwi", "blueberry", "blackberry", "mango", "grapes", "pear", "orange"};
            int i = new Random().nextInt(f.length);
            String word = f[i];

            player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &b&lType &f" + word + " &ein the &3&lsign&e."));

            player.openTypeSign(new JSignRegister(s -> {
                if(s[0].equalsIgnoreCase(word)) {
                    player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eYou have &b&ltyped &f" + word + " &aCorrectly&e! &eNice!"));
                } else {
                    player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &c&lWrong&e! &eIt is &f" + word + " &eyou &3Dumbo&e!"));
                }
            }, new JadAPIAsJPlugin(), "", "^^^^", "Type the", "word in chat"));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &e You &c&lcan't &buse &ethis &3command&e!"));
        }
    }
}
