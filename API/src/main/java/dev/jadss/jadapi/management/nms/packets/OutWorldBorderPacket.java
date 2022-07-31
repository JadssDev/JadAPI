package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.interfaces.MultipleDefinedPacket;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

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

    public static final Class<?> INITIALIZE_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket");
    public static final Class<?> SET_BORDER_SIZE_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket");
    public static final Class<?> SET_BORDER_LERP_SIZE_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket");
    public static final Class<?> SET_BORDER_CENTER_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket");
    public static final Class<?> SET_BORDER_WARNING_DELAY_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket");
    public static final Class<?> SET_BORDER_WARNING_DISTANCE_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket");

    public static final Class<?> OLD_WORLD_BORDER_PACKET = JClassReflector.getClass("net.minecraft.server." + NMS.getNMSVersion() + ".PacketPlayOutWorldBorder");

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
            this.action = NMSEnum.getEnum(WorldBorderAction.class, JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, WorldBorderAction.worldBorderActionEnum, (i) -> i, packet));
            this.portalTeleportBoundary = JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, int.class, (i) -> i - 2, packet);
            this.centerX = JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, double.class, (i) -> i - 3, packet);
            this.centerZ = JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, double.class, (i) -> i - 2, packet);
            this.oldDiameter = JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, double.class, (i) -> i - 1, packet);
            this.newDiameter = JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, double.class, (i) -> i, packet);
            this.millisecondsToNewDiameter = JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, long.class, (i) -> i, packet);
            this.warningTime = JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, int.class, (i) -> i - 1, packet);
            this.warningDistance = JFieldReflector.getObjectFromUnspecificField(OLD_WORLD_BORDER_PACKET, int.class, (i) -> i, packet);
        } else {
            Class<?> packetClass = packet.getClass();

            if (INITIALIZE_PACKET.equals(packetClass)) {
                this.action = WorldBorderAction.INITIALIZE;

                this.centerX = JFieldReflector.getObjectFromUnspecificField(INITIALIZE_PACKET, double.class, (i) -> i - 3, packet);
                this.centerZ = JFieldReflector.getObjectFromUnspecificField(INITIALIZE_PACKET, double.class, (i) -> i - 2, packet);
                this.oldDiameter = JFieldReflector.getObjectFromUnspecificField(INITIALIZE_PACKET, double.class, (i) -> i - 1, packet);
                this.newDiameter = JFieldReflector.getObjectFromUnspecificField(INITIALIZE_PACKET, double.class, (i) -> i, packet);
                this.millisecondsToNewDiameter = JFieldReflector.getObjectFromUnspecificField(INITIALIZE_PACKET, long.class, (i) -> i, packet);
                this.portalTeleportBoundary = JFieldReflector.getObjectFromUnspecificField(INITIALIZE_PACKET, int.class, (i) -> i - 2, packet);
                this.warningDistance = JFieldReflector.getObjectFromUnspecificField(INITIALIZE_PACKET, int.class, (i) -> i - 1, packet);
                this.warningTime = JFieldReflector.getObjectFromUnspecificField(INITIALIZE_PACKET, int.class, (i) -> i, packet);
            } else if (SET_BORDER_SIZE_PACKET.equals(packetClass)) {
                this.action = WorldBorderAction.SET_SIZE;

                this.newDiameter = JFieldReflector.getObjectFromUnspecificField(SET_BORDER_SIZE_PACKET, double.class, (i) -> i, packet);
            } else if (SET_BORDER_LERP_SIZE_PACKET.equals(packetClass)) {
                this.action = WorldBorderAction.LERP_SIZE;

                this.newDiameter = JFieldReflector.getObjectFromUnspecificField(SET_BORDER_LERP_SIZE_PACKET, double.class, (i) -> i - 1, packet);
                this.oldDiameter = JFieldReflector.getObjectFromUnspecificField(SET_BORDER_LERP_SIZE_PACKET, double.class, (i) -> i, packet);
                this.millisecondsToNewDiameter = JFieldReflector.getObjectFromUnspecificField(SET_BORDER_LERP_SIZE_PACKET, long.class, (i) -> i, packet);
            } else if (SET_BORDER_CENTER_PACKET.equals(packetClass)) {
                this.action = WorldBorderAction.SET_CENTER;

                this.centerX = JFieldReflector.getObjectFromUnspecificField(SET_BORDER_CENTER_PACKET, double.class, (i) -> i - 1, packet);
                this.centerZ = JFieldReflector.getObjectFromUnspecificField(SET_BORDER_CENTER_PACKET, double.class, (i) -> i, packet);
            } else if (SET_BORDER_WARNING_DELAY_PACKET.equals(packetClass)) {
                this.action = WorldBorderAction.SET_WARNING_TIME;

                this.warningDistance = JFieldReflector.getObjectFromUnspecificField(SET_BORDER_WARNING_DELAY_PACKET, int.class, (i) -> i, packet);
            } else if (SET_BORDER_WARNING_DISTANCE_PACKET.equals(packetClass)) {
                this.action = WorldBorderAction.SET_WARNING_BLOCKS;

                this.warningTime = JFieldReflector.getObjectFromUnspecificField(SET_BORDER_WARNING_DISTANCE_PACKET, int.class, (i) -> i, packet);
            } else throw new NMSException("Could not parse.");
        }
    }

    @Override
    public Object build() {
        Object packet;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JConstructorReflector.executeConstructor(OLD_WORLD_BORDER_PACKET, new Class[]{});

            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, WorldBorderAction.worldBorderActionEnum, packet, this.action.getNMSObject());
            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, int.class, (i) -> i - 2, packet, this.portalTeleportBoundary);
            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, double.class, (i) -> i - 3, packet, this.centerX);
            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, double.class, (i) -> i - 2, packet, this.centerZ);
            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, double.class, (i) -> i - 1, packet, this.oldDiameter);
            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, double.class, (i) -> i, packet, this.newDiameter);
            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, long.class, (i) -> i, packet, this.millisecondsToNewDiameter);
            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, int.class, (i) -> i - 1, packet, this.warningTime);
            JFieldReflector.setObjectToUnspecificField(OLD_WORLD_BORDER_PACKET, int.class, (i) -> i, packet, this.warningDistance);

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
                    packet = JConstructorReflector.executeConstructor(INITIALIZE_PACKET, new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case SET_SIZE:
                    data.writeDouble(this.newDiameter);
                    packet = JConstructorReflector.executeConstructor(SET_BORDER_SIZE_PACKET, new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case LERP_SIZE:
                    data.writeDouble(this.oldDiameter);
                    data.writeDouble(this.newDiameter);
                    data.writeVarLong(this.millisecondsToNewDiameter);
                    packet = JConstructorReflector.executeConstructor(SET_BORDER_LERP_SIZE_PACKET, new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case SET_CENTER:
                    data.writeDouble(this.centerX);
                    data.writeDouble(this.centerZ);
                    packet = JConstructorReflector.executeConstructor(SET_BORDER_CENTER_PACKET, new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case SET_WARNING_TIME:
                    data.writeVarInt(this.warningTime);
                    packet = JConstructorReflector.executeConstructor(SET_BORDER_WARNING_DELAY_PACKET, new Class[]{data.getParsingClass()}, data.getPDS());
                    break;
                case SET_WARNING_BLOCKS:
                    data.writeVarInt(this.warningDistance);
                    packet = JConstructorReflector.executeConstructor(SET_BORDER_WARNING_DISTANCE_PACKET, new Class[]{data.getParsingClass()}, data.getPDS());
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
            list.add(INITIALIZE_PACKET);
            list.add(SET_BORDER_SIZE_PACKET);
            list.add(SET_BORDER_LERP_SIZE_PACKET);
            list.add(SET_BORDER_CENTER_PACKET);
            list.add(SET_BORDER_WARNING_DELAY_PACKET);
            list.add(SET_BORDER_WARNING_DISTANCE_PACKET);
            list.add(SET_BORDER_WARNING_DELAY_PACKET);
            list.add(SET_BORDER_WARNING_DISTANCE_PACKET);
        } else {
            list.add(OLD_WORLD_BORDER_PACKET);
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

        public static final Class<?> worldBorderActionEnum = JClassReflector.getClass("net.minecraft.server." + NMS.getNMSVersion() + ".PacketPlayOutWorldBorder$EnumWorldBorderAction");

        @Override
        public Object getNMSObject() {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) || JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17))
                throw new NMSException("Unsupported!");

            return JEnumReflector.getEnum(this.ordinal(), worldBorderActionEnum);
        }

        @Override
        public Class<?> getNMSEnumClass() {
            return worldBorderActionEnum;
        }
    }

    @Override
    public String toString() {
        return "OutWorldBorderPacket{" +
                "action=" + action +
                ", portalTeleportBoundary=" + portalTeleportBoundary +
                ", centerX=" + centerX +
                ", centerZ=" + centerZ +
                ", oldDiameter=" + oldDiameter +
                ", newDiameter=" + newDiameter +
                ", millisecondsToNewDiameter=" + millisecondsToNewDiameter +
                ", warningTime=" + warningTime +
                ", warningDistance=" + warningDistance +
                '}';
    }
}
