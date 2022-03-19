package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import org.bukkit.command.CommandSender;

public class TestCommand {

    public TestCommand(CommandSender s, String[] args) {
        new JSender(s).ifPlayer(jPlayer -> {
            jPlayer.sendMessage("bruh!");
        }, sender -> System.out.println("You see, you're not a player, you can't execute this!"));
    }
}
