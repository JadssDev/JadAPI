package dev.jadss.jadapi.interfaces.managing;

import dev.jadss.jadapi.bukkitImpl.enchantments.EnchantmentInstance;
import dev.jadss.jadapi.bukkitImpl.enchantments.JEnchantmentInfo;
import dev.jadss.jadapi.bukkitImpl.menu.AbstractMenu;
import dev.jadss.jadapi.bukkitImpl.misc.JHologram;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.management.JPacketHook;
import dev.jadss.jadapi.management.JQuickEvent;

/**
 * Represents the registerer of the JadAPI.
 */
public interface JRegisterer {

    /**
     * Register an enchantment using JadAPI.
     * @param info The EnchantmentInfo!
     * @return The EnchantmentInstance.
     * @see org.bukkit.enchantments.Enchantment
     * @see EnchantmentInstance
     * @throws JException if the JadAPIPlugin is unregistered!
     */
    EnchantmentInstance registerEnchantment(JEnchantmentInfo info);

    /**
     * Unregister an Enchantment from the database of enchantments of bukkit.
     * @param instance the Enchantment in specific.
     */
    void unregisterEnchantment(EnchantmentInstance instance);

    /**
     * Register a QuickEvent!
     * @param quickEvent the QuickEvent to register.
     * @return The QuickEvent specified.
     */
    JQuickEvent<?> registerQuickEvent(JQuickEvent<?> quickEvent);

    /**
     * Unregister a QuickEvent.
     * @param quickEvent the QuickEvent to unregister!
     */
    void unregisterQuickEvent(JQuickEvent<?> quickEvent);

    /**
     * Register a PacketHook!
     * @param packetEvent the PacketHook to register.
     * @return The PacketHook specified.
     */
    JPacketHook registerPacketHook(JPacketHook packetEvent);

    /**
     * Unregister a PacketHook.
     * @param packetHook the PacketHook to unregister!
     */
    void unregisterPacketHook(JPacketHook packetHook);

    /**
     * Register a Hologram!
     * @param hologram the Hologram to register.
     * @return The Hologram specified.
     */
    JHologram registerHologram(JHologram hologram);

    /**
     * Unregister a Hologram!
     * @param hologram the Hologram to unregister!
     */
    void unregisterHologram(JHologram hologram);

    /**
     * Register a Menu!
     * @param menu the Menu to register.
     * @return The Menu specified.
     */
    <A extends AbstractMenu<?, ?, ?>> A registerMenu(A menu);

    /**
     * Unregister a Menu.
     * @param menu the Menu to unregister!
     */
    <A extends AbstractMenu<?, ?, ?>> void unregisterMenu(A menu);
}
