package dev.jadss.jadapi.bukkitImpl.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class JInventory extends AbstractInventory<JInventory, JItemStack> {

    private final String displayName;

    /**
     * Create an JInventory.
     *
     * @param rows        the many rows the inventory should have.
     * @param displayName it's display name
     */
    public JInventory(int rows, String displayName) {
        this(Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&', displayName)), displayName);
    }

    /**
     * Tired of chest inventory? k create an inventory like a hopper or furnace.
     *
     * @param type        the InventoryType.
     * @param displayName it's display name.
     */
    public JInventory(InventoryType type, String displayName) {
        this(Bukkit.createInventory(null, type, ChatColor.translateAlternateColorCodes('&', displayName)), displayName);
    }

    /**
     * Create a JInventory with an already existing Inventory.
     *
     * @param inventory the already Existing inventory.
     * @param displayName it's display name.
     */
    public JInventory(Inventory inventory, String displayName) {
        super(inventory);
        if (displayName == null)
            throw new IllegalArgumentException("The display name may not be null!");

        this.displayName = displayName;
    }

    private static final Material AIR_MATERIAL = JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR).getMaterial(JMaterial.Type.ITEM).getKey();

    @Override
    public JItemStack getItem(int index) {
        return this.inventory.getItem(index) != null && this.inventory.getItem(index).getType() != AIR_MATERIAL ? new JItemStack(this.inventory.getItem(index)) : null;
    }

    @Override
    public List<JItemStack> getInventoryContentsAsList() {
        return Arrays.stream(this.inventory.getContents())
                .map(JItemStack::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public JItemStack[] getInventoryContentsAsArray() {
        return this.getInventoryContentsAsList().toArray(new JItemStack[0]);
    }

    @Override
    public JInventory copy() {
        Inventory newInventory;
        if (this.getType() == InventoryType.CHEST)
            newInventory = Bukkit.createInventory(null, this.getSize(), this.displayName);
        else
            newInventory = Bukkit.createInventory(null, this.getType(), this.displayName);

        return new JInventory(newInventory, this.displayName);
    }
}
