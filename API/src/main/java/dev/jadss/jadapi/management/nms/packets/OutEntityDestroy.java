package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.util.Arrays;

public class OutEntityDestroy extends DefinedPacket {

    private int[] entityIds;

    public static final Class<?> entityDestroyPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutEntityDestroy");

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
                Object obj = JFieldReflector.getObjectsFromFields(entityDestroyPacketClass, packet, false).get(0);
                this.entityIds = JFieldReflector.getObjectFromUnspecificField(obj.getClass(), int[].class, obj);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.entityIds = JFieldReflector.getObjectFromUnspecificField(entityDestroyPacketClass, int[].class, packet);
        }
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            return JConstructorReflector.executeConstructor(entityDestroyPacketClass, new Class[]{int[].class}, entityIds);
        } else {
            Object packet = JConstructorReflector.executeConstructor(entityDestroyPacketClass, new Class[]{});
            JFieldReflector.setObjectToUnspecificField(entityDestroyPacketClass, int[].class, packet, entityIds);
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

    @Override
    public String toString() {
        return "OutEntityDestroy{" +
                "entityIds=" + Arrays.toString(entityIds) +
                '}';
    }
}
