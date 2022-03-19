package dev.jadss.jadapi.interfaces.inventory;

import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;
import java.util.List;

public abstract class JItemStackAbstract {

    /**
     * DisplayName of the item
     * @return the DisplayName
     */
    public abstract String getDisplayName();

    /**
     * Lore of the item.
     * @return the lore.
     */
    public abstract List<String> getLore();

    /**
     * ItemFlags of the item
     * @return the ItemFlags.
     */
    public abstract List<ItemFlag> getItemFlags();

    /**
     * Material of the item.
     * @return the Material of the item.
     */
    public abstract JMaterial getMaterial();

    /**
     * Amount of the item
     * @return amount of items.
     */
    public abstract int getAmount();

    /**
     * The enchantments the item should have.
     * @return the enchantments or null if none.
     */
    public abstract HashMap<Enchantment, Integer> getEnchantments();

    /**
     * Any extra steps that we should apply to the item?
     * @param stack the item at the start.
     * @return the final item.
     */
    public abstract JItemStack extraSteps(JItemStack stack);
}
