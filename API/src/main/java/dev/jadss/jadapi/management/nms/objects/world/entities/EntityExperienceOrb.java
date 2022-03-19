package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.JReflection;

public final class EntityExperienceOrb extends Entity {

    public static final Class<?> experienceOrbClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity" : "server." + JReflection.getNMSVersion()) + ".EntityExperienceOrb");

    public EntityExperienceOrb(Object nmsItem) {
        super(nmsItem);
        if(isExperienceOrb(nmsItem)) throwExceptionNotClass("Experience Orb");
    }

    //Todo:
    // Implement a way that you can get the amount of experience the orb has
    // Implement a way that you can set the amount of experience the orb has

    public boolean isExperienceOrb(Object entity) { return experienceOrbClass.isAssignableFrom(entity.getClass()); }
}
