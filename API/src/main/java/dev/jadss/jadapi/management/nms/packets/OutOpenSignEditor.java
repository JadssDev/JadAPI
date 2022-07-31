package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class OutOpenSignEditor extends DefinedPacket {

    public static final Class<?> openSignEditorPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutOpenSignEditor");

    private BlockPosition position;

    public OutOpenSignEditor() {

    }

    public OutOpenSignEditor(BlockPosition blockPosition) {
        this.position = blockPosition;
    }

    public BlockPosition getPosition() {
        return position;
    }

    public void setPosition(BlockPosition position) {
        this.position = position;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            int x = JFieldReflector.getObjectFromUnspecificField(openSignEditorPacketClass, int.class, (i) -> 0, packet);
            int y = JFieldReflector.getObjectFromUnspecificField(openSignEditorPacketClass, int.class, (i) -> 1, packet);
            int z = JFieldReflector.getObjectFromUnspecificField(openSignEditorPacketClass, int.class, (i) -> 2, packet);
            this.position = new BlockPosition(x, y, z);
        } else {
            this.position = new BlockPosition();
            this.position.parse(JFieldReflector.getObjectFromUnspecificField(openSignEditorPacketClass, BlockPosition.blockPositionClass, packet));
        }
    }

    @Override
    public Object build() {
        Object packet;

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            packet = JConstructorReflector.executeConstructor(openSignEditorPacketClass, new Class[]{int.class, int.class, int.class}, this.position.getX(), this.position.getY(), this.position.getZ());
        } else {
            packet = JConstructorReflector.executeConstructor(openSignEditorPacketClass, new Class[]{BlockPosition.blockPositionClass}, this.position.build());
        }

        return packet;
    }

    @Override
    public Class<?> getParsingClass() {
        return openSignEditorPacketClass;
    }

    @Override
    public boolean canParse(Object object) {
        return openSignEditorPacketClass.equals(object.getClass());
    }

    @Override
    public DefinedPacket copy() {
        return new OutOpenSignEditor((BlockPosition) this.position.copy());
    }

    @Override
    public String toString() {
        return "OutOpenSignEditor{" +
                "position=" + position +
                '}';
    }
}
