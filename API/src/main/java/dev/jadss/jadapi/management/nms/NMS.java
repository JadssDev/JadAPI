package dev.jadss.jadapi.management.nms;

import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.objects.network.ByteBufWorker;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.management.nms.objects.world.WorldServer;
import dev.jadss.jadapi.management.nms.objects.world.block.Block;
import dev.jadss.jadapi.management.nms.objects.world.entities.*;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.*;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * Some utils for <b>NMS</b>!
 */
public final class NMS {

    private static String nmsVersion;

    public static void setupNMS() {
        String[] version = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        nmsVersion = version[version.length - 1];
    }

    public static String getNMSVersion() {
        return nmsVersion;
    }

    private NMS() {
        //Utility class
    }

    /**
     * Get the <b>NMS MinecraftServer instance</b>.
     *
     * @return The MinecraftServer Object.
     */
    public static Object getMinecraftServer() {
        return JMethodReflector.executeMethod(JClassReflector.getClass("org.bukkit.craftbukkit." + NMS.getNMSVersion() + ".CraftServer"), "getServer", new Class[]{}, Bukkit.getServer(), new Object[]{});
    }

    /**
     * Get an <b>NMS Entity</b>!
     *
     * @param entity the entity!
     * @return this Entity in a MinecraftEntity class, you may cast this to LivingEntity, etc., if appropriate.
     */
    public static Entity getEntity(org.bukkit.entity.Entity entity) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null!");

        Object handle = JMethodReflector.executeMethod(entity.getClass(), "getHandle", new Class[]{}, entity, new Object[]{});

        if (EntityAnimal.isEntityAnimal(handle)) {
            return new EntityAnimal(handle) {
            };
        } else if (EntityAgeable.isEntityAgeable(handle)) {
            return new EntityAgeable(handle) {
            };
        } else if (EntityMonster.isEntityMonster(handle)) {
            return new EntityMonster(handle) {
            };
        } else if (EntityCreature.isEntityCreature(handle)) {
            return new EntityCreature(handle) {
            };
        } else if (EntityInsentient.isEntityInsentient(handle)) {
            return new EntityInsentient(handle) {
            };
        } else if (EntityLiving.isEntityLiving(handle)) {
            if (EntityArmorStand.isArmorStand(handle)) {
                return new EntityArmorStand(handle);
            } else if (EntityPlayer.isEntityPlayer(handle)) {
                return new EntityPlayer(handle);
            } else {
                return new EntityLiving(handle) {
                };
            }
        } else {
            if (EntityAreaEffectCloud.isAreaEffectCloud(handle)) {
                return new EntityAreaEffectCloud(handle);
            } else if (EntityExperienceOrb.isExperienceOrb(handle)) {
                return new EntityExperienceOrb(handle);
            } else if (EntityItem.isEntityItem(handle)) {
                return new EntityItem(handle);
            } else {
                return new Entity(handle) {
                };
            }
        }
    }

    /**
     * Transforms a world into a MinecraftWorldServer!
     *
     * @param world the world to obtain as WorldServer!
     * @return the {@link WorldServer} object!
     */
    public static WorldServer toWorldServer(World world) {
        return new WorldServer(JMethodReflector.executeMethod(world.getClass(), "getHandle", new Class[]{}, world, new Object[]{}));
    }

    /**
     * Creates a block with a Material!
     *
     * @param material the {@link JMaterial} to use to create such block!
     * @return the {@link Block} instance!
     */
    public static Block createBlock(JMaterial material) {
        return new Block(CraftUtils.getBlock(material), material.getMaterial(JMaterial.Type.BLOCK).getValue());
    }

    //NBT getters«

    /**
     * Gets the NBT of an object that can have NBT in NMS!
     *
     * @param nmsHandle the handle in nms
     * @return the NBT of the object!
     */
    public static ObjectPackage getNBTFromClass(Object nmsHandle) {
        Object nbt = JConstructorReflector.executeConstructor(Others.NBT_TAG_COMPOUND_CLASS, new Class[]{});

        JMethodReflector.executeMethod(Others.NBT_TAG_COMPOUND_CLASS, Others.NBT_SAVE_METHOD.get(), new Class[]{Others.NBT_TAG_COMPOUND_CLASS}, nmsHandle, new Object[]{nbt});

        return new ObjectPackage(nbt);
    }

    //Packet Data serializers.

    /**
     * Create a <b>PacketDataSerializer</b> in NMS.
     *
     * @return the PacketDataSerializer.
     */
    public static PacketDataSerializer newPacketDataSerializer() {
        return new PacketDataSerializer(JConstructorReflector.executeConstructor(PacketDataSerializer.DATA_SERIALIZER, new Class[]{ByteBufWorker.byteBufClass}, ByteBufWorker.createByteBuf().getByteBuf()));
    }

    /**
     * Create a <b>PacketDataSerializer</b> in NMS with the byte array specified.
     *
     * @param bytes The byte array to contain.
     * @return the PacketDataSerializer.
     */
    public static PacketDataSerializer newPacketDataSerializer(byte[] bytes) {
        return new PacketDataSerializer(ByteBufWorker.createByteBuf(bytes).getByteBuf());
    }
}
