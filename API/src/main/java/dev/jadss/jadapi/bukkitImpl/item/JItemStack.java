package dev.jadss.jadapi.bukkitImpl.item;

import dev.jadss.jadapi.bukkitImpl.entities.subEntities.JItem;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;
import java.util.Map;

public final class JItemStack extends AbstractItemStack<JItemStack> {

    public JItemStack(JMaterial material) {
        this(material.getMaterial(JMaterial.Type.ITEM));
    }

    public JItemStack(Material material, byte data) {
        this(new AbstractMap.SimpleEntry<>(material, data));
    }

    private JItemStack(Map.Entry<Material, Byte> entry) {
        this(new ItemStack(entry.getKey(), 1, (short) 0, entry.getValue() == 0 ? null : entry.getValue()));
    }

    public JItemStack(ItemStack item) {
        super(item);
    }

    /**
     * Drop this JItemStack in a location.
     * @param location The location to spawn it in.
     * @return a JItem Object.
     */
    public JItem drop(Location location) {
        if(location == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        return new JItem(location.getWorld().dropItemNaturally(location, item));
    }

    @Override
    public JItemStack copy() {
        return new JItemStack(this.item.clone());
    }
}
