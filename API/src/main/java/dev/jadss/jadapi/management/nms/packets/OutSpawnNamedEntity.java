package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.world.entities.EntityPlayer;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OutSpawnNamedEntity extends DefinedPacket {

    private EntityPlayer player;

    private final Class<?> spawnNamedEntityPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutNamedEntitySpawn");

    public OutSpawnNamedEntity() {
    }

    public OutSpawnNamedEntity(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public Object build() {
        return JReflection.executeConstructor(spawnNamedEntityPacketClass, new Class[]{EntityPlayer.entityHumanClass}, player.getHandle());
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        int entityID = JReflection.getUnspecificFieldObject(spawnNamedEntityPacketClass, int.class, 0, packet);

        Player gettingIt = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (entityID == player.getEntityId())
                gettingIt = player;
        }

        if (gettingIt == null) this.player = null;

        this.player = (EntityPlayer) NMS.getEntity(gettingIt);
    }

    @Override
    public Class<?> getParsingClass() {
        return spawnNamedEntityPacketClass;
    }

    @Override
    public boolean canParse(Object object) {
        return spawnNamedEntityPacketClass.equals(object.getClass());
    }

    @Override
    public DefinedPacket copy() {
        return new OutSpawnNamedEntity(this.player);
    }
}
