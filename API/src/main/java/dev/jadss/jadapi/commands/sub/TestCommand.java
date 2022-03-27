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
        new JSender(s).ifPlayer(jPlayer -> {
            jPlayer.sendMessage("&ctesting!");
            jPlayer.sendMessage(JWorld.getJWorlds().stream().map(JWorld::getName).collect(Collectors.toCollection(ArrayList::new)).toString());
            jPlayer.sendMessage(Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toCollection(ArrayList::new)).toString());
        }, sender -> System.out.println("You see, you're not a player, you can't execute this!"));
    }
}
