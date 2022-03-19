package dev.jadss.jadapi.interfaces.inventory;

import dev.jadss.jadapi.bukkitImpl.item.JInventory;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.event.inventory.InventoryType;

/**
 * Represents an Inventory in <b>abstract</b> ways, can be created later on as needed..
 * @see JInventory
 */
public abstract class JInventoryAbstract {

    /**
     * The Name/Title of the inventory!
     * @return the Title.
     */
    public abstract String getInventoryTitle();

    /**
     * How many rows does the inventory have.
     * @return the amount of rows.
     */
    public abstract int getInventoryRows();

    /**
     * If this is a custom inventory type,<p>
     * if this is not null rows will be used instead of the inventory type.
     * @return The inventory type.
     */
    public abstract InventoryType getInventoryType();

    /**
     * Fill the inventory with a specific item!
     * @return the stack to fill the back.
     */
    public abstract JItemStack getBackgroundItem();


    /**
     * Set up the inventory items.
     * <p>Note: <b>if the inventory is not the same</b> = exception</p>
     * @param inventory the inventory to set the items in.
     * @return the Inventory after modifications.
     * @throws JException if the Inventory is not the same.
     */
    public abstract JInventory setupInventory(JInventory inventory);

}
