package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.annotations.Beta;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

@Beta(description = "The datawatcher is not parsed by this packet.") //todo
public class OutEntityMetadata extends DefinedPacket {

    private int entityID;
    private ObjectPackage dataWatcherPackage;

    public static final Class<?> entityMetadataPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutEntityMetadata");

    public OutEntityMetadata() {
    }

    public OutEntityMetadata(Entity entity) {
        this.entityID = entity.getEntityId();
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
        return JConstructorReflector.executeConstructor(entityMetadataPacketClass, new Class[]{int.class, Entity.DATA_WATCHER_CLASS, boolean.class}, entityID, (dataWatcherPackage != null ? dataWatcherPackage.getObject() : null), true);
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

        this.entityID = JFieldReflector.getObjectFromUnspecificField(entityMetadataPacketClass, int.class, packet);
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

    @Override
    public String toString() {
        return "OutEntityMetadata{" +
                "entityID=" + entityID +
                ", dataWatcherPackage=" + dataWatcherPackage +
                '}';
    }
}
