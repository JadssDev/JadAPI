package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public final class EntityItem extends Entity {

    public static final Class<?> ITEM = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.item" : "server." + NMS.getNMSVersion()) + ".EntityItem");

    public EntityItem(Object entity) {
        super(entity);
        if (isEntityItem(entity))
            throwExceptionNotClass("Item");
    }

    public static boolean isEntityItem(Object entity) {
        return ITEM.isAssignableFrom(entity.getClass());
    }
}
