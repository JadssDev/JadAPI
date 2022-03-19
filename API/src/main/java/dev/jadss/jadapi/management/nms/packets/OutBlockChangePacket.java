package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.world.block.Block;
import dev.jadss.jadapi.management.nms.objects.world.block.IBlockData;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.JReflection;

public class OutBlockChangePacket extends DefinedPacket {

    private IBlockData blockData;
    private BlockPosition blockPosition;

    public static final Class<?> blockChangePacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutBlockChange");

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
            int x = JReflection.getUnspecificFieldObject(blockChangePacketClass, int.class, 0, packet);
            int y = JReflection.getUnspecificFieldObject(blockChangePacketClass, int.class, 1, packet);
            int z = JReflection.getUnspecificFieldObject(blockChangePacketClass, int.class, 2, packet);

            this.blockPosition = new BlockPosition(x, y, z);
        } else {
            this.blockPosition = new BlockPosition();
            this.blockPosition.parse(JReflection.getUnspecificFieldObject(blockChangePacketClass, BlockPosition.blockPositionClass, packet));
        }

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            Block block = new Block(JReflection.getUnspecificFieldObject(blockChangePacketClass, Block.blockClass, 0, packet), JReflection.getUnspecificFieldObject(blockChangePacketClass, int.class, Integer.MAX_VALUE, packet).byteValue());
            this.blockData = new IBlockData(block);
        } else {
            this.blockData = new IBlockData();
            this.blockData.parse(JReflection.getUnspecificFieldObject(blockChangePacketClass, IBlockData.blockDataClass, packet));
        }
    }

    public Object build() {
        Object packet;
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            packet = JReflection.executeConstructor(getParsingClass(), new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass}, blockPosition.build(), blockData.build());
        } else {
            packet = JReflection.executeConstructor(getParsingClass(), new Class[]{});

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                JReflection.setUnspecificField(blockChangePacketClass, int.class, 0, packet, blockPosition.getX());
                JReflection.setUnspecificField(blockChangePacketClass, int.class, 1, packet, blockPosition.getY());
                JReflection.setUnspecificField(blockChangePacketClass, int.class, 2, packet, blockPosition.getZ());

                if (blockData.getBlock().isInvalid())
                    throw new IllegalStateException("The block specified is invalid! (byte == -1)");

                JReflection.setUnspecificField(blockChangePacketClass, Block.blockClass, packet, blockData.getBlock());
                JReflection.setUnspecificField(blockChangePacketClass, int.class, Integer.MAX_VALUE, packet, blockData.getBlock().getBlockData());
            } else {
                JReflection.setUnspecificField(blockChangePacketClass, BlockPosition.blockPositionClass, packet, blockPosition.build());
                JReflection.setUnspecificField(blockChangePacketClass, IBlockData.iBlockDataClass, packet, blockData.build());
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
}
