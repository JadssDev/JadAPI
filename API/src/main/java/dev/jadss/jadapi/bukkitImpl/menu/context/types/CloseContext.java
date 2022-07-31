package dev.jadss.jadapi.bukkitImpl.menu.context.types;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.AbstractInventory;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;
import dev.jadss.jadapi.bukkitImpl.menu.context.Context;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Represents the context of the closure of the player on a menu.
 * @param <T> the type of inventory.
 * @param <K> the type of item stack.
 */
public class CloseContext<T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> implements Context<T, K> {

    private final MainContext<T, K> context;
    private final InventoryCloseEvent event;

    public CloseContext(MainContext<T, K> context, InventoryCloseEvent event) {
        this.context = context;
        this.event = event;
    }

    /**
     * The event that caused this context.
     * @return the event.
     */
    public InventoryCloseEvent getEvent() {
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
