package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public final class EntityAreaEffectCloud extends Entity {

    public static final Class<?> AREA_EFFECT_CLOUD = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + NMS.getNMSVersion()) + ".EntityAreaEffectCloud");

    public EntityAreaEffectCloud(Object entity) {
        super(entity);
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8))
            throwExceptionUnsupported();
        if (isAreaEffectCloud(entity))
            throwExceptionNotClass("Area Effect Cloud");
    }

    public static boolean isAreaEffectCloud(Object entity) {
        return AREA_EFFECT_CLOUD.isAssignableFrom(entity.getClass());
    }
}
