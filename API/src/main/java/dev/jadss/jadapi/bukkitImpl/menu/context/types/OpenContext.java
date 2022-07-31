package dev.jadss.jadapi.bukkitImpl.menu.context.types;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.AbstractInventory;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;
import dev.jadss.jadapi.bukkitImpl.menu.context.Context;
import dev.jadss.jadapi.bukkitImpl.menu.context.ForwardedContext;

/**
 * Represents the context of a menu opening for a player.
 * @param <T> the type of inventory.
 * @param <K> the type of item stack.
 */
public class OpenContext<T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> implements Context<T, K> {

    private final MainContext<T, K> context;
    private final ForwardedContext forwardedContext;
    private boolean cancelled = false;

    public OpenContext(MainContext<T, K> context, ForwardedContext forwardedContext) {
        this.context = context;
        this.forwardedContext = forwardedContext;
    }

    /**
     * Check if this opening of this menu is cancelled.
     * @return if the opening is cancelled.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Set this opening cancelled or not.
     * @param cancelled represents the new value of the cancelled status.
     * @return itself.
     */
    public OpenContext<T, K> setCancelled(boolean cancelled) {
        this.cancelled = cancelled;

        return this;
    }

    /**
     * <p>Toggle the cancel</p>
     * <p>If it wasn't cancelled, it now is cancelled.</p>
     * <p>If it was cancelled, it is now not cancelled.</p>
     * @return itself.
     */
    public OpenContext<T, K> toggleCancelled() {
        return this.setCancelled(!this.isCancelled());
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
