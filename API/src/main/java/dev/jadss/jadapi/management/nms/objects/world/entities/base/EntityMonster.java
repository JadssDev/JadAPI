package dev.jadss.jadapi.management.nms.objects.world.entities.base;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public abstract class EntityMonster extends EntityCreature {

    public static final Class<?> ENTITY_MONSTER = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.monster" : "server." + NMS.getNMSVersion()) + ".EntityMonster");

    public EntityMonster(Object entity) {
        super(entity);

        if (!isEntityMonster(entity))
            throwExceptionNotClass("Entity Monster");
    }

    public static boolean isEntityMonster(Object entity) {
        return ENTITY_MONSTER.isAssignableFrom(entity.getClass());
    }
}
