package dev.jadss.jadapi.management.nms.objects.world.entities.tile;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.objects.world.entities.types.TileEntityTypesInstance;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.JReflection;

public abstract class TileEntity implements NMSObject, NMSManipulable {

    public static final int MOB_SPAWNER_TYPE = 1;
    public static final int COMMAND_BLOCK_TYPE = 2;
    public static final int BEACON_TYPE = 3;
    public static final int SKULL_TYPE = 4;
    public static final int CONDUIT_TYPE = 5;
    public static final int BANNER_TYPE = 6;
    public static final int STRUCT_BLOCK_TYPE = 7;
    public static final int END_GATEWAY_TYPE = 8;
    public static final int SIGN_TYPE = 9;
    public static final int BED_TYPE = 11;
    public static final int JIGSAW_TYPE = 12;
    public static final int CAMPFIRE_TYPE = 13;
    public static final int BEEHIVE_TYPE = 14;

    protected Object tileEntity;

    public static final Class<?> tileEntityClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.entity" : "server." + JReflection.getNMSVersion()) + ".TileEntity");

    public TileEntity(Object nmsTileEntity) {
        if (nmsTileEntity == null)
            throw new NMSException("The tile entity may not be null!");
        if (!isTileEntityObject(nmsTileEntity))
            throw new NMSException("This is not an NMS TileEntity!");

        tileEntity = nmsTileEntity;
    }

    //todo: maybe add more tile entities?
    public static TileEntity parseTileEntity(Object nmsTileEntity) {
        if (nmsTileEntity == null) throw new NMSException("The tile entity may not be null!");

        if (nmsTileEntity.getClass().equals(TileEntitySign.tileEntitySignClass)) {
            return new TileEntitySign(nmsTileEntity);
        } else {
            return new TileEntity(nmsTileEntity) {
                @Override
                public DefinedPacket getUpdatePacket(BlockPosition position) {
                    throw new UnsupportedOperationException("This tile entity does not support update packets!");
                }
            };
        }
    }

    public boolean isTileEntityObject(Object object) {
        return tileEntityClass.isAssignableFrom(object.getClass());
    }

    public abstract DefinedPacket getUpdatePacket(BlockPosition position);

    public TileEntityTypesInstance getTileEntityType() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12))
            throw new NMSException("Unsupported for now!");

        return new TileEntityTypesInstance(JReflection.getFieldObject(TileEntity.tileEntityClass, TileEntityTypesInstance.TILE_ENTITY_TYPE, tileEntity, (i) -> 0));
    }

    @Override
    public Object getHandle() {
        return tileEntity;
    }

}
