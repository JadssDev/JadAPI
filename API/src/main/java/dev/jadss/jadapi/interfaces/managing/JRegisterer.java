package dev.jadss.jadapi.interfaces.managing;

import dev.jadss.jadapi.bukkitImpl.enchantments.EnchantmentInstance;
import dev.jadss.jadapi.bukkitImpl.enchantments.JEnchantmentInfo;
import dev.jadss.jadapi.exceptions.JException;

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
}
