package dev.jadss.jadapi.bukkitImpl.misc;

import dev.jadss.jadapi.annotations.ForChange;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@ForChange(isMajor = true, expectedVersionForChange = "1.24.1", reason = "Feature more methods and complete rewrite.")
public class JSender {

    private final CommandSender sender;

    /**
     * Create a {@link JSender} object.
     *
     * @param sender the CommandSender.
     */
    public JSender(CommandSender sender) {
        if (sender == null) throw new JException(JException.Reason.SENDER_IS_NULL);

        this.sender = sender;
    }

    public JSender sendMessage(String message) {
        this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        return this;
    }

    /**
     * This method executes the Consumers if the player is OR is NOT a player.
     *
     * @param player if the sender is a player this will be executed.
     * @param sender if it is not a player, this will be executed.
     * @return itself.
     */
    public JSender ifPlayer(Consumer<JPlayer> player, Consumer<JSender> sender) {
        if (this.sender instanceof Player)
            player.accept(new JPlayer((Player) this.sender));
        else
            sender.accept(this);
        return this;
    }

    /**
     * This method executes the Consumers if the player is OR is NOT a player.
     *
     * @param console if the sender is the server console this will be executed.
     * @param sender  if it is not the server console, this will be executed.
     * @return itself.
     */
    public JSender ifConsole(Consumer<JSender> console, Consumer<JSender> sender) {
        if (this.sender instanceof ConsoleCommandSender)
            console.accept(this);
        else
            sender.accept(this);
        return this;
    }

    /**
     * This method executes the Consumers if the player is OR is NOT a player.
     *
     * @param command_block if the sender is a {@link org.bukkit.block.CommandBlock} this will be executed.
     * @param sender        if he is not a command block, this will be executed.
     * @return itself.
     */
    public JSender ifCommandBlock(Consumer<JSender> command_block, Consumer<JSender> sender) {
        if (this.sender instanceof ConsoleCommandSender)
            command_block.accept(this);
        else
            sender.accept(this);
        return this;
    }

    /**
     * Get the sender from bukkit.
     *
     * @return the sender.
     */
    public CommandSender getSender() {
        return this.sender;
    }
}
