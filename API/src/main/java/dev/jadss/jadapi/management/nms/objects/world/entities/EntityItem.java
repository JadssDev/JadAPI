package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.JReflection;

public final class EntityItem extends Entity {

    public static final Class<?> itemClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.item" : "server." + JReflection.getNMSVersion()) + ".EntityItem");

    public EntityItem(Object nmsItem) {
        super(nmsItem);
        if(isEntityItem(nmsItem)) throwExceptionNotClass("Item");
    }

    public boolean isEntityItem(Object entity) { return itemClass.isAssignableFrom(entity.getClass()); }
}
