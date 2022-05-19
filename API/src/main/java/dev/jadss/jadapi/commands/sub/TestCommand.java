package dev.jadss.jadapi.commands.sub;

import org.bukkit.command.CommandSender;

public class TestCommand {

    public TestCommand(CommandSender s) {
        if (!s.hasPermission("JadAPI.test")) {
            s.sendMessage("You don't have permission to use this command!");
            return;
        }
    }
}
