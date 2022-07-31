package dev.jadss.jadapi.bukkitImpl.menu.context.types;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.AbstractInventory;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;
import dev.jadss.jadapi.bukkitImpl.menu.context.Context;
import dev.jadss.jadapi.bukkitImpl.menu.context.SavedContext;

/**
 * Represents the context of a player with an open menu inventory instance.
 * @param <T> the inventory instance used.
 * @param <K> the item stack instance used by the inventory.
 */
public class MainContext<T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> implements Context<T, K> {

    private final JPlayer player;
    private final T inventory;
    private final SavedContext<T, K> context;

    public MainContext(JPlayer player, T inventory) {
        this.player = player;
        this.inventory = inventory;
        this.context = new SavedContextImpl<>(this, player);
    }

    /**
     * Represents the saved variables of the current context.
     * @return the saved context.
     */
    public SavedContext<T, K> getSavedContext() {
        return context;
    }

    /**
     * Represents the inventory for this player's open inventory context.
     * @return the inventory.
     */
    public T getInventory() {
        return inventory;
    }

    @Override
    public MainContext<T, K> getMainContext() {
        return this;
    }

    @Override
    public JPlayer getPlayer() {
        return player;
    }
}
