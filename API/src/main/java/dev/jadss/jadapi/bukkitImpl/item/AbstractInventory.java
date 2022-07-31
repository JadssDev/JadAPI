package dev.jadss.jadapi.bukkitImpl.item;

import dev.jadss.jadapi.interfaces.Copyable;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractInventory<T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> implements Copyable<T> {

    protected final Inventory inventory;

    public AbstractInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Get the type of this inventory.
     * @return the type.
     */
    public InventoryType getType() {
        return inventory.getType();
    }

    /**
     * Get the size of this Inventory.
     * @return the size.
     */
    public int getSize() {
        return this.inventory.getSize();
    }

    /**
     * Get the item in an index position.
     * @param index the index position.
     * @return the item, or null.
     */
    public abstract K getItem(int index);

    /**
     * Set an item to the index position of this inventory.
     * @param index the index position.
     * @param item the item to set.
     * @return itself.
     */
    public T setItem(int index, K item) {
        this.inventory.setItem(index, item != null ? item.getBukkitItem() : null);

        return (T) this;
    }

    /**
     * Fill the inventory with a specific item.
     * @param item the item to fill the inventory with.
     * @return itself.
     * @see AbstractInventory#fill(AbstractItemStack, boolean)
     */
    public T fill(K item) {
        return this.fill(item, true);
    }

    /**
     * Fill the inventory with a specific item.
     * @param item the item to fill the inventory with.
     * @param overrideExisting should we override existing items?
     * @return itself.
     */
    public T fill(K item, boolean overrideExisting) {
        if (overrideExisting) {
            for (int i = 0; i < this.inventory.getSize(); i++)
                if (true) //I'm going to be severely honest and say this true is here because below I check for null, and here I don't have to check anything so it's just true.
                    this.setItem(i, item);
        } else {
            for (int i = 0; i < this.inventory.getSize(); i++)
                if (this.getItem(i) != null && this.getItem(i).isValidNBTItem()) //checks if it's different than null && AUR
                    this.setItem(i, item);
        }

        return (T) this;
    }

    /**
     * Get the list of items in this inventory.
     * @return the list of items.
     */
    public abstract List<K> getInventoryContentsAsList();

    /**
     * Get the array of items in this inventory.
     * @return the array of items.
     */
    public abstract K[] getInventoryContentsAsArray();

    /**
     * Set an Array as the inventory items.
     * @param contents the contents.
     * @return itself.
     */
    public T setContents(K[] contents) {
        return this.setContents(Arrays.asList(contents));
    }

    /**
     * Set a List as the inventory items.
     * @param contents the contents.
     * @return itself.
     */
    public T setContents(List<K> contents) {
        for(int i = 0; i < this.getSize(); ++i) {
            if (i >= contents.size()) {
                this.setItem(i, null);
            } else {
                this.setItem(i, contents.get(i));
            }
        }

        return (T) this;
    }

    /**
     * Checks if the inventory has a free slot.
     * @return if the inventory has a free slot.
     */
    public boolean hasFreeSlot() {
        return this.inventory.firstEmpty() != -1;
    }

    /**
     * Get the index of the slot free in the inventory.
     * @return the index of the free slot.
     */
    public int getFreeSlot() {
        return this.inventory.firstEmpty();
    }

    /**
     * Clear this inventory.
     * @return itself.
     */
    public T clear() {
        this.inventory.clear();

        return (T) this;
    }

    /**
     * <p>Clear this inventory using the predicate specified.</p>
     * <p>If the predicate returns true, the item is preserved</p>
     * <p>If the predicate returns false, the item is cleared</p>
     * @param predicate the predicate used to remove items to clear the inventory.
     * @return itself.
     */
    public T clear(Predicate<K> predicate) {
        for (int i = 0; i < this.inventory.getSize(); i++)
            if (!predicate.test(this.getItem(i)))
                this.setItem(i, null);

        return (T) this;
    }

     /**
     * Get the bukkit inventory of this instance.
     * @return the bukkit inventory.
     */
    public Inventory getBukkitInventory() {
        return this.inventory;
    }
}
