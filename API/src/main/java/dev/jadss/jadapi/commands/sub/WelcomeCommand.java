package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.management.JConnection;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WelcomeCommand {

    public WelcomeCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThis &b&lserver &eis &brunning &3&lJadAPI&e!"));
        if(sender instanceof Player) {
            new JPlayer((Player) sender)
                    .sendActionBar("&eThis &b&lserver &eis &brunning &3&lJadAPI &eon &3&lNMS &f" + JReflection.getNMSVersion() + "&e!")
                    .sendTitle("&3&lJadAPI", "&bAdvanced &3&lAPI", 5, 80, 10);
            Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                long id = JConnection.getJadServersId();
                if(id != -1) {
                    new JPlayer((Player) sender).sendActionBar("&eThis &3server &eis &b&lconnected &eunder &3JadID &a-> &f" + id);
                } else {
                    new JPlayer((Player) sender).sendActionBar("&eThis &3server &eis &b&ldisconnected &efrom &3&lJad Servers&e!");
                }
            }, 40);
        }
    }
}
