package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.EnumProtocol;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.utils.JReflection;

public class InHandshakePacket extends DefinedPacket {

    private int protocol;
    private String hostname;
    private int port;
    private EnumProtocol enumProtocol;

    public static final Class<?> handshakePacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.handshake" : "server." + JReflection.getNMSVersion()) + ".PacketHandshakingInSetProtocol");

    public InHandshakePacket() {
    }

    public InHandshakePacket(int protocol, String hostname, int port, EnumProtocol enumProtocol) {
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
        this.enumProtocol = enumProtocol;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public JVersion getProtocolVersion() {
        return JVersion.parseProtocol(this.protocol);
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public EnumProtocol getProtocolEnum() {
        return enumProtocol;
    }

    public void setProtocolEnum(EnumProtocol enumProtocol) {
        this.enumProtocol = enumProtocol;
    }

    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        this.protocol = JReflection.getUnspecificFieldObject(handshakePacketClass, int.class, (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? 1 : 0), packet);
        this.hostname = JReflection.getUnspecificFieldObject(handshakePacketClass, String.class, packet);
        Integer i = JReflection.getUnspecificFieldObject(handshakePacketClass, int.class, (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? 2 : 1), packet);

        if (i != null)
            this.port = i;
        else
            this.port = -1;
        this.enumProtocol = NMSEnum.getEnum(EnumProtocol.class, (Enum<?>) JReflection.getUnspecificFieldObject(handshakePacketClass, EnumProtocol.enumProtocolClass, packet));
    }

    public Object build() {
        Object packet;

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JReflection.executeConstructor(getParsingClass(), new Class[]{});

            JReflection.setUnspecificField(handshakePacketClass, int.class, 0, packet, this.protocol);
            JReflection.setUnspecificField(handshakePacketClass, String.class, 0, packet, this.hostname);
            JReflection.setUnspecificField(handshakePacketClass, int.class, 1, packet, this.port);
            JReflection.setUnspecificField(handshakePacketClass, EnumProtocol.enumProtocolClass, 0, packet, this.enumProtocol.getNMSEnumClass());
        } else {
            PacketDataSerializer dataSerializer = NMS.newPacketDataSerializer();
            dataSerializer.writeVarInt(this.protocol);
            dataSerializer.writeString(this.hostname);
            dataSerializer.writeShort(this.port);
            dataSerializer.writeVarInt(this.enumProtocol.getId());

            packet = JReflection.executeConstructor(getParsingClass(), new Class[]{dataSerializer.getParsingClass()}, dataSerializer.getPDS());
        }

        return packet;
    }

    public Class<?> getParsingClass() {
        return handshakePacketClass;
    }

    public boolean canParse(Object object) {
        return getParsingClass().equals(object.getClass());
    }

    public DefinedPacket copy() {
        return new InHandshakePacket(this.protocol, this.hostname, this.port, this.enumProtocol);
    }
}
