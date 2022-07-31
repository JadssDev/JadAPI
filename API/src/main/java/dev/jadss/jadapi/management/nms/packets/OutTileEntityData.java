package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.Others;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntity;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntitySign;
import dev.jadss.jadapi.management.nms.objects.world.entities.types.TileEntityTypesInstance;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class OutTileEntityData extends DefinedPacket {

    public static final Class<?> tileEntityDataPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutTileEntityData");

    private BlockPosition blockPosition;
    private ObjectPackage nbt;

    //Old
    private int id;

    //New
    private TileEntityTypesInstance type;

    public OutTileEntityData() {

    }

    public OutTileEntityData(TileEntity tileEntity, BlockPosition position) {
        this.blockPosition = position;

        if (tileEntity instanceof TileEntitySign) {
            this.nbt = NMS.getNBTFromClass(tileEntity.getHandle());
            if (this.isUpdatedVersion()) {
                this.type = tileEntity.getTileEntityType();
            } else {
                this.id = TileEntity.SIGN_TYPE;
            }
        } else {
            throw new NMSException("Cannot find the ID of the this TileEntity!");
        }
    }

    public OutTileEntityData(ObjectPackage nbtPackage, BlockPosition position, TileEntityTypesInstance type) {
        if (this.isUpdatedVersion())
            throw new NMSException("You may not use the type to create this packet on <1.17");

        this.nbt = nbtPackage;
        this.blockPosition = position;
        this.type = type;
    }

    public OutTileEntityData(ObjectPackage nbtPackage, BlockPosition position, int typeId) {
        if (!this.isUpdatedVersion())
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

    public boolean isUpdatedVersion() {
        return JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_18);
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        //Position
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            this.blockPosition = new BlockPosition();
            this.blockPosition.parse(JFieldReflector.getObjectFromUnspecificField(tileEntityDataPacketClass, BlockPosition.blockPositionClass, (i) -> i, packet));
        } else {
            int x = JFieldReflector.getObjectFromUnspecificField(tileEntityDataPacketClass, int.class, (i) -> 0, packet);
            int y = JFieldReflector.getObjectFromUnspecificField(tileEntityDataPacketClass, int.class, (i) -> 1, packet);
            int z = JFieldReflector.getObjectFromUnspecificField(tileEntityDataPacketClass, int.class, (i) -> 2, packet);
            this.blockPosition = new BlockPosition(x, y, z);
        }

        //Type
        if (this.isUpdatedVersion()) {
            this.type = new TileEntityTypesInstance(JFieldReflector.getObjectFromUnspecificField(tileEntityDataPacketClass, TileEntityTypesInstance.TILE_ENTITY_TYPE, (i) -> i, packet));
        } else {
            this.id = JFieldReflector.getObjectFromUnspecificField(tileEntityDataPacketClass, int.class, (i) -> i, packet);
        }

        //NBT
        this.nbt = new ObjectPackage(JFieldReflector.getObjectFromUnspecificField(tileEntityDataPacketClass, Others.NBT_TAG_COMPOUND_CLASS, packet));
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
            if (this.isUpdatedVersion()) {
                return JConstructorReflector.executeConstructor(tileEntityDataPacketClass, new Class[]{BlockPosition.blockPositionClass, TileEntityTypesInstance.TILE_ENTITY_TYPE, Others.NBT_TAG_COMPOUND_CLASS}, blockPosition.build(), type.getHandle(), nbt.getObject());
            } else {
                return JConstructorReflector.executeConstructor(tileEntityDataPacketClass, new Class[]{BlockPosition.blockPositionClass, int.class, Others.NBT_TAG_COMPOUND_CLASS}, blockPosition.build(), id, nbt.getObject());
            }
        } else {
            return JConstructorReflector.executeConstructor(tileEntityDataPacketClass, new Class[]{int.class, int.class, int.class, int.class, Others.NBT_TAG_COMPOUND_CLASS}, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), id, nbt.getObject());
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

    @Override
    public String toString() {
        return "OutTileEntityData{" +
                "blockPosition=" + blockPosition +
                ", nbt=" + nbt +
                ", id=" + id +
                ", type=" + type +
                '}';
    }
}
