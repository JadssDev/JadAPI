package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.EntityLiving;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public final class EntityPlayer extends EntityLiving {

    public static final Class<?> ENTITY_PLAYER = JClassReflector.getClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "level" : NMS.getNMSVersion()) + ".EntityPlayer");
    public static final Class<?> ENTITY_HUMAN = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.player" : "server." + NMS.getNMSVersion()) + ".EntityHuman");

    public EntityPlayer(Object entity) {
        super(entity);
        if (!isEntityPlayer(entity))
            throwExceptionNotClass("Entity Player");
    }

    public static boolean isEntityPlayer(Object entity) {
        return ENTITY_PLAYER.isAssignableFrom(entity.getClass());
    }
}
