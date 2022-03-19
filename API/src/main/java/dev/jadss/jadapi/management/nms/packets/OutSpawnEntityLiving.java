package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.entities.JEntity;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.EntityLiving;
import dev.jadss.jadapi.utils.JReflection;

public class OutSpawnEntityLiving extends DefinedPacket {

    private EntityLiving entityLiving;

    public static final Class<?> spawnEntityLivingPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutSpawnEntityLiving");

    public OutSpawnEntityLiving() {
    }

    public OutSpawnEntityLiving(EntityLiving entityLiving) {
        this.entityLiving = entityLiving;
    }

    public EntityLiving getEntityLiving() {
        return entityLiving;
    }

    public void setEntityLiving(EntityLiving entityLiving) {
        this.entityLiving = entityLiving;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        int entityID = JReflection.getUnspecificFieldObject(spawnEntityLivingPacketClass, int.class, packet);
        this.entityLiving = (EntityLiving) NMS.getEntity(JEntity.getEntity(entityID).getEntity());
    }

    @Override
    public Object build() {
        return JReflection.executeConstructor(spawnEntityLivingPacketClass, new Class[]{EntityLiving.entityLivingClass}, this.entityLiving.getHandle());
    }

    @Override
    public Class<?> getParsingClass() {
        return spawnEntityLivingPacketClass;
    }

    @Override
    public boolean canParse(Object object) {
        return spawnEntityLivingPacketClass.equals(object.getClass());
    }

    @Override
    public DefinedPacket copy() {
        return new OutSpawnEntityLiving(this.entityLiving);
    }
}
