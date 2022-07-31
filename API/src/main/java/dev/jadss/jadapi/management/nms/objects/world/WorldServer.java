package dev.jadss.jadapi.management.nms.objects.world;

import dev.jadss.jadapi.JadAPI;
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
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.reflection.JMappings;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class WorldServer implements NMSObject, NMSManipulable, NMSCopyable {

    private Object world;

    public static final Class<?> worldClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level" : "server." + NMS.getNMSVersion()) + ".World");
    public static final Class<?> worldServerClass = JClassReflector.getClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "level" : NMS.getNMSVersion()) + ".WorldServer");

    //maybe change this to its own class.
    public static final Class<?> worldStorageNBTClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.storage" : "server." + NMS.getNMSVersion()) + ".WorldNBTStorage");

    public WorldServer() {
    }

    public WorldServer(Object worldServer) {
        if (!worldServer.getClass().equals(worldServerClass))
            throw new NMSException("The given object is not a valid WorldServer");

        this.world = worldServer;
    }

    public JWorld toWorld() {
        if (world == null)
            throw new NMSException("What the f### am I supposed to do with a null world?");

        UUID worldUUID;

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            worldUUID = JFieldReflector.getObjectFromUnspecificField(worldStorageNBTClass, UUID.class, JMethodReflector.executeMethod(worldServerClass, "getDataManager", world, new Class[]{}));
        } else {
            worldUUID = JFieldReflector.getObjectFromUnspecificField(worldServerClass, UUID.class, world);
        }

        return JWorld.getJWorlds().stream()
                .filter(world -> world.getUUID().equals(worldUUID))
                .findFirst().orElse(null);
    }

    //Custom methods.

    public static JMappings GET_TILE_ENTITY_METHOD = JMappings.create(worldClass)
            .add(JVersion.v1_7, "getTileEntity")
            .add(JVersion.v1_18, "c_")
            .finish();

    public TileEntity getTileEntity(BlockPosition position) {
        Object tileEntity;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            tileEntity = JMethodReflector.executeMethod(worldClass, GET_TILE_ENTITY_METHOD.get(), new Class[]{int.class, int.class, int.class}, world, new Object[]{Math.floor(position.getX()), Math.floor(position.getY()), Math.floor(position.getZ())});
        } else {
            tileEntity = JMethodReflector.executeMethod(worldClass, GET_TILE_ENTITY_METHOD.get(), new Class[]{BlockPosition.blockPositionClass}, world, new Object[]{position.build()});
        }

        if (JadAPI.getInstance().getDebug().doMiscDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eGetting &3Tile Entity &eat &b" + position + "&e, Result -> &a" + tileEntity + "&e!"));

        return TileEntity.parseTileEntity(tileEntity);
    }


    public IBlockData getBlockData(BlockPosition position) {
        if ((boolean) JFieldReflector.getObjectFromField(worldClass, "captureTreeGeneration", this.world)) {
            throw new NMSException("Wtf is bukkit doing? worthless garbage.");
        }

        IBlockData iBlockData = new IBlockData();
        iBlockData.parse(this.getBlockNMS(position));
        if (JVersion.getServerVersion() == JVersion.v1_7)
            iBlockData.setBlock(iBlockData.getBlock().createNewWithData((byte) this.getBlockDataValueNMS(position)));

        return iBlockData;
    }

    private static final IBlockData AIR_BLOCK = new IBlockData(NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR)));

    public static final int SET_BLOCK_MAINFLAG_WITH_PHYSICS = 3;
    public static final int SET_BLOCK_MAINFLAG_WITHOUT_PHYSICS = JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_10) ? 2 : (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12) ? 18 : 1042);
    public static final int NOTIFY_FLAG = 3;

    public boolean setBlockData(BlockPosition position, IBlockData blockData, boolean applyPhysics) {
        if ((boolean) JFieldReflector.getObjectFromField(worldClass, "captureTreeGeneration", this.world)) {
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

            return JMethodReflector.executeUnspecificMethod(worldClass, new Class[]{int.class, int.class, int.class, Block.blockClass, int.class, int.class}, boolean.class, this.world, new Object[]{position.getX(), position.getY(), position.getZ(), blockData.getBlock(), blockData.getBlock().getBlockData(), flag});
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            return JMethodReflector.executeUnspecificMethod(worldClass, new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass, int.class}, boolean.class, this.world, new Object[]{position.build(), blockData.build(), flag});
        } else {
            return JMethodReflector.executeUnspecificMethod(worldClass, new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass, int.class, int.class}, boolean.class, this.world, new Object[]{position.build(), blockData.build(), flag, 512});
        }
    }

    //Flag probably does nothing from what I've seen.
    public void notifyNMS(BlockPosition blockPosition, IBlockData newBlock, IBlockData oldBlock, int flag) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            JMethodReflector.executeUnspecificMethod(worldClass, new Class[]{int.class, int.class, int.class}, void.class, this.world, new Object[]{blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()});
        } else if (JVersion.getServerVersion() == JVersion.v1_8) {
            JMethodReflector.executeUnspecificMethod(worldClass, new Class[]{BlockPosition.blockPositionClass}, void.class, this.world, new Object[]{blockPosition.build()});
        } else if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
            JMethodReflector.executeUnspecificMethod(worldClass, new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass, IBlockData.iBlockDataClass, int.class}, void.class, this.world, new Object[]{blockPosition.build(), oldBlock.build(), newBlock.build(), flag});
        }
    }


    public Object getBlockDataValueNMS(BlockPosition position) {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8))
            throw new NMSException("This method does not exist on 1.8 neither versions above, only 1.7 and downwards!");

        return JMethodReflector.executeMethod(worldClass, "getData", new Class[]{int.class, int.class, int.class}, this.world, new Object[]{position.getX(), position.getY(), position.getZ()});
    }


    public static final JMappings GET_BLOCK_NMS_METHOD = JMappings.create(WorldServer.worldClass)
            .add(JVersion.v1_7, "getType")
            .add(JVersion.v1_18, "a_")
            .finish();

    //much care with this too, may generate a crash if used incorrectly.
    public Object getBlockNMS(BlockPosition position) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            return JMethodReflector.executeMethod(worldClass, GET_BLOCK_NMS_METHOD.get(), new Class[]{int.class, int.class, int.class, boolean.class}, new Object[]{position.getX(), position.getY(), position.getZ(), false});
        } else {
            return JMethodReflector.executeMethod(worldClass, GET_BLOCK_NMS_METHOD.get(), new Class[]{BlockPosition.blockPositionClass}, this.world, new Object[]{position.build()});
        }
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
