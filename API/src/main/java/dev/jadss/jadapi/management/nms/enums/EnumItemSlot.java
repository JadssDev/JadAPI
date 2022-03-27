package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.JReflection;

/**
 * Represents a Slot in NMS.
 */
public enum EnumItemSlot implements NMSEnum {
    MAIN_HAND("mainhand", 0),
    OFF_HAND("offhand", 5),
    FEET("feet", 1),
    LEGS("legs", 2),
    CHEST("chest", 3),
    HEAD("head", 4);

    private String name;
    private int slot;

    EnumItemSlot(String name, int slot) {
        this.name = name;
        this.slot = slot;
    }

    public String getName() { return name; }
    public int getSlot() { return slot; }

    public static final Class<?> enumItemSlotClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".EnumItemSlot");

    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8))
            throw new NMSException("EnumItemSlot is not available in versions below 1.8");
        return JReflection.executeMethod(enumItemSlotClass, new Class[] { String.class }, null, enumItemSlotClass, (i) -> 0, name);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumItemSlotClass;
    }
}
