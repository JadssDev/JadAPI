package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.JReflection;

public final class EntityAreaEffectCloud extends Entity {

    public static final Class<?> areaEffectCloud = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".EntityAreaEffectCloud");

    public EntityAreaEffectCloud(Object nmsItem) {
        super(nmsItem);
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) throwExceptionUnsupported();
        if(isAreaEffectCloud(nmsItem)) throwExceptionNotClass("Area Effect Cloud");
    }

    public boolean isAreaEffectCloud(Object entity) { return areaEffectCloud.isAssignableFrom(entity.getClass()); }
}
