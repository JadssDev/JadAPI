package dev.jadss.jadapi.enchmodules;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enchantments.EnchantmentInstance;
import dev.jadss.jadapi.bukkitImpl.enchantments.JEnchantmentInfo;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class RevisionOneEnchantment extends Enchantment implements EnchantmentInstance {

    private final JEnchantmentInfo info;
    private boolean registered = true;

    public RevisionOneEnchantment(JEnchantmentInfo info) {
        super(info.getId());
        this.info = info;
    }

    @Override
    public JEnchantmentInfo getEnchantmentInformation() {
        return info;
    }

    @Override
    public Enchantment asEnchantment() {
        return this;
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    @Override
    public void unregister() {
        registered = false;
        JadAPI.getInstance().getRegisterer().unregisterEnchantment(this);
    }

    @Override
    public String getName() {
        return info.getName();
    }

    @Override
    public int getMaxLevel() {
        return info.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return info.getStartLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return info.getTarget();
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return info.conflictsWith(other);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return info.canEnchant(item);
    }
}
