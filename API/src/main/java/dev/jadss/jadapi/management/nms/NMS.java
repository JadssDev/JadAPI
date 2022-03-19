package dev.jadss.jadapi.management.nms;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.objects.network.ByteBufWorker;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.management.nms.objects.world.WorldServer;
import dev.jadss.jadapi.management.nms.objects.world.block.Block;
import dev.jadss.jadapi.management.nms.objects.world.entities.*;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.*;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;

/**
 * Some utils for <b>NMS</b>!
 */
public final class NMS {

    private NMS() {
        throw new NMSException("Fuck off");
    }

    /**
     * Get the <b>NMS MinecraftServer instance</b>.
     *
     * @return The MinecraftServer Object.
     */
    public static Object getMinecraftServer() {
        return JReflection.executeMethod(JReflection.getReflectionClass("org.bukkit.craftbukkit." + JReflection.getNMSVersion() + ".CraftServer"), "getServer", Bukkit.getServer(), new Class[]{});
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

        Object handle = JReflection.executeMethod(entity.getClass(), "getHandle", entity, new Class[]{});

        if (entity instanceof Tameable) {
            return new EntityTameableAnimal(handle) {
            };
        } else if (entity instanceof Animals) {
            return new EntityAnimal(handle) {
            };
        } else if (entity instanceof Ageable) {
            return new EntityAgeable(handle) {
            };
        } else if (entity instanceof Creature) {
            return new EntityCreature(handle) {
            };
        } else if (entity instanceof LivingEntity) {
            if (entity instanceof ArmorStand) {
                return new EntityArmorStand(handle);
            } else if (entity instanceof Player) {
                return new EntityPlayer(handle);
            } else {
                return new EntityLiving(handle) {
                };
            }
        } else {
            if (entity instanceof AreaEffectCloud) {
                return new EntityAreaEffectCloud(handle);
            } else if (entity instanceof ExperienceOrb) {
                return new EntityExperienceOrb(handle);
            } else if (entity instanceof Item) {
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
        return new WorldServer(JReflection.executeMethod(world.getClass(), "getHandle", world, new Class[]{}));
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

    public static final Class<?> nbtTagCompoundClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "nbt" : "server." + JReflection.getNMSVersion()) + ".NBTTagCompound");

    //NBT getters

    /**
     * Gets the NBT of an object that can have NBT in NMS!
     * @param clazz the class of the object!
     * @param nmsHandle the handle in nms
     * @return the NBT of the object!
     */
    public static ObjectPackage getNBTFromClass(Class<?> clazz, Object nmsHandle) {
        return new ObjectPackage(JReflection.executeUnspecificMethod(nbtTagCompoundClass, new Class[]{nbtTagCompoundClass}, nmsHandle, nbtTagCompoundClass, JReflection.executeConstructor(nbtTagCompoundClass, new Class[]{})));
    }

    //Packet Data serializers.

    /**
     * Create a <b>PacketDataSerializer</b> in NMS.
     *
     * @return the PacketDataSerializer.
     */
    public static PacketDataSerializer newPacketDataSerializer() {
        return new PacketDataSerializer(JReflection.executeConstructor(new PacketDataSerializer().getParsingClass(), new Class[]{ByteBufWorker.byteBufClass}, ByteBufWorker.createByteBuf().getByteBuf()));
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
