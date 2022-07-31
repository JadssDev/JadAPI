package dev.jadss.jadapi.bukkitImpl.item;

import dev.jadss.jadapi.interfaces.Copyable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an Item in JadAPI.
 * @param <T> the instance that the NBTInfo will have
 * @see JItemStack
 */
public abstract class AbstractItemStack<T extends AbstractItemStack<T>> implements Copyable<T> {

    protected final ItemStack item;

    private ItemNBT<T> nbt = null;

    public AbstractItemStack(ItemStack item) {
        if (item == null)
            item = new ItemStack(AIR_MATERIAL.getMaterial(JMaterial.Type.ITEM).getKey());

        this.item = item;
    }

    /**
     * Get the type of this Item.
     *
     * @return the type pf this item.
     */
    public JMaterial getType() {
        return JMaterial.getRegistryMaterials().find(this.item.getType(), this.item.getData().getData());
    }

    /**
     * Set the type of this item.
     *
     * @param material the new type of this item.
     * @return itself.
     */
    public T setType(JMaterial material) {
        Map.Entry<Material, Byte> entry = material.getMaterial(JMaterial.Type.ITEM);
        this.item.setType(entry.getKey());
        this.item.setData(new MaterialData(entry.getKey(), entry.getValue()));

        this.nbt = null;

        return (T) this;
    }

    /**
     * Set the display name of this item.
     *
     * @param displayName the new display name.
     * @return itself.
     */
    public T setDisplayName(String displayName) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        this.item.setItemMeta(meta);

