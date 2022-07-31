package dev.jadss.jadapi.bukkitImpl.menu.context.types;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.AbstractInventory;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;
import dev.jadss.jadapi.bukkitImpl.menu.context.Context;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Represents the context of a click of the player on a menu.
 * @param <T> the type of inventory.
 * @param <K> the type of item stack.
 */
public class ClickContext<T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> implements Context<T, K> {

    private final MainContext<T, K> context;
    private final InventoryClickEvent event;

    public ClickContext(MainContext<T, K> context, InventoryClickEvent event) {
        this.context = context;

        this.event = event;
    }

    /**
     * Get the event of this context.
     * @return the event.
     */
    public InventoryClickEvent getEvent() {
        return event;
    }

    @Override
    public MainContext<T, K> getMainContext() {
        return context;
    }

    @Override
    public JPlayer getPlayer() {
        return context.getPlayer();
    }
}