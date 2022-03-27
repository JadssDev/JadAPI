package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.utils.JReflection;

public class OutEntityDestroy extends DefinedPacket {

    private int[] entityIds;

    public static final Class<?> entityDestroyPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutEntityDestroy");

    public OutEntityDestroy() {
    }

    public OutEntityDestroy(int[] entityIds) {
        this.entityIds = entityIds;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            try {
                Object obj = entityDestroyPacketClass.getFields()[0].get(packet);
                this.entityIds = JReflection.getFieldObject(obj.getClass(), int[].class, obj);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.entityIds = JReflection.getFieldObject(entityDestroyPacketClass, int[].class, packet);
        }
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            return JReflection.executeConstructor(entityDestroyPacketClass, new Class[]{int[].class}, entityIds);
        } else {
            Object packet = JReflection.executeConstructor(entityDestroyPacketClass, new Class[]{});
            JReflection.setFieldObject(entityDestroyPacketClass, int[].class, packet, entityIds);
            return packet;
        }
    }

    @Override
    public Class<?> getParsingClass() {
        return entityDestroyPacketClass;
    }

    @Override
    public boolean canParse(Object object) {
        return entityDestroyPacketClass.equals(object.getClass());
    }

    @Override
    public DefinedPacket copy() {
        return null;
    }
}