        return (T) this;
    }

    /**
     * Get the display name of this item.
     * @return the display name of this item.
     */
    public String getDisplayName() {
        return this.item.getItemMeta().getDisplayName();
    }

    /**
     * Checks if this item has lore.
     * @return if the item has lore.
     */
    public boolean hasLore() {
        return this.item.getItemMeta().hasLore();
    }

    /**
     * Get the lore as a List.
     * @return the lore.
     */
    public List<String> getLoreAsList() {
        return this.item.getItemMeta().getLore();
    }

    /**
     * Get the lore as an Array.
     * @return the lore.
     */
    public String[] getLoreAsArray() {
        return this.item.getItemMeta().getLore().toArray(new String[0]);
    }

    /**
     * Set the lore of this item.
     * @param lore the lore.
     * @return itself.
     */
    public T setLore(List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();

        meta.setLore(lore.stream()
                .map(text -> ChatColor.translateAlternateColorCodes('&', text))
                .collect(Collectors.toCollection(ArrayList::new)));

        this.item.setItemMeta(meta);

        return (T) this;
    }

    /**
     * Set the lore of this item.
     * @param lore the lore.
     * @return itself.
     */
    public T setLore(String... lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(Arrays.stream(lore)
                .map(text -> ChatColor.translateAlternateColorCodes('&', text))
                .collect(Collectors.toCollection(ArrayList::new)));

        this.item.setItemMeta(meta);

        return (T) this;
    }

    /**
     * Add flags to this item.
     * @param flags the flags to add.
     * @return itself.
     */
    public T addFlags(ItemFlag... flags) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(flags);
        this.item.setItemMeta(meta);

        return (T) this;
    }

    /**
     * Remove flags from this item.
     * @param flags the flags to remove.
     * @return itself.
     */
    public T removeFlags(ItemFlag... flags) {
        ItemMeta meta = this.item.getItemMeta();
        meta.removeItemFlags(flags);
        this.item.setItemMeta(meta);

        return (T) this;
    }

    /**
     * Sets the amount of this item.
     *
     * @param amount the new amount of this item.
     * @return itself.
     */
    public T setAmount(int amount) {
        this.item.setAmount(amount);

        return (T) this;
    }

    /**
     * Gets the amount of this item.
     *
     * @return the amount of this item.
     */
    public int getAmount() {
        return this.item.getAmount();
    }

    /**
     * Get the list of enchantments of this item.
     *
     * @return the list of enchantments.
     */
    public List<Enchantment> getEnchantmentList() {
        return new ArrayList<>(this.item.getEnchantments().keySet());
    }

    /**
     * Get the map of enchantments of this item.
     *
     * @return the map of enchantments.
     */
    public Map<Enchantment, Integer> getEnchantmentMap() {
        return new HashMap<>(this.item.getEnchantments());
    }

    /**
     * Check if this item has the specified enchantment.
     *
     * @param enchantment the enchantment to check if exists.
     * @return if this enchantment exists.
     */
    public boolean hasEnchantment(Enchantment enchantment) {
        return this.item.containsEnchantment(enchantment);
    }

    /**
     * Check if this item has the specified enchantment.
     *
     * @param enchantment the enchantment to check if exists.
     * @param minLevel    the minimum level of this enchantment to be present (inclusive).
     * @return if this enchantment with the specified minimum level is present.
     */
    public boolean hasEnchantment(Enchantment enchantment, int minLevel) {
        return this.hasEnchantment(enchantment) && this.getEnchantmentLevel(enchantment) >= minLevel;
    }

    /**
     * Check if this item has the specified enchantment.
     *
     * @param enchantment the enchantment ot check if exists.
     * @param minLevel    the minimum level of this enchantment to be present (inclusive).
     * @param maxLevel    the maximum level of this enchantment to be present (exclusive).
     * @return if this enchantment is between the minimum and maximum value.
     */
    public boolean hasEnchantment(Enchantment enchantment, int minLevel, int maxLevel) {
        return this.hasEnchantment(enchantment) && (this.getEnchantmentLevel(enchantment) >= minLevel && this.getEnchantmentLevel(enchantment) < maxLevel);
    }

    /**
     * Get the level of this enchantment in this item.
     *
     * @param enchantment the enchantment to check the level for.
     * @return the level of this enchantment.
     */
    public int getEnchantmentLevel(Enchantment enchantment) {
        return this.item.getEnchantmentLevel(enchantment);
    }

    /**
     * Add an enchantment to this item.
     *
     * @param enchantment the enchantment to add.
     * @param level       the level of this enchantment.
     * @param bypassLimit do we bypass the max level of this enchantment?
     * @return itself.
     */
    public T addEnchantment(Enchantment enchantment, int level, boolean bypassLimit) {
        if (!bypassLimit)
            level = Math.min(enchantment.getMaxLevel(), level);

        this.item.addEnchantment(enchantment, level);

        return (T) this;
    }

    /**
     * Add an enchantment to this item.
     *
     * @param enchantment the enchantment to add.
     * @param level       the level of this enchantment.
     * @return itself.
     * @see AbstractItemStack#addEnchantment(Enchantment, int, boolean)
     */
    public T addEnchantment(Enchantment enchantment, int level) {
        return this.addEnchantment(enchantment, level, false);
    }

    /**
     * Remove an enchantment from this item.
     *
     * @param enchantment the enchantment to remove.
     * @return itself.
     */
    public T removeEnchantment(Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);

        return (T) this;
    }

    private JMaterial lastMaterial;

    /**
     * Returns the NBT of the item, note that to get the NBT of the item, the item has to be non-null and a type different then AIR.
     * @return the NBT.
     */
    public ItemNBT<T> getNBT() {
        if (!isValidNBTItem())
            return null;

        //Reset.
        if (lastMaterial != this.getType())
            this.nbt = null;

        if (nbt != null) {
            return nbt;
        } else {
            lastMaterial = this.getType();
            return nbt = new ItemNBT<>((T) this);
        }
    }

    private static final JMaterial AIR_MATERIAL = JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR);

    /**
     * Checks if this item is a valid NBT Item, so basically if it is not AIR and is non-null.
     * @return if it is a valid NBT item.
     */
    public boolean isValidNBTItem() {
        return this.item != null && this.getType() != AIR_MATERIAL;
    }


    /**
     * Get the bukkit item of this instance.
     * @return the bukkit item.
     */
    public ItemStack getBukkitItem() {
        return this.item;
    }
}
