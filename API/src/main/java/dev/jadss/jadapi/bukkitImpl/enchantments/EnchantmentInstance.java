package dev.jadss.jadapi.bukkitImpl.enchantments;

import org.bukkit.enchantments.Enchantment;

/**
 * Represents an instance of an Enchantment.
 */
public interface EnchantmentInstance {

    /**
     * Get the information of this EnchantmentInstance.
     * @return the Information in a {@link JEnchantmentInfo}
     */
    JEnchantmentInfo getEnchantmentInformation();

    /**
     * Get this EnchantmentInstance as an Enchantment from bukkit!
     * @return the Enchantment.
     */
    Enchantment asEnchantment();

    /**
     * Check if this enchantment is registered!
     * @return if the enchantment is registered!
     */
    boolean isRegistered();

    /**
     * Unregister this enchantment, Unregistered enchantments <b>do not work</b> and will throw <b>exceptions</b> if any of their methods are <b>used</b>.
     */
    void unregister();
}
