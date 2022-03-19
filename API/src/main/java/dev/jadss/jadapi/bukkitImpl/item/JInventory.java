package dev.jadss.jadapi.bukkitImpl.item;

import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.interfaces.inventory.JInventoryAbstract;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JInventory {

    private final Inventory inv;

    /**
     * Create an JInventory.
     *
     * @param rows        the many rows the inventory should have.
     * @param displayName it's display name
     */
    public JInventory(int rows, String displayName) {
        if (rows <= 0) throw new JException(JException.Reason.ROWS_IS_LESS_THEN_1);

        inv = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&', displayName));
    }

    /**
     * Tired of chest inventory? k create an inventory like a hopper or furnace.
     *
     * @param invType     the InventoryType.
     * @param displayName it's display name.
     */
    public JInventory(InventoryType invType, String displayName) {
        if (invType == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        inv = Bukkit.createInventory(null, invType, ChatColor.translateAlternateColorCodes('&', displayName));
    }

    /**
     * Create a JInventory with an already existing Inventory.
     *
     * @param inventory the already Existing inventory.
     */
    public JInventory(Inventory inventory) {
        if (inventory == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        inv = inventory;
    }

    public JInventory(JInventoryAbstract abstractInventory) {
        if (abstractInventory != null) {
            Inventory firstInv;

            if (abstractInventory.getInventoryType() != null)
                firstInv = Bukkit.createInventory(null, abstractInventory.getInventoryType(), ChatColor.translateAlternateColorCodes('&', abstractInventory.getInventoryTitle()));
            else
                firstInv = Bukkit.createInventory(null, abstractInventory.getInventoryRows() * 9, ChatColor.translateAlternateColorCodes('&', abstractInventory.getInventoryTitle()));

            JInventory initial = new JInventory(firstInv);
            initial.fill(abstractInventory.getBackgroundItem());

            JInventory got = abstractInventory.setupInventory(initial);

            if (got != initial)
                throw new JException(JException.Reason.OTHER);

            this.inv = got.getInventory();
        } else throw new JException(JException.Reason.VALUE_IS_NULL);
    }

    /**
     * Set the item in a slot of the inventory.
     *
     * @param index the index of the item.
     * @param item  the JItemStack object.
     * @return itself.
     */
    public JInventory setItem(int index, JItemStack item) {
        if (item == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        inv.setItem(index, item.buildItemStack());
        return this;
    }

    /**
     * Set the item in a slot of the inventory.
     *
     * @param index the index of the item.
     * @param item  the ItemStack.
     * @return itself.
     */
    public JInventory setItem(int index, ItemStack item) {
        inv.setItem(index, item);
        return this;
    }

    /**
     * Fill the inventory with the specific item.
     *
     * @param item the JItemStack.
     * @return itself.
     */
    public JInventory fill(JItemStack item) {
        if (item == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, item.buildItemStack());

        return this;
    }

    /**
     * Fill the inventory with the specific item.
     *
     * @param item the ItemStack.
     * @return itself.
     */
    public JInventory fill(ItemStack item) {
        if (item == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, item);

        return this;
    }

    /**
     * Clear the inventory.
     *
     * @return itself.
     */
    public JInventory clearInventory() {
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, null);
        }

        return this;
    }

    /**
     * Clears the inventory, but keeps something if the predicate returns false.
     *
     * @param predicate the predicate to check if the item should be kept.
     *                  <p>Should return false to keep, and true to clear.</p>
     * @return itself.
     */
    public JInventory clearInventory(Predicate<JItemStack> predicate) {
        for (int i = 0; i < inv.getSize(); i++)
            if (predicate.test(new JItemStack(inv.getItem(i))))
                inv.setItem(i, null);
        return this;
    }

    /**
     * Get all the items in the inventory by index.
     * @return It's items in a list!
     */
    public List<JItemStack> getItemsInList() {
        return Arrays.stream(inv.getContents()).map(JItemStack::new).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get all the items in the inventory by index.
     * @return it's items in an array!
     */
    public JItemStack[] getItemsInArray() {
        return Arrays.stream(inv.getContents()).map(JItemStack::new).toArray(JItemStack[]::new);
    }

    /**
     * Check if the inventory has a slot open for an item!
     * @return true if it has an open slot.
     */
    public boolean isThereInventorySpace() {
        if (inv.firstEmpty() == -1)
            return false;
        else
            return true;
    }

    /**
     * Build inventory
     * @return Inventory object.
     * @deprecated use {@link #getInventory()} instead.
     */
    public Inventory buildInventory() {
        return inv;
    }

    /**
     * Gets the {@link Inventory}.
     * @return Inventory object.
     */
    public Inventory getInventory() {
        return inv;
    }
}
