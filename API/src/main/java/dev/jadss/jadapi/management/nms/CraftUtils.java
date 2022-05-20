package dev.jadss.jadapi.management.nms;

import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.objects.other.ItemStack;
import dev.jadss.jadapi.management.nms.objects.world.block.Block;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Material;

/**
 * Represents utils for the "CraftMagicNumbers" class.
 */
public class CraftUtils {

    public static final Class<?> craftMagicNumbers = JReflection.getReflectionClass("org.bukkit.craftbukkit." + JReflection.getNMSVersion() + ".util.CraftMagicNumbers");
    public static final Class<?> craftItemStack = JReflection.getReflectionClass("org.bukkit.craftbukkit." + JReflection.getNMSVersion() + ".inventory.CraftItemStack");

    public static Object getBlock(JMaterial material) {
        return JReflection.executeMethod(craftMagicNumbers, "getBlock", null, new Class[] { Material.class }, material.getMaterial(JMaterial.Type.BLOCK).getKey());
    }

    public static Object getItem(JMaterial material) {
        return JReflection.executeMethod(craftMagicNumbers, "getItem", null, new Class[] { Material.class }, material.getMaterial(JMaterial.Type.BLOCK).getKey());
    }

    /**
     * Convert an ItemStack from Bukkit to a NMS one.
     * @param item the item in the bukkit api to transfer to NMS.
     * @return the NMS ItemStack
     */
    public static Object asItem(org.bukkit.inventory.ItemStack item) {
        return JReflection.executeMethod(craftItemStack, "asNMSCopy", null, new Class[] { org.bukkit.inventory.ItemStack.class }, item);
    }

    /**
     * Convert an itemstack from NMS to a Bukkit one.
     * @param nmsItemStack teh NMS ItemStack
     * @return teh Bukkit ItemStack
     */
    public static org.bukkit.inventory.ItemStack asItem(Object nmsItemStack) {
        return (org.bukkit.inventory.ItemStack) JReflection.executeMethod(craftItemStack, "asBukkitCopy", null, new Class[] { ItemStack.itemStackClass }, nmsItemStack);
    }

    public static Material getMaterial(Object block) {
        return (Material) JReflection.executeMethod(craftMagicNumbers, "getMaterial", null, new Class[] { Block.blockClass }, block);
    }
}
