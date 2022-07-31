package dev.jadss.jadapi.bukkitImpl.menu.context;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.AbstractInventory;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.MainContext;

/**
 * Represents a context of a menu.
 */
public interface Context<T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> {

    /**
     * Get the Main context.
     * @return the context.
     */
    MainContext<T, K> getMainContext();

    /**
     * Which player does this context refer to?
     * @return the player from this context.
     */
    JPlayer getPlayer();

}
