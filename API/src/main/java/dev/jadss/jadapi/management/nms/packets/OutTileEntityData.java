package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntity;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntitySign;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.JReflection;

public class OutTileEntityData extends DefinedPacket {

    public static final Class<?> tileEntityDataPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutTileEntityData");

    private BlockPosition blockPosition;
    private int id;
    private ObjectPackage nbt;

    public OutTileEntityData(TileEntity tileEntity, BlockPosition position) {
        this.blockPosition = position;
        if (tileEntity instanceof TileEntitySign) {
            this.id = TileEntity.SIGN_TYPE;
            this.nbt = NMS.getNBTFromClass(TileEntitySign.tileEntitySignClass, tileEntity.getHandle());
        } else {
            throw new NMSException("Cannot find the ID of the this TileEntity!");
        }
    }

    public OutTileEntityData(ObjectPackage nbtPackage, BlockPosition position, int typeId) {
        this.nbt = nbtPackage;
        this.blockPosition = position;
        this.id = typeId;
    }

    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(BlockPosition blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ObjectPackage getNBT() {
        return nbt;
    }

    public void setNBT(ObjectPackage nbt) {
        this.nbt = nbt;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            this.blockPosition = new BlockPosition();
            this.blockPosition.parse(JReflection.getUnspecificFieldObject(tileEntityDataPacketClass, BlockPosition.blockPositionClass, packet));

            this.id = JReflection.getUnspecificFieldObject(tileEntityDataPacketClass, int.class, packet);

            this.nbt = new ObjectPackage(JReflection.getUnspecificFieldObject(tileEntityDataPacketClass, NMS.nbtTagCompoundClass, packet));
        } else {
            int x = JReflection.getUnspecificFieldObject(tileEntityDataPacketClass, int.class, 0, packet);
            int y = JReflection.getUnspecificFieldObject(tileEntityDataPacketClass, int.class, 1, packet);
            int z = JReflection.getUnspecificFieldObject(tileEntityDataPacketClass, int.class, 2, packet);
            this.blockPosition = new BlockPosition(x, y, z);

            this.id = JReflection.getUnspecificFieldObject(tileEntityDataPacketClass, int.class, 3, packet);

            this.nbt = new ObjectPackage(JReflection.getUnspecificFieldObject(tileEntityDataPacketClass, NMS.nbtTagCompoundClass, packet));
        }
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            return JReflection.executeConstructor(tileEntityDataPacketClass, new Class[]{BlockPosition.blockPositionClass, int.class, NMS.nbtTagCompoundClass}, blockPosition.build(), id, nbt.getObject());
        } else {
            return JReflection.executeConstructor(tileEntityDataPacketClass, new Class[]{int.class, int.class, int.class, int.class, NMS.nbtTagCompoundClass}, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), id, nbt.getObject());
        }
    }

    @Override
    public boolean canParse(Object packet) {
        return packet.getClass().equals(tileEntityDataPacketClass);
    }

    @Override
    public Class<?> getParsingClass() {
        return tileEntityDataPacketClass;
    }

    @Override
    public DefinedPacket copy() {
        return new OutTileEntityData(this.nbt, (BlockPosition) this.blockPosition.copy(), this.id);
    }
}
