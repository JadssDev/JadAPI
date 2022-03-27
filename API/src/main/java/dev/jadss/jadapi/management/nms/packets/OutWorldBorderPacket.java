package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.interfaces.MultipleDefinedPacket;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.utils.JReflection;

import java.util.ArrayList;
import java.util.List;

/**
 * Note this World border packet, if the player is on the nether, be sure to use a centerx and centerz times 8, since 1 block in the nether = 8 blocks in overworld, very IMPORTANT!
 */
public class OutWorldBorderPacket extends MultipleDefinedPacket {

    private WorldBorderAction action;

    public static final int DEFAULT_PORTAL_TELEPORT_BOUNDARY = (29999984);

    private int portalTeleportBoundary = DEFAULT_PORTAL_TELEPORT_BOUNDARY;

    private double centerX;
    private double centerZ;
    private double oldDiameter;
    private double newDiameter;

    private long millisecondsToNewDiameter;

    private int warningTime;
    private int warningDistance;

    private Class<?> getClientboundInitializeBorderPacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket");
    }

    private Class<?> getClientboundSetBorderSizePacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket");
    }

    private Class<?> getClientboundSetBorderLerpSizePacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket");
    }

    private Class<?> getClientboundSetBorderCenterPacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket");
    }

    private Class<?> getClientboundSetBorderWarningDelayPacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket");
    }

    private Class<?> getClientboundSetBorderWarningDistancePacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket");
    }

    private Class<?> getWorldBorderPacket() {
        return JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutWorldBorder");
    }

    public OutWorldBorderPacket() {
    }

    public OutWorldBorderPacket(WorldBorderAction action, int portalTeleportBoundary, double centerX, double centerZ, double oldDiameter, double newDiameter, long millisecondsToNewDiameter, int warningTime, int warningDistance) {
        this.action = action;
        this.portalTeleportBoundary = portalTeleportBoundary;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.oldDiameter = oldDiameter;
        this.newDiameter = newDiameter;
        this.millisecondsToNewDiameter = millisecondsToNewDiameter;
        this.warningTime = warningTime;
        this.warningDistance = warningDistance;
    }

    public WorldBorderAction getAction() {
        return action;
    }

    public void setAction(WorldBorderAction action) {
        this.action = action;
    }

    public int getPortalTeleportBoundary() {
        return portalTeleportBoundary;
    }

    public void setPortalTeleportBoundary(int portalTeleportBoundary) {
        this.portalTeleportBoundary = portalTeleportBoundary;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterZ() {
        return centerZ;
    }

    public void setCenterZ(double centerZ) {
        this.centerZ = centerZ;
    }

    public double getOldDiameter() {
        return oldDiameter;
    }

    public void setOldDiameter(double oldDiameter) {
        if (oldDiameter < 0)
            throw new IllegalArgumentException("Old diameter may not be less than 0");

        this.oldDiameter = oldDiameter;
    }

    public double getNewDiameter() {
        return newDiameter;
    }

    public void setNewDiameter(double newDiameter) {
        this.newDiameter = newDiameter;
    }

    public long getMillisecondsToNewDiameter() {
        return millisecondsToNewDiameter;
    }

    public void setMillisecondsToNewDiameter(long millisecondsToNewDiameter) {
        this.millisecondsToNewDiameter = millisecondsToNewDiameter;
    }

    public int getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(int warningTime) {
        this.warningTime = warningTime;
    }

    public int getWarningDistance() {
        return warningDistance;
    }

    public void setWarningDistance(int warningDistance) {
        this.warningDistance = warningDistance;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            this.action = NMSEnum.getEnum(WorldBorderAction.class, JReflection.getFieldObject(getWorldBorderPacket(), WorldBorderAction.worldBorderActionEnum, packet));
            this.portalTeleportBoundary = JReflection.getFieldObject(getWorldBorderPacket(), int.class, packet, (i) -> 0);
            this.centerX = JReflection.getFieldObject(getWorldBorderPacket(), double.class, packet, (i) -> 0);
            this.centerZ = JReflection.getFieldObject(getWorldBorderPacket(), double.class, packet, (i) -> 1);
            this.oldDiameter = JReflection.getFieldObject(getWorldBorderPacket(), double.class, packet, (i) -> 2);
            this.newDiameter = JReflection.getFieldObject(getWorldBorderPacket(), double.class, packet, (i) -> 3);
            this.millisecondsToNewDiameter = JReflection.getFieldObject(getWorldBorderPacket(), long.class, packet, (i) -> 0);
            this.warningTime = JReflection.getFieldObject(getWorldBorderPacket(), int.class, packet, (i) -> 1);
            this.warningDistance = JReflection.getFieldObject(getWorldBorderPacket(), int.class, packet, (i) -> 2);
        } else {
            Class<?> packetClass = packet.getClass();

            if (getClientboundInitializeBorderPacket().equals(packetClass)) {
                this.centerX = JReflection.getFieldObject(getClientboundInitializeBorderPacket(), double.class, packet, (i) -> 0);
                this.centerZ = JReflection.getFieldObject(getClientboundInitializeBorderPacket(), double.class, packet, (i) -> 1);
                this.oldDiameter = JReflection.getFieldObject(getClientboundInitializeBorderPacket(), double.class, packet, (i) -> 2);
                this.newDiameter = JReflection.getFieldObject(getClientboundInitializeBorderPacket(), double.class, packet, (i) -> 3);
                this.millisecondsToNewDiameter = JReflection.getFieldObject(getClientboundInitializeBorderPacket(), long.class, packet, (i) -> 0);
                this.portalTeleportBoundary = JReflection.getFieldObject(getClientboundInitializeBorderPacket(), int.class, packet, (i) -> 0);
                this.warningDistance = JReflection.getFieldObject(getClientboundInitializeBorderPacket(), int.class, packet, (i) -> 1);
                this.warningTime = JReflection.getFieldObject(getClientboundInitializeBorderPacket(), int.class, packet, (i) -> 2);
            } else if (getClientboundSetBorderSizePacket().equals(packetClass)) {
                this.newDiameter = JReflection.getFieldObject(getClientboundSetBorderSizePacket(), double.class, packet, (i) -> 0);
            } else if (getClientboundSetBorderLerpSizePacket().equals(packetClass)) {
                this.newDiameter = JReflection.getFieldObject(getClientboundSetBorderLerpSizePacket(), double.class, packet, (i) -> 0);
                this.oldDiameter = JReflection.getFieldObject(getClientboundSetBorderLerpSizePacket(), double.class, packet, (i) -> 1);
                this.millisecondsToNewDiameter = JReflection.getFieldObject(getClientboundSetBorderLerpSizePacket(), long.class, packet, (i) -> 0);
            } else if (getClientboundSetBorderCenterPacket().equals(packetClass)) {
                this.centerX = JReflection.getFieldObject(getClientboundSetBorderCenterPacket(), double.class, packet, (i) -> 0);
                this.centerZ = JReflection.getFieldObject(getClientboundSetBorderCenterPacket(), double.class, packet, (i) -> 1);
            } else if (getClientboundSetBorderWarningDelayPacket().equals(packetClass)) {
                this.warningDistance = JReflection.getFieldObject(getClientboundSetBorderWarningDelayPacket(), int.class, packet, (i) -> 0);
            } else if (getClientboundSetBorderWarningDistancePacket().equals(packetClass)) {
                this.warningTime = JReflection.getFieldObject(getClientboundSetBorderWarningDistancePacket(), int.class, packet, (i) -> 0);
            } else throw new NMSException("Could not parse.");
        }
    }

    @Override
    public Object build() {
        Object packet;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JReflection.executeConstructor(getWorldBorderPacket(), new Class[]{});

            JReflection.setFieldObject(getWorldBorderPacket(), WorldBorderAction.worldBorderActionEnum, packet, this.action.getNMSObject());
            JReflection.setFieldObject(getWorldBorderPacket(), int.class, packet, this.portalTeleportBoundary, (i) -> 0);
            JReflection.setFieldObject(getWorldBorderPacket(), double.class, packet, this.centerX, (i) -> 0);
            JReflection.setFieldObject(getWorldBorderPacket(), double.class, packet, this.centerZ, (i) -> 1);
            JReflection.setFieldObject(getWorldBorderPacket(), double.class, packet, this.oldDiameter, (i) -> 2);
            JReflection.setFieldObject(getWorldBorderPacket(), double.class, packet, this.newDiameter, (i) -> 3);
            JReflection.setFieldObject(getWorldBorderPacket(), long.class, packet, this.millisecondsToNewDiameter, (i) -> 0);
            JReflection.setFieldObject(getWorldBorderPacket(), int.class, packet, this.warningTime, (i) -> 1);
            JReflection.setFieldObject(getWorldBorderPacket(), int.class, packet, this.warningDistance, (i) -> 2);

        } else {
            PacketDataSerializer data = NMS.newPacketDataSerializer();
            switch (action) {
                case INITIALIZE:
                    data.writeDouble(this.centerX);
                    data.writeDouble(this.centerZ);
                    data.writeDouble(this.oldDiameter);
                    data.writeDouble(this.newDiameter);
                    data.writeVarLong(this.millisecondsToNewDiameter);
                    data.writeVarInt(this.portalTeleportBoundary);
                    data.writeVarInt(this.warningDistance);
                    data.writeVarInt(this.warningTime);
                    packet = JReflection.executeConstructor(getClientboundInitializeBorderPacket(), new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case SET_SIZE:
                    data.writeDouble(this.newDiameter);
                    packet = JReflection.executeConstructor(getClientboundSetBorderSizePacket(), new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case LERP_SIZE:
                    data.writeDouble(this.oldDiameter);
                    data.writeDouble(this.newDiameter);
                    data.writeVarLong(this.millisecondsToNewDiameter);
                    packet = JReflection.executeConstructor(getClientboundSetBorderLerpSizePacket(), new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case SET_CENTER:
                    data.writeDouble(this.centerX);
                    data.writeDouble(this.centerZ);
                    packet = JReflection.executeConstructor(getClientboundSetBorderCenterPacket(), new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case SET_WARNING_TIME:
                    data.writeVarInt(this.warningTime);
                    packet = JReflection.executeConstructor(getClientboundSetBorderWarningDelayPacket(), new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case SET_WARNING_BLOCKS:
                    data.writeVarInt(this.warningDistance);
                    packet = JReflection.executeConstructor(getClientboundSetBorderWarningDistancePacket(), new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                default:
                    throw new NMSException("The fuck?");
            }
        }

        return packet;
    }

    @Override
    public List<Class<?>> getParsingClasses() {
        List<Class<?>> list = new ArrayList<>();

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            list.add(getClientboundInitializeBorderPacket());
            list.add(getClientboundSetBorderSizePacket());
            list.add(getClientboundSetBorderLerpSizePacket());
            list.add(getClientboundSetBorderCenterPacket());
            list.add(getClientboundSetBorderWarningDelayPacket());
            list.add(getClientboundSetBorderWarningDistancePacket());
        } else {
            list.add(getWorldBorderPacket());
        }

        return list;
    }

    @Override
    public boolean canParse(Object packet) {
        for (Class<?> classy : getParsingClasses())
            if (classy.equals(packet.getClass()))
                return true;

        return false;
    }

    @Override
    public DefinedPacket copy() {
        return new OutWorldBorderPacket(this.action, this.portalTeleportBoundary, this.centerX, this.centerZ, this.oldDiameter, this.newDiameter, this.millisecondsToNewDiameter, this.warningTime, this.warningDistance);
    }

    public enum WorldBorderAction implements NMSEnum {
        SET_SIZE,
        LERP_SIZE,
        SET_CENTER,
        INITIALIZE,
        SET_WARNING_TIME,
        SET_WARNING_BLOCKS;

        public static final Class<?> worldBorderActionEnum = JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutWorldBorder$EnumWorldBorderAction");

        @Override
        public Object getNMSObject() {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) || JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17))
                throw new NMSException("Unsupported!");

            return JReflection.getEnum(this.ordinal(), worldBorderActionEnum);
        }

        @Override
        public Class<?> getNMSEnumClass() {
            return worldBorderActionEnum;
        }
    }
}
