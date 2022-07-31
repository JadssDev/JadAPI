package dev.jadss.jadapi.bukkitImpl.entities.subEntities;

import dev.jadss.jadapi.bukkitImpl.entities.JEntity;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import org.bukkit.entity.Item;

/**
 * Represents an Item on the ground!
 */
public class JItem extends JEntity {

    /**
     * Create a JItem from an existing Item.
     *
     * @param item The Item
     */
    public JItem(Item item) {
        super(item);
    }

    /**
     * Get the item stack that item represents while dropped.
     *
     * @return the JItemStack of it.
     */
    public AbstractItemStack<?> getItemStack() {
        return new JItemStack(((Item) entity).getItemStack());
    }

    /**
     * Set the ItemStack on the floor.
     *
     * @param item the JItemStack to set the Item's ItemStack.
     * @return itself.
     */
    public JItem setItemStack(AbstractItemStack<?> item) {
        ((Item) entity).setItemStack(item.getBukkitItem());

        return this;
    }

    /**
     * Set a delay on how many seconds the player has to wait to be able to pick the item up.
     *
     * @param delay the delay in ticks.
     * @return itself.
     */
    public JItem setPickupDelay(int delay) {
        ((Item) entity).setPickupDelay(delay);
        return this;
    }
}
