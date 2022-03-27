package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.annotations.Beta;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.JReflection;

@Beta(description = "The datawatcher is not parsed by this packet.") //todo
public class OutEntityMetadata extends DefinedPacket {

    private int entityID;
    private ObjectPackage dataWatcherPackage;

    public static final Class<?> entityMetadataPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutEntityMetadata");

    public OutEntityMetadata() {
    }

    public OutEntityMetadata(Entity entity) {
        this.entityID = entity.getId();
        this.dataWatcherPackage = new ObjectPackage(entity.getDataWatcher());
    }

    public OutEntityMetadata(int entityID, ObjectPackage dataWatcher) {
        this.entityID = entityID;
        this.dataWatcherPackage = dataWatcher;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public ObjectPackage getDataWatcherPackage() {
        return dataWatcherPackage;
    }

    public void setDataWatcherPackage(ObjectPackage dataWatcherPackage) {
        this.dataWatcherPackage = dataWatcherPackage;
    }

    @Override
    public Object build() {
        return JReflection.executeConstructor(entityMetadataPacketClass, new Class[]{int.class, Entity.dataWatcherClass, boolean.class}, entityID, dataWatcherPackage.getObject(), true);
    }

    /**
     * Note parsing this packet will not parse the data watcher.
     *
     * @param packet the packet to parse into this object.
     */
    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        this.entityID = JReflection.getFieldObject(entityMetadataPacketClass, int.class, packet);
    }

    @Override
    public Class<?> getParsingClass() {
        return entityMetadataPacketClass;
    }

    @Override
    public boolean canParse(Object packet) {
        return entityMetadataPacketClass.equals(packet.getClass());
    }

    @Override
    public DefinedPacket copy() {
        return new OutEntityMetadata(this.entityID, this.dataWatcherPackage);
    }
}
