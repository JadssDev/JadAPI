package dev.jadss.jadapi.bukkitImpl.item;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.jadss.jadapi.bukkitImpl.entities.subEntities.JItem;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.interfaces.inventory.Enchantable;
import dev.jadss.jadapi.interfaces.inventory.JItemStackAbstract;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JItemStack implements Copyable<JItemStack>, Enchantable<JItemStack> {

    private ItemStack item;

    /**
     * Create an JItemStack from a JMaterial.
     * @param material the JMaterial object.
     */
    public JItemStack(JMaterial material) {
        Map.Entry<Material, Byte> entry = material.getMaterial(JMaterial.Type.ITEM);
        if (entry == null)
            throw new JException(JException.Reason.VALUE_IS_NULL);
        this.item = new ItemStack(entry.getKey(), 1, entry.getValue());
    }

    /**
     * Create a JItemStack from a Material.
     * @param material the Material object.
     */
    public JItemStack(Material material) {
        this.item = new ItemStack(material, 1);
    }

    /**
     * Create a JItemStack from an existing ItemStack.
     * @param item the ItemStack object.
     */
    public JItemStack(ItemStack item) {
        this.item = item;
    }

    /**
     * Create a JItemStack from an abstract class.
     * @param abstractItem the class that extends JItemStackAbstract
     */
    public JItemStack(JItemStackAbstract abstractItem) {
        if(abstractItem != null) {

            Map.Entry<Material, Byte> entry = abstractItem.getMaterial().getMaterial(JMaterial.Type.ITEM);
            this.item = new ItemStack(entry.getKey(), abstractItem.getAmount(), entry.getValue());
            ItemMeta meta = this.item.getItemMeta();

            List<String> lore = new ArrayList<>();
            for(String line : abstractItem.getLore())
                lore.add(ChatColor.translateAlternateColorCodes('&', line));

            meta.setLore(lore);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', abstractItem.getDisplayName()));

            HashMap<Enchantment, Integer> hash = abstractItem.getEnchantments();
            for(Enchantment ench : hash.keySet()) meta.addEnchant(ench, hash.get(ench), true);

            if(abstractItem.getItemFlags() != null) meta.addItemFlags(abstractItem.getItemFlags().toArray(new ItemFlag[0]));

            this.item.setItemMeta(meta);
            this.item = abstractItem.extraSteps(new JItemStack(this.item)).buildItemStack();

        } else throw new JException(JException.Reason.INVALID_CLASS);
    }

    /**
     * Set the display name of the JItemStack.
     * @param displayName the new DisplayName.
     * @return itself.
     */
    public JItemStack setDisplayName(String displayName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Set the display name (WITH NO COLORS) of the JItemStack.
     * @param displayName the new DisplayName.
     * @return itself.
     */
    public JItemStack setNoColorDisplayName(String displayName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Set the lore of the JItemStack.
     * @param lore the new lore in List.
     * @return itself.
     */
    public JItemStack setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore2 = new ArrayList<>();

        for(String string : lore) lore2.add(ChatColor.translateAlternateColorCodes('&', string));

        meta.setLore(lore2);
        item.setItemMeta(meta);

        return this;
    }

    /**
     * Get the lore of the JItemStack.
     * @return the Lore as {@link List}.
     */
    public List<String> getLore() {
        return this.item.getItemMeta().getLore();
    }

    /**
     * Set the lore of the JItemStack.
     * @param lore the new lore in a String.
     * @return itself.
     */
    public JItemStack setLore(String... lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore2 = new ArrayList<>();

        for(String string : lore) lore2.add(ChatColor.translateAlternateColorCodes('&', string));

        meta.setLore(lore2);
        item.setItemMeta(meta);

        return this;
    }

    /**
     * Set the lore (WITH NO COLORS) of the JItemStack.
     * @param lore the new lore in a String.
     * @return itself.
     */
    public JItemStack setNoColorLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Set the amount of the stack in the JItemStack.
     * @param amount the new amount
     * @return itself.
     */
    public JItemStack setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Add a flag (or more) to an item.
     * @param flags The flag(s) to add.
     * @return itself.
     */
    public JItemStack addFlags(ItemFlag... flags) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(flags);
        this.item.setItemMeta(meta);
        return this;
    }

    /**
     * Remove a flag (or more) to an item.
     * @param flags The flag(s) to removeS.
     * @return itself.
     */
    public JItemStack removeFlags(ItemFlag... flags) {
        ItemMeta meta = this.item.getItemMeta();
        meta.removeItemFlags(flags);
        this.item.setItemMeta(meta);
        return this;
    }

    /*NBT CHANGES TO ITEM*/

    /**
     * Set a string in the nbt of the item
     * @param key the key of the nbt.
     * @param value the value of the nbt.
     * @return itself.
     * @exception JException if the nbt key == null
     */
    public JItemStack setNBTString(String key, String value) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTItem item = new NBTItem(this.item);
        item.setString(key, value);
        this.item = item.getItem();

        return this;
    }

    /**
     * Get a string in the nbt of the item.
     * @param key the key of the nbt.
     * @return The string if any.
     * @exception JException if the nbt key == null
     */
    public String getNBTString(String key) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTCompound compound = new NBTItem(this.item);

        return compound.getString(key);
    }


    /**
     * Set a boolean in the nbt of the item
     * @param key the key of the nbt.
     * @param value the value of the nbt.
     * @return itself.
     * @exception JException if the nbt key == null
     */
    public JItemStack setNBTBoolean(String key, boolean value) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTItem item = new NBTItem(this.item);
        item.setBoolean(key, value);
        this.item = item.getItem();

        return this;
    }

    /**
     * Get a boolean in the nbt of the item.
     * @param key the key of the nbt.
     * @return The boolean if any.
     * @exception JException if the nbt key == null
     */
    public boolean getNBTBoolean(String key) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTCompound compound = new NBTItem(this.item);

        return compound.getBoolean(key);
    }


    /**
     * Set a byte in the nbt of the item
     * @param key the key of the nbt.
     * @param value the value of the nbt.
     * @return itself.
     * @exception JException if the nbt key == null
     */
    public JItemStack setNBTByte(String key, byte value) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTItem item = new NBTItem(this.item);
        item.setByte(key, value);
        this.item = item.getItem();

        return this;
    }

    /**
     * Get a byte in the nbt of the item.
     * @param key the key of the nbt.
     * @return The byte if any.
     * @exception JException if the nbt key == null
     */
    public byte getNBTByte(String key) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTCompound compound = new NBTItem(this.item);

        return compound.getByte(key);
    }


    /**
     * Set a integer in the nbt of the item
     * @param key the key of the nbt.
     * @param value the value of the nbt.
     * @return itself.
     * @exception JException if the nbt key == null
     */
    public JItemStack setNBTInteger(String key, int value) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTItem item = new NBTItem(this.item);
        item.setInteger(key, value);
        this.item = item.getItem();

        return this;
    }

    /**
     * Get a integer in the nbt of the item.
     * @param key the key of the nbt.
     * @return The integer if any.
     * @exception JException if the nbt key == null
     */
    public int getNBTInteger(String key) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTCompound compound = new NBTItem(this.item);

        return compound.getInteger(key);
    }


    /**
     * Set a double in the nbt of the item
     * @param key the key of the nbt.
     * @param value the value of the nbt.
     * @return itself.
     * @exception JException if the nbt key == null
     */
    public JItemStack setNBTDouble(String key, double value) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTItem item = new NBTItem(this.item);
        item.setDouble(key, value);
        this.item = item.getItem();

        return this;
    }

    /**
     * Get a double in the nbt of the item.
     * @param key the key of the nbt.
     * @return The double if any.
     * @exception JException if the nbt key == null
     */
    public double getNBTDouble(String key) {
        if(key == null) throw new JException(JException.Reason.BAD_NBT);

        NBTCompound compound = new NBTItem(this.item);

        return compound.getDouble(key);
    }

    /*NBT CHANGES TO ITEM*/

    /**
     * Drop this JItemStack in a location.
     * @param location The location to spawn it in.
     * @return a JItem Object.
     */
    public JItem drop(Location location) {
        if(location == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        return new JItem(location.getWorld().dropItemNaturally(location, item));
    }

    /**
     * Get the ItemStack of this JItemStack.
     * @return The ItemStack.
     */
    public ItemStack buildItemStack() { return this.item; }

    /**
     * Set the item stack of this JItemStack.
     * @param item The item to set it to.
     * @return itself.
     */
    public JItemStack setItemStack(ItemStack item) { this.item = item; return this; }

    /**
     * Copy this JItemStack.
     * @return a new JItemStack.
     */
    public JItemStack copy() { return new JItemStack(item.clone()); }

    /**
     * Add an enchantment to this Item.
     * @param enchantment the Enchantment.
     * @param level the level.
     * @return itself.
     */
    @Override
    public JItemStack addEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = this.item.getItemMeta();

        meta.removeEnchant(enchantment);
        meta.addEnchant(enchantment, level, true);
        this.item.setItemMeta(meta);

        return this;
    }

    /**
     * Remove an enchantment from this item, no matter it's level.
     * @param enchantment The enchantment to remove.
     * @return itself.
     */
    @Override
    public JItemStack removeEnchantment(Enchantment enchantment) {
        ItemMeta meta = this.item.getItemMeta();

        meta.removeEnchant(enchantment);
        this.item.setItemMeta(meta);

        return this;
    }

    /**
     * Get the Enchantment List of this item!
     * @return the List as {@link List}!
     */
    @Override
    public Map<Enchantment, Integer> getEnchantmentList() {
        return this.item.getItemMeta().getEnchants();
    }
}
