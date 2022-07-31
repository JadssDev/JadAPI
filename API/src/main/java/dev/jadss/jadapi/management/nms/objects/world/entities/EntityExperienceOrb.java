package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public final class EntityExperienceOrb extends Entity {

    public static final Class<?> EXPERIENCE_ORB = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + NMS.getNMSVersion()) + ".EntityExperienceOrb");

    public EntityExperienceOrb(Object entity) {
        super(entity);
        if (isExperienceOrb(entity))
            throwExceptionNotClass("Experience Orb");
    }

    //(Scrapped ideas, just use bukkit.)
    // Implement a way that you can get the amount of experience the orb has
    // Implement a way that you can set the amount of experience the orb has

    public static boolean isExperienceOrb(Object entity) {
        return EXPERIENCE_ORB.isAssignableFrom(entity.getClass());
    }
}
