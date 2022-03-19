package dev.jadss.jadapi.interfaces.inventory;

import org.bukkit.enchantments.Enchantment;

import java.util.Map;

/**
 * Represents an object that can have <b>enchantments</b>
 * @param <R> The return type.
 */
public interface Enchantable<R> {

    /**
     * Add an enchantment.
     * @param enchantment the Enchantment.
     * @param level the level.
     * @return the Return type specified by the class.
     */
    R addEnchantment(Enchantment enchantment, int level);

    /**
     * Remove an enchantment.
     * @param enchantment The enchantment to remove.
     * @return the Return type specified by the class.
     */
    R removeEnchantment(Enchantment enchantment);

    /**
     * Get the enchantments present in this.
     * @return the Enchantments as {@link Map} where the Enchantment is key and the Integer represents the Enchantment level!
     */
    Map<Enchantment, Integer> getEnchantmentList();
}
