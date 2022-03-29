package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import dev.jadss.jadapi.bukkitImpl.misc.JWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TestCommand {

    public TestCommand(CommandSender s) {
        if (!s.hasPermission("JadAPI.test")) {
            s.sendMessage("You don't have permission to use this command!");
            return;
        }
    }
}
