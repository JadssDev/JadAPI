package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.EntityLiving;
import dev.jadss.jadapi.utils.JReflection;

public final class EntityPlayer extends EntityLiving {

    public static final Class<?> entityPlayerClass = JReflection.getReflectionClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "level" : JReflection.getNMSVersion()) + ".EntityPlayer");
    public static final Class<?> entityHumanClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.player" : "server." + JReflection.getNMSVersion()) + ".EntityHuman");

    public EntityPlayer(Object nsmEntityPlayer) {
        super(nsmEntityPlayer);
        if(!isEntityPlayerObject(nsmEntityPlayer)) throwExceptionNotClass("Entity Player");
    }

    public boolean isEntityPlayerObject(Object object) { return entityPlayerClass.isAssignableFrom(object.getClass()); }
}
