package dev.jadss.jadapi.commands;

import dev.jadss.jadapi.commands.sub.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class JadAPICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            new WelcomeCommand(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            new HelpCommand(sender);
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("actionbar")) {
            new ActionBarCommand(sender, args);
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("title")) {
            new TitleCommand(sender, args);
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("subtitle")) {
            new SubTitleCommand(sender, args);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("type_racer")) {
            new TypeRacerCommand(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            new InfoCommand(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            new VersionCommand(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("allow")) {
            new AllowCommand(sender);
        } else if (args[0].equalsIgnoreCase("test")) {
            new TestCommand(sender, Arrays.stream(args).skip(1).toArray(String[]::new));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("debug")) {
            new DebugCommand(sender, Arrays.stream(args).skip(1).toArray(String[]::new));
        } else {
            new WelcomeCommand(sender);
        }

        return true;
    }
}
