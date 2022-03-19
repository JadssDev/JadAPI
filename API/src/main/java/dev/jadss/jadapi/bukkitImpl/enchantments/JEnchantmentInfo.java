package dev.jadss.jadapi.bukkitImpl.enchantments;

import dev.jadss.jadapi.JadAPIPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

/**
 * Information of an Enchantment JadAPI can process into a real enchantment!
 */
public interface JEnchantmentInfo {

    /**
     * Is this EnchantmentInfo Registered already with an EnchantmentInstance?
     * @return if it was already registered.
     * @see JEnchantmentInfo#getRegistered()
     */
    boolean isRegistered();

    /**
     * Get the instance that has been registered.
     * @return The registered instance.
     */
    EnchantmentInstance getRegistered();

    /**
     * Set this Enchantment has already been registered.
     * <h1>Note: </h1> <b>- Called with null to unregister</b>
     * @param instance the Instance to register with.
     */
    void setRegistered(EnchantmentInstance instance);



    /**
     * The internal name of the enchantment, <b>shouldn't contain spaces</b>.
     * @return The internal name the Enchantment should have!
     */
    @BukkitAPI
    String getName();

    /**
     * What is the Display name of this enchantment!
     * @return the DisplayName.
     */
    String getDisplayName();

    /**
     * The id of the enchantment, this should be unique, and it's used on older versions.
     * @return the ID.
     */
    @BukkitAPI
    int getId();

    /**
     * Owner of the enchantment here.
     * @return the Owner's JadAPI.
     */
    JadAPIPlugin getEnchantmentOwner();

    /**
     * What is the start level of this Enchantment?
     * @return the Enchantment start level.
     */
    @BukkitAPI
    int getStartLevel();

    /**
     * The maximum level this Enchantmnet can reach.
     * @return the Max level.
     */
    @BukkitAPI
    int getMaxLevel();

    /**
     * Build the text to display after the enchantment displayname of the lore.
     * @param level The level to build the text for.
     * @return the Level display.
     */
    String buildLevelText(int level);

    /**
     * <p>What is this enchantment used on?</p>
     * <h1>Note: </h1> <b>- This is given to the Bukkit API, but it's completely worthless.</b>
     * @return what it is used on.
     */
    @BukkitAPI
    EnchantmentTarget getTarget();

    /**
     * Is this enchantment a Treasure.
     * @return if it is.
     */
    @BukkitAPI
    boolean isTreasure();

    /**
     * Is this enchantment a Curse?
     * @return if it is.
     */
    @BukkitAPI
    boolean isCurse();

    /**
     * Does this enchantment conflict with X enchantment?
     * @param enchantment the enchantment in specific.
     * @return if it does conflict.
     */
    @BukkitAPI
    boolean conflictsWith(Enchantment enchantment);

    /**
     * Can this enchantment be added to X item?
     * @param item the Item in question.
     * @return if it can.
     */
    @BukkitAPI
    boolean canEnchant(ItemStack item);

    /**
     * This method is instantly called <b>on demand</b> by the bukkit api as soon as it calls the specified method.
     */
    @interface BukkitAPI {}
}
