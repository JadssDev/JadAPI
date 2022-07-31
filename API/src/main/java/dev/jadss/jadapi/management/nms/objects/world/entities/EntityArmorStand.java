package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.objects.world.WorldServer;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.EntityLiving;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

public final class EntityArmorStand extends EntityLiving {

    public static final Class<?> ARMOR_STAND = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.decoration" : "server." + NMS.getNMSVersion()) + ".EntityArmorStand");

    public EntityArmorStand(Object entity) {
        super(entity);
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throwExceptionUnsupported();
        if (!isArmorStand(entity))
            throw new NMSException("This is not an NMS Armor Stand Object!");
    }

    public EntityArmorStand(WorldServer world, BlockPosition position) {
        super(JConstructorReflector.executeConstructor(ARMOR_STAND, new Class[]{WorldServer.worldClass, double.class, double.class, double.class}, world.getHandle(), position.getX(), position.getY(), position.getZ()));
    }

    public static boolean isArmorStand(Object entity) {
        return ARMOR_STAND.isAssignableFrom(entity.getClass());
    }

    @Override
    public void setInvisible(boolean invisible) {
        JFieldReflector.setObjectToUnspecificField(ARMOR_STAND, boolean.class, (i) -> i, this.entity, invisible);
        super.setInvisible(invisible);
    }

    @Override
    public void setNoGravity(boolean noGravity) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_9)) {
            JMethodReflector.executeMethod(ARMOR_STAND, "setGravity", new Class[]{boolean.class}, this.entity, new Object[] {!noGravity});
        } else {
            super.setNoGravity(noGravity);
        }
    }
}
