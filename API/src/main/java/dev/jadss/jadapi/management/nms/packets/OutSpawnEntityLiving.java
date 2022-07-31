package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JEntity;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.BukkitAsUtility;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.EntityLiving;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class OutSpawnEntityLiving extends DefinedPacket {

    private EntityLiving entityLiving;

    public static final Class<?> spawnEntityLivingPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + "." +
            (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19) ? "PacketPlayOutSpawnEntity" : "PacketPlayOutSpawnEntityLiving"));

    public OutSpawnEntityLiving() {
    }

    public OutSpawnEntityLiving(EntityLiving entityLiving) {
        this.entityLiving = entityLiving;
    }

    public EntityLiving getEntityLiving() {
        return entityLiving;
    }

    public void setEntityLiving(EntityLiving entityLiving) {
        this.entityLiving = entityLiving;
    }

    @Override
    @BukkitAsUtility
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        int entityID = JFieldReflector.getObjectFromUnspecificField(spawnEntityLivingPacketClass, int.class, packet);

        JEntity entity = JEntity.getEntity(entityID);
        if (entity == null)
            if (JadAPI.getInstance().getDebug().doMiscDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCannot Find &3Entity &ewhile parsing &b&lOutSpawnEntityLiving &ePacket!"));

        if (entity != null)
            this.entityLiving = (EntityLiving) NMS.getEntity(entity.getEntity());
    }

    @Override
    public Object build() {
        return JConstructorReflector.executeConstructor(spawnEntityLivingPacketClass, new Class[]{EntityLiving.ENTITY_LIVING}, this.entityLiving.getHandle());
    }

    @Override
    public Class<?> getParsingClass() {
        return spawnEntityLivingPacketClass;
    }

    @Override
    public boolean canParse(Object object) {
        return spawnEntityLivingPacketClass.equals(object.getClass());
    }

    @Override
    public DefinedPacket copy() {
        return new OutSpawnEntityLiving(this.entityLiving);
    }

    @Override
    public String toString() {
        return "OutSpawnEntityLiving{" +
                "entityLiving=" + (entityLiving != null ? entityLiving.getHandle() : null) +
                '}';
    }
}
