package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import org.bukkit.command.CommandSender;

public class AllowCommand {

    public static boolean allow = false;

    public AllowCommand(CommandSender s) {
        new JSender(s).ifConsole(console -> {
            allow = !allow;
            if (allow) {
                console.sendMessage("&3&lJadAPI &7>> &eUsing the &3&lTest Command &ehas been &ballowed&e!");
            } else {
                console.sendMessage("&3&lJadAPI &7>> &eUsing the &3&lTest Command &ehas been &bdisallowed&e!");
            }
        }, sender -> {
            sender.sendMessage("&3&lJadAPI &7>> &eYou may not &ballow&e. Only the &c&lConsole&e!");
        });
    }
}
