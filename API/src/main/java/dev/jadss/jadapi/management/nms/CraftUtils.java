package dev.jadss.jadapi.management.nms;

import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.objects.other.ItemStack;
import dev.jadss.jadapi.management.nms.objects.world.block.Block;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;
import org.bukkit.Material;

/**
 * Represents utils for the "CraftMagicNumbers" class.
 */
public class CraftUtils {

    public static final Class<?> craftMagicNumbers = JClassReflector.getClass("org.bukkit.craftbukkit." + NMS.getNMSVersion() + ".util.CraftMagicNumbers");
    public static final Class<?> craftItemStack = JClassReflector.getClass("org.bukkit.craftbukkit." + NMS.getNMSVersion() + ".inventory.CraftItemStack");

    public static Object getBlock(JMaterial material) {
        return JMethodReflector.executeMethod(craftMagicNumbers, "getBlock", new Class[]{Material.class}, null, new Object[]{material.getMaterial(JMaterial.Type.BLOCK).getKey()});
    }

    public static Object getItem(JMaterial material) {
        return JMethodReflector.executeMethod(craftMagicNumbers, "getItem", new Class[]{Material.class}, null, new Object[]{material.getMaterial(JMaterial.Type.BLOCK).getKey()});
    }

    /**
     * Convert an ItemStack from Bukkit to a NMS one.
     *
     * @param item the item in the bukkit api to transfer to NMS.
     * @return the NMS ItemStack
     */
    public static Object asItem(org.bukkit.inventory.ItemStack item) {
        return JMethodReflector.executeMethod(craftItemStack, "asNMSCopy", new Class[]{org.bukkit.inventory.ItemStack.class}, null, new Object[]{item});
    }

    /**
     * Convert an itemstack from NMS to a Bukkit one.
     *
     * @param nmsItemStack teh NMS ItemStack
     * @return teh Bukkit ItemStack
     */
    public static org.bukkit.inventory.ItemStack asItem(Object nmsItemStack) {
        return (org.bukkit.inventory.ItemStack) JMethodReflector.executeMethod(craftItemStack, "asBukkitCopy", new Class[]{ItemStack.itemStackClass}, null, new Object[]{nmsItemStack});
    }

    public static Material getMaterial(Object block) {
        return (Material) JMethodReflector.executeMethod(craftMagicNumbers, "getMaterial", new Class[]{Block.blockClass}, null, new Object[]{block});
    }
}
