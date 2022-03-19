package dev.jadss.jadapi.management.nms.objects.world.entities;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.objects.world.WorldServer;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.EntityLiving;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.JReflection;

public final class EntityArmorStand extends EntityLiving {

    public static final Class<?> armorStandClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.decoration" : "server." + JReflection.getNMSVersion()) + ".EntityArmorStand");

    public EntityArmorStand(Object armorStandObject) {
        super(armorStandObject);
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) throwExceptionUnsupported();
        if(!isArmorStandObject(armorStandObject)) throw new NMSException("This is not an NMS Armor Stand Object!");
    }

    public EntityArmorStand(WorldServer world, BlockPosition position) {
        super(JReflection.executeConstructor(armorStandClass, new Class[] { WorldServer.worldClass, double.class, double.class, double.class }, world.getHandle(), position.getX(), position.getY(), position.getZ()));
    }

    public boolean isArmorStandObject(Object object) { return armorStandClass.isAssignableFrom(object.getClass()); }

    @Override
    public void setInvisible(boolean invisible) {
        JReflection.setUnspecificField(armorStandClass, boolean.class, (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? Integer.MAX_VALUE : 0), this.entity, invisible);
        super.setInvisible(invisible);
    }

    @Override
    public void setNoGravity(boolean gravity) {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_9)) {
            JReflection.executeMethod(armorStandClass, "setGravity", this.entity, new Class[] { boolean.class }, !gravity);
        } else {
            super.setNoGravity(gravity);
        }
    }
}
