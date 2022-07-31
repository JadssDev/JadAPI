package dev.jadss.jadapi.bukkitImpl.item;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Represents the NBT data of an item.
 * <p>Uses NBTAPI to edit the NBT data.</p>
 * @param <T> the type of instance of item.
 */
public class ItemNBT<T extends AbstractItemStack<T>> {

    private final T item;

    private final NBTItem nbtAccessor;

    public ItemNBT(T item) {
        this.item = item;
        this.nbtAccessor = new NBTItem(item.getBukkitItem(), true);
    }


    /**
     * Set a string in the nbt.
     * @param key the key for the nbt.
     * @param value the value of this nbt.
     * @return itself.
     */
    public ItemNBT<T> setString(String key, String value) {
        if (key == null)
            throw new NullPointerException("Key may not be null.");

        nbtAccessor.setString(key, value);

        return this;
    }

    /**
     * Get a string in the nbt.
     * @param key the key for the nbt.
     * @return the value of this key.
     */
    public String getString(String key) {
        if (key == null)
            throw new NullPointerException("Key may not be null.");

        return nbtAccessor.getString(key);
    }




    /**
     * Set a boolean in the nbt.
     * @param key the key for the nbt.
     * @param value the value of this nbt.
     * @return itself.
     */
    public ItemNBT<T> setBoolean(String key, boolean value) {
        if (key == null)
            throw new NullPointerException("Key may not be null.");

        nbtAccessor.setBoolean(key, value);

        return this;
    }

    /**
     * Get a boolean in the nbt.
     * @param key the key for the nbt.
     * @return the value of this key.
     */
    public boolean getBoolean(String key) {
        if (key == null)
            throw new NullPointerException("Key may not be null.");

        return nbtAccessor.getBoolean(key);
    }




    /**
     * Set an int in the nbt.
     * @param key the key for the nbt.
     * @param value the value of this nbt.
     * @return itself.
     */
    public ItemNBT<T> setInteger(String key, int value) {
        if (key == null)
            throw new NullPointerException("Key may not be null.");

        nbtAccessor.setInteger(key, value);

        return this;
    }

    /**
     * Get an int in the nbt.
     * @param key the key for the nbt.
     * @return the value of this key.
     */
    public int getInteger(String key) {
        if (key == null)
            throw new NullPointerException("Key may not be null.");

        return nbtAccessor.getInteger(key);
    }




    /**
     * Set a double in the nbt.
     * @param key the key for the nbt.
     * @param value the value of this nbt.
     * @return itself.
     */
    public ItemNBT<T> setDouble(String key, double value) {
        if (key == null)
            throw new NullPointerException("Key may not be null.");

        nbtAccessor.setDouble(key, value);

        return this;
    }

    /**
     * Get a double in the nbt.
     * @param key the key for the nbt.
     * @return the value of this key.
     */
    public double getDouble(String key) {
        if (key == null)
            throw new NullPointerException("Key may not be null.");

        return nbtAccessor.getDouble(key);
    }





    public T getItem() {
        return this.item;
    }
}
