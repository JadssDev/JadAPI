package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.world.block.Block;
import dev.jadss.jadapi.management.nms.objects.world.block.IBlockData;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class OutBlockChangePacket extends DefinedPacket {

    private IBlockData blockData;
    private BlockPosition blockPosition;

    public static final Class<?> blockChangePacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutBlockChange");

    public OutBlockChangePacket() {
    }

    public OutBlockChangePacket(IBlockData blockData, BlockPosition blockPosition) {
        this.blockData = blockData;
        this.blockPosition = blockPosition;
    }

    public BlockPosition getLocation() {
        return this.blockPosition;
    }

    public IBlockData getBlockData() {
        return this.blockData;
    }

    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            int x = JFieldReflector.getObjectFromUnspecificField(blockChangePacketClass, int.class, (i) -> 0, packet);
            int y = JFieldReflector.getObjectFromUnspecificField(blockChangePacketClass, int.class, (i) -> 1, packet);
            int z = JFieldReflector.getObjectFromUnspecificField(blockChangePacketClass, int.class, (i) -> 2, packet);

            this.blockPosition = new BlockPosition(x, y, z);
        } else {
            this.blockPosition = new BlockPosition();
            this.blockPosition.parse(JFieldReflector.getObjectFromUnspecificField(blockChangePacketClass, BlockPosition.blockPositionClass, packet));
        }

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            Block block = new Block(JFieldReflector.getObjectFromUnspecificField(blockChangePacketClass, Block.blockClass, packet), JFieldReflector.getObjectFromUnspecificField(blockChangePacketClass, int.class, (i) -> i, packet).byteValue());
            this.blockData = new IBlockData(block);
        } else {
            this.blockData = new IBlockData();
            this.blockData.parse(JFieldReflector.getObjectFromUnspecificField(blockChangePacketClass, IBlockData.iBlockDataClass, packet));
        }
    }

    public Object build() {
        Object packet;
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            packet = JConstructorReflector.executeConstructor(getParsingClass(), new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass}, blockPosition.build(), blockData.build());
        } else {
            packet = JConstructorReflector.executeConstructor(getParsingClass(), new Class[]{});

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                JFieldReflector.setObjectToUnspecificField(blockChangePacketClass, int.class, (i) -> 0, packet, blockPosition.getX());
                JFieldReflector.setObjectToUnspecificField(blockChangePacketClass, int.class, (i) -> 1, packet, blockPosition.getY());
                JFieldReflector.setObjectToUnspecificField(blockChangePacketClass, int.class, (i) -> 2, packet, blockPosition.getZ());

                if (blockData.getBlock().isInvalid())
                    throw new IllegalStateException("The block specified is invalid! (byte == -1)");

                JFieldReflector.setObjectToUnspecificField(blockChangePacketClass, Block.blockClass, packet, blockData.getBlock());
                JFieldReflector.setObjectToUnspecificField(blockChangePacketClass, int.class, (i) -> i, packet, blockData.getBlock().getBlockData());
            } else {
                JFieldReflector.setObjectToUnspecificField(blockChangePacketClass, BlockPosition.blockPositionClass, packet, blockPosition.build());
                JFieldReflector.setObjectToUnspecificField(blockChangePacketClass, IBlockData.iBlockDataClass, packet, blockData.build());
            }
        }

        return packet;
    }

    public Class<?> getParsingClass() {
        return blockChangePacketClass;
    }

    public boolean canParse(Object packet) {
        return getParsingClass().equals(packet.getClass());
    }

    public DefinedPacket copy() {
        return new OutBlockChangePacket((IBlockData) blockData.copy(), (BlockPosition) blockPosition.copy());
    }

    @Override
    public String toString() {
        return "OutBlockChangePacket{" +
                "blockData=" + blockData +
                ", blockPosition=" + blockPosition +
                '}';
    }
}
