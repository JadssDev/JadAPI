package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.management.labymod.displays.EconomyPacket;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WelcomeCommand {

    private static final Map<UUID, Long> PLAYER_DELAYS = new HashMap<>();

    public WelcomeCommand(CommandSender sender) {
        if (sender instanceof Player) {
            if (PLAYER_DELAYS.containsKey(((Player) sender).getUniqueId()) && PLAYER_DELAYS.get(((Player) sender).getUniqueId()) + 2500L > System.currentTimeMillis()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &ePlease &3wait &ebefore using this &bcommand &aagain&e!"));
                return;
            } else {
                PLAYER_DELAYS.put(((Player) sender).getUniqueId(), System.currentTimeMillis());
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThis &b&lserver &eis &brunning &3&lJadAPI&e!"));
        if(sender instanceof Player) {
            JPlayer player = new JPlayer((Player) sender);
            if (JadAPI.getInstance().getLabyService().isUser(player)) {
                showCoolStuff(player);
                JadAPI.getInstance().getLabyService().sendPacket(player, new EconomyPacket(Collections.singletonMap("cash",
                        new EconomyPacket.EconomyType(true,
                                Integer.parseInt(JadAPI.getInstance().getDescription().getVersion().replaceAll("[^0-9]", "")),
                                null, null))));
                Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                    JadAPI.getInstance().getLabyService().sendPacket(player, new EconomyPacket(Collections.singletonMap("cash", new EconomyPacket.EconomyType(true, 0, null, null))));
                    Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
                        JadAPI.getInstance().getLabyService().sendPacket(player, new EconomyPacket(Collections.singletonMap("cashst", new EconomyPacket.EconomyType(false, 0, null))));
                    }, 20L);
                }, 500L);
            } else {
                //No LabyMod installed, Sadge!
                showCoolStuff(player);
            }
        }
    }

    public static void showCoolStuff(JPlayer player) {
        player.sendActionBar("&eThis &b&lserver &eis &brunning &3&lJadAPI " + JadAPI.getInstance().getDescription().getVersion() + " &eon &3&lNMS &f" + JReflection.getNMSVersion() + "&e!")
                .sendTitle("&3&lJadAPI", "&bAdvanced &3&lAPI", 5, 80, 10);
        Bukkit.getScheduler().runTaskLater(JadAPI.getInstance(), () -> {
            long id = -1; //todo: get id
            if(id != -1) {
                player.sendActionBar("&eThis &3server &eis &b&lconnected &eunder &3JadID &a-> &f" + id);
            } else {
                player.sendActionBar("&eThis &3server &eis &b&ldisconnected &efrom &3&lJad Servers&e!");
            }
        }, 80);
    }
}
