package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntity;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntitySign;
import dev.jadss.jadapi.management.nms.objects.world.entities.types.TileEntityTypesInstance;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.JReflection;

public class OutTileEntityData extends DefinedPacket {

    public static final Class<?> tileEntityDataPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutTileEntityData");

    private BlockPosition blockPosition;
    private ObjectPackage nbt;
    //Old
    private int id;
    //New
    private TileEntityTypesInstance type;

    public OutTileEntityData(TileEntity tileEntity, BlockPosition position) {
        this.blockPosition = position;

        if (tileEntity instanceof TileEntitySign) {
            this.nbt = NMS.getNBTFromClass(tileEntity.getHandle());
            if (this.isUpdateVersion()) {
                this.type = tileEntity.getTileEntityType();
            } else {
                this.id = TileEntity.SIGN_TYPE;
            }
        } else {
            throw new NMSException("Cannot find the ID of the this TileEntity!");
        }
    }

    public OutTileEntityData(ObjectPackage nbtPackage, BlockPosition position, TileEntityTypesInstance type) {
        if (this.isUpdateVersion())
            throw new NMSException("You may not use the type to create this packet on <1.17");

        this.nbt = nbtPackage;
        this.blockPosition = position;
        this.type = type;
    }

    public OutTileEntityData(ObjectPackage nbtPackage, BlockPosition position, int typeId) {
        if (!this.isUpdateVersion())
            throw new NMSException("You may not use the typeId to create this packet on 1.18+");

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

    public TileEntityTypesInstance getType() {
        return type;
    }

    public void setType(TileEntityTypesInstance type) {
        this.type = type;
    }

    public boolean isUpdateVersion() {
        return JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_18);
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            this.blockPosition = new BlockPosition();
            this.blockPosition.parse(JReflection.getFieldObject(tileEntityDataPacketClass, BlockPosition.blockPositionClass, packet));

            this.id = JReflection.getFieldObject(tileEntityDataPacketClass, int.class, packet);

            this.nbt = new ObjectPackage(JReflection.getFieldObject(tileEntityDataPacketClass, NMS.nbtTagCompoundClass, packet));
        } else {
            int x = JReflection.getFieldObject(tileEntityDataPacketClass, int.class, packet, (i) -> 0);
            int y = JReflection.getFieldObject(tileEntityDataPacketClass, int.class, packet, (i) -> 1);
            int z = JReflection.getFieldObject(tileEntityDataPacketClass, int.class, packet, (i) -> 2);
            this.blockPosition = new BlockPosition(x, y, z);

            if (this.isUpdateVersion()) {
                this.type = new TileEntityTypesInstance(JReflection.getFieldObject(tileEntityDataPacketClass, TileEntityTypesInstance.TILE_ENTITY_TYPE, packet, (i) -> i));
            } else {
                this.id = JReflection.getFieldObject(tileEntityDataPacketClass, int.class, packet, (i) -> i);
            }

            this.nbt = new ObjectPackage(JReflection.getFieldObject(tileEntityDataPacketClass, NMS.nbtTagCompoundClass, packet));
        }
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            if (this.isUpdateVersion()) {
                return JReflection.executeConstructor(tileEntityDataPacketClass, new Class[]{BlockPosition.blockPositionClass, TileEntityTypesInstance.TILE_ENTITY_TYPE, NMS.nbtTagCompoundClass}, blockPosition.build(), type.getHandle(), nbt.getObject());
            } else {
                return JReflection.executeConstructor(tileEntityDataPacketClass, new Class[]{BlockPosition.blockPositionClass, int.class, NMS.nbtTagCompoundClass}, blockPosition.build(), id, nbt.getObject());
            }
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
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_18)) {
            return new OutTileEntityData(this.nbt, (BlockPosition) this.blockPosition.copy(), this.type);
        } else {
            return new OutTileEntityData(this.nbt, (BlockPosition) this.blockPosition.copy(), this.id);
        }
    }
}
