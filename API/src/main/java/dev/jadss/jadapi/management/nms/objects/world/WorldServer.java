package dev.jadss.jadapi.management.nms.objects.world;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.misc.JWorld;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.objects.world.block.Block;
import dev.jadss.jadapi.management.nms.objects.world.block.IBlockData;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntity;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntitySign;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.JReflection;

import java.util.UUID;

public class WorldServer implements NMSObject, NMSManipulable, NMSCopyable {

    private Object world;

    public static final Class<?> worldClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level" : "server." + JReflection.getNMSVersion()) + ".World");
    public static final Class<?> worldServerClass = JReflection.getReflectionClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "level" : JReflection.getNMSVersion()) + ".WorldServer");

    //maybe change this to its own class.
    public static final Class<?> worldStorageNBTClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.storage" : "server." + JReflection.getNMSVersion()) + ".WorldNBTStorage");

    public WorldServer() {
    }

    public WorldServer(Object worldServer) {
        if (!worldServer.getClass().equals(worldServerClass))
            throw new NMSException("The given object is not a valid WorldServer");

        this.world = worldServer;
    }

    public JWorld toWorld() {
        if (world == null) throw new NMSException("What the fuck am I supposed to do with a null world?");

        UUID worldUUID;

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            worldUUID = JReflection.getUnspecificFieldObject(worldStorageNBTClass, UUID.class, JReflection.executeMethod(worldServerClass, "getDataManager", world, new Class[]{}));
        } else {
            worldUUID = JReflection.getUnspecificFieldObject(worldServerClass, UUID.class, world);
        }

        return JWorld.getJWorlds().stream().filter(world -> world.getUUID().equals(worldUUID)).findFirst().orElse(null);
    }

    //Custom methods.

    public TileEntity getTileEntity(BlockPosition position) {
        Object tileEntity;

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            tileEntity = JReflection.executeUnspecificMethod(worldClass, new Class[]{int.class, int.class, int.class}, world, TileEntity.tileEntityClass, Math.floor(position.getX()), Math.floor(position.getY()), Math.floor(position.getZ()));
        } else {
            tileEntity = JReflection.executeUnspecificMethod(worldClass, new Class[]{BlockPosition.blockPositionClass}, world, TileEntity.tileEntityClass, position.build());
        }

        return TileEntitySign.parseTileEntity(tileEntity);
    }


    public IBlockData getBlockData(BlockPosition position) {
        if ((boolean) JReflection.getFieldObject(worldClass, "captureTreeGeneration", this.world)) {
            throw new NMSException("Wtf is bukkit doing? worthless garbage.");
        }

        IBlockData iBlockData = new IBlockData();
        iBlockData.parse(this.getBlockNMS(position));

        return iBlockData;
    }

    public Chunk getChunkAt(BlockPosition position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static final IBlockData AIR_BLOCK = new IBlockData(NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR)));

    public static final int SET_BLOCK_MAINFLAG_WITH_PHYSICS = 3;
    public static final int SET_BLOCK_MAINFLAG_WITHOUT_PHYSICS = JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_10) ? 2 : (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12) ? 18 : 1042);
    public static final int NOTIFY_FLAG = 3;

    public boolean setBlockData(BlockPosition position, IBlockData blockData, boolean applyPhysics) {
        if ((boolean) JReflection.getFieldObject(worldClass, "captureTreeGeneration", this.world)) {
            throw new NMSException("Wtf is bukkit doing? worthless garbage.");
        }

        IBlockData oldBlockData = getBlockData(position);

        //Removes block entity, according to bukkit, let's hope bukkit is correct or I'll SCREAMMMMMMMMMMMMMMMMMMM.
        setBlockNMS(position, AIR_BLOCK, 0); //special case.

        if (applyPhysics) {
            return setBlockNMS(position, blockData, SET_BLOCK_MAINFLAG_WITH_PHYSICS);
        } else {
            boolean success = setBlockNMS(position, blockData, SET_BLOCK_MAINFLAG_WITHOUT_PHYSICS);

            if (success)
                notifyNMS(position, blockData, oldBlockData, NOTIFY_FLAG);

            return success;
        }
    }

    //much care using this.
    public boolean setBlockNMS(BlockPosition position, IBlockData blockData, int flag) {

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            if (blockData.getBlock().isInvalid())
                throw new NMSException("Invalid block data.");

            return JReflection.executeUnspecificMethod(worldClass, new Class[]{int.class, int.class, int.class, Block.blockClass, int.class, int.class}, this.world, boolean.class, position.getX(), position.getY(), position.getZ(), blockData.getBlock(), blockData.getBlock().getBlockData(), flag);
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            return JReflection.executeUnspecificMethod(worldClass, new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass, int.class}, this.world, boolean.class, position.build(), blockData.build(), flag);
        } else {
            return JReflection.executeUnspecificMethod(worldClass, new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass, int.class, int.class}, this.world, boolean.class, position.build(), blockData.build(), flag, 512);
        }
    }

    //Flag probably does nothing from what I've seen.
    public void notifyNMS(BlockPosition blockPosition, IBlockData newBlock, IBlockData oldBlock, int flag) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            JReflection.executeUnspecificMethod(worldServerClass, new Class[]{int.class, int.class, int.class}, this.world, null, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        } else if (JVersion.getServerVersion() == JVersion.v1_8) {
            JReflection.executeUnspecificMethod(worldServerClass, new Class[]{BlockPosition.blockPositionClass}, this.world, null, blockPosition.build());
        } else if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
            JReflection.executeUnspecificMethod(worldServerClass, new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass, IBlockData.iBlockDataClass, int.class}, this.world, null, blockPosition.build(), newBlock.build(), oldBlock.build(), flag);
        }
    }


    //much care with this too, may generate a crash if used incorrectly.
    public Object getBlockNMS(BlockPosition position) {
        return JReflection.executeUnspecificMethod(worldClass, new Class[]{BlockPosition.blockPositionClass}, this.world, IBlockData.iBlockDataClass, position.build());
    }

    @Override
    public NMSObject copy() {
        return new WorldServer(world);
    }

    @Override
    public Object getHandle() {
        return world;
    }

    public void setHandle(Object handle) {
        this.world = handle;
    }
}
