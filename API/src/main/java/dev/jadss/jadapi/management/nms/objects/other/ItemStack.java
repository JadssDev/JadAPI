package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.management.nms.CraftUtils;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.JReflection;

public class ItemStack implements NMSObject, NMSManipulable, NMSCopyable {

    private Object itemStack;

    public static final Class<?> itemStackClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.item" : "server." + JReflection.getNMSVersion()) + ".ItemStack");

    public ItemStack(Object itemStack) {
        if(itemStack.getClass().equals(itemStackClass))
            throw new NMSException("The given object is not an ItemStack!");

        this.itemStack = itemStack;
    }

    @Override
    public NMSObject copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getHandle() { return itemStack; }

    public JItemStack getItem() {
        return new JItemStack(CraftUtils.asItem(itemStack));
    }
}
