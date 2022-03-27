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
            int x = JReflection.getFieldObject(blockChangePacketClass, int.class, packet, (i) -> 0);
            int y = JReflection.getFieldObject(blockChangePacketClass, int.class, packet, (i) -> 1);
            int z = JReflection.getFieldObject(blockChangePacketClass, int.class, packet, (i) -> 2);

            this.blockPosition = new BlockPosition(x, y, z);
        } else {
            this.blockPosition = new BlockPosition();
            this.blockPosition.parse(JReflection.getFieldObject(blockChangePacketClass, BlockPosition.blockPositionClass, packet));
        }

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            Block block = new Block(JReflection.getFieldObject(blockChangePacketClass, Block.blockClass, packet), JReflection.getFieldObject(blockChangePacketClass, int.class, packet, (i) -> i).byteValue());
            this.blockData = new IBlockData(block);
        } else {
            this.blockData = new IBlockData();
            this.blockData.parse(JReflection.getFieldObject(blockChangePacketClass, IBlockData.blockDataClass, packet));
        }
    }

    public Object build() {
        Object packet;
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            packet = JReflection.executeConstructor(getParsingClass(), new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass}, blockPosition.build(), blockData.build());
        } else {
            packet = JReflection.executeConstructor(getParsingClass(), new Class[]{});

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                JReflection.setFieldObject(blockChangePacketClass, int.class, packet, blockPosition.getX(), (i) -> 0);
                JReflection.setFieldObject(blockChangePacketClass, int.class, packet, blockPosition.getY(), (i) -> 1);
                JReflection.setFieldObject(blockChangePacketClass, int.class, packet, blockPosition.getZ(), (i) -> 2);

                if (blockData.getBlock().isInvalid())
                    throw new IllegalStateException("The block specified is invalid! (byte == -1)");

                JReflection.setFieldObject(blockChangePacketClass, Block.blockClass, packet, blockData.getBlock());
                JReflection.setFieldObject(blockChangePacketClass, int.class, packet, blockData.getBlock().getBlockData(), (i) -> i);
            } else {
                JReflection.setFieldObject(blockChangePacketClass, BlockPosition.blockPositionClass, packet, blockPosition.build());
                JReflection.setFieldObject(blockChangePacketClass, IBlockData.iBlockDataClass, packet, blockData.build());
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
