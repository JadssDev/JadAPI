package dev.jadss.jadapi.bukkitImpl.menu;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.AbstractInventory;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;
import dev.jadss.jadapi.bukkitImpl.menu.context.ForwardedContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.ClickContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.CloseContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.MainContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.OpenContext;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.management.JQuickEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a menu.
 */
public abstract class AbstractMenu<A extends AbstractMenu<A, T, K>, T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> {

    private final JadAPIPlugin registerer;
    private final T inventory;
    private final List<MainContext<T, K>> contexts = new ArrayList<>();

    private JQuickEvent<InventoryClickEvent> clickEventListener = null;
    private JQuickEvent<InventoryCloseEvent> closeEventListener = null;

    public AbstractMenu(JadAPIPlugin registerer, T initialInventory) {
        this.registerer = registerer;
        this.inventory = initialInventory.copy();
    }

    /**
     * Register this Menu to JadEventHandler
     * @param enable if this Menu should be registered.
     * @return itself.
     */
    public A register(boolean enable) {
        if (!this.registerer.isRegistered()) {
            throw new JException(JException.Reason.UNREGISTERED);
        }

        if (enable) {
            if (this.isRegistered())
                return (A) this;

            this.clickEventListener = new JQuickEvent<>(this.registerer, InventoryClickEvent.class, EventPriority.MONITOR, (event) -> {
                if (cancelEveryClick())
                    event.setCancelled(true);

                this.onClick(new ClickContext<>(this.getContext(event.getWhoClicked().getUniqueId()), event));
            }, -1, -1, (e) -> this.contexts.stream()
                    .anyMatch(context -> context.getPlayer().getPlayer().getUniqueId().equals(e.getWhoClicked().getUniqueId())), JQuickEvent.generateID())
                    .register(true);

            this.closeEventListener = new JQuickEvent<>(this.registerer, InventoryCloseEvent.class, EventPriority.MONITOR, (event) -> {
                this.onClose(new CloseContext<>(this.getContext(event.getPlayer().getUniqueId()), event));
            }, -1, -1, (e) -> this.contexts.stream()
                    .anyMatch(context -> context.getPlayer().getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId())), JQuickEvent.generateID())
                    .register(true);

            this.onRegister();

            JadAPI.getInstance().getRegisterer().registerMenu(this);
        } else {
            for (MainContext<T, K> context : contexts)
                context.getPlayer().getPlayer().closeInventory();

            this.clickEventListener.register(false);
            this.closeEventListener.register(false);

            this.onUnregister();

            JadAPI.getInstance().getRegisterer().unregisterMenu(this);
        }

        return (A) this;
    }

    /**
     * Represents if this menu is registered.
     * @return if this menu is registered.
     */
    public boolean isRegistered() {
        return JadAPI.getInstance().getInformationManager().getMenus().contains(this);
    }

    /**
     * Get the list of contexts that currently exist.
     * @return the list of contexts.
     */
    public List<MainContext<T, K>> getContexts() {
        return new ArrayList<>(contexts);
    }

    /**
     * Get the context of a player.
     * @param player the player to get the context from.
     * @return the context of the player or null if he doesn't have this menu open.
     */
    public MainContext<T, K> getContext(JPlayer player) {
        return this.getContext(player.getPlayer().getUniqueId());
    }

    /**
     * Get the context of a player.
     * @param player the player to get the context from.
     * @return the context of the player or null if he doesn't have this menu open.
     */
    public MainContext<T, K> getContext(UUID player) {
        return this.contexts.stream()
                .filter(context -> context.getPlayer().getPlayer().getUniqueId().equals(player))
                .findFirst().orElse(null);
    }

    /**
     * Open this specific menu for this player.
     * @param player the player to open it for.
     * @return the context of the open menu, or null if the opening was cancelled.
     */
    public MainContext<T, K> openMenu(JPlayer player) {
        return this.openMenu(player, null);
    }

    public MainContext<T, K> openMenu(JPlayer player, ForwardedContext forwardedContext) {
        MainContext<T, K> context = new MainContext<>(player, this.reuseInventory() ? inventory : inventory.copy());

        OpenContext<T, K> openingContext = new OpenContext<>(context, forwardedContext);
        this.onOpen(openingContext);

        if (openingContext.isCancelled())
            return null;

        this.contexts.add(context);

        player.openInventory(context.getInventory());
        return context;
    }

    protected abstract void onRegister();

    protected abstract void onUnregister();

    /**
     * Called when a menu is opened.
     * @param context the context of this opening.
     */
    protected abstract void onOpen(OpenContext<T, K> context);

    /**
     * Called when a menu is clicked.
     * @param context The context of the click.
     */
    protected abstract void onClick(ClickContext<T, K> context);

    /**
     * Called when a menu is closed.
     * @param context the context of the closure.
     */
    protected abstract void onClose(CloseContext<T, K> context);


    /**
     * Do we cancel every click?
     * @return if we cancel every click on the menus.
     */
    protected abstract boolean cancelEveryClick();

    /**
     * Do we reuse the same inventory for everyone?
     * @return if we reuse the same inventory, so we don't call {@link AbstractInventory#copy()} for a new menu opening.
     */
    protected abstract boolean reuseInventory();

    /**
     * Get the registerer of this menu.
     * @return the registerer of this menu.
     */
    public JadAPIPlugin getRegisterer() {
        return registerer;
    }
}
