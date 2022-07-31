package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.EnumProtocol;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class InHandshakePacket extends DefinedPacket {

    private int protocol;
    private String hostname;
    private int port;
    private EnumProtocol enumProtocol;

    public static final Class<?> handshakePacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.handshake" : "server." + NMS.getNMSVersion()) + ".PacketHandshakingInSetProtocol");

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

        this.protocol = JFieldReflector.getObjectFromUnspecificField(handshakePacketClass, int.class, (i) -> JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? 1 : 0, packet);
        this.hostname = JFieldReflector.getObjectFromUnspecificField(handshakePacketClass, String.class, packet);
        Integer port = JFieldReflector.getObjectFromUnspecificField(handshakePacketClass, int.class, (i) -> i, packet);

        if (port != null)
            this.port = port;
        else
            this.port = -1;

        this.enumProtocol = NMSEnum.getEnum(EnumProtocol.class, JFieldReflector.getObjectFromUnspecificField(handshakePacketClass, EnumProtocol.enumProtocolClass, packet));
    }

    public Object build() {
        Object packet;

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JConstructorReflector.executeConstructor(getParsingClass(), new Class[]{});

            JFieldReflector.setObjectToUnspecificField(handshakePacketClass, int.class, packet, this.protocol);
            JFieldReflector.setObjectToUnspecificField(handshakePacketClass, String.class, packet, this.hostname);
            JFieldReflector.setObjectToUnspecificField(handshakePacketClass, int.class, (i) -> 1, packet, this.port);
            JFieldReflector.setObjectToUnspecificField(handshakePacketClass, EnumProtocol.enumProtocolClass, packet, this.enumProtocol.getNMSObject());
        } else {
            PacketDataSerializer dataSerializer = NMS.newPacketDataSerializer();
            dataSerializer.writeVarInt(this.protocol);
            dataSerializer.writeString(this.hostname);
            dataSerializer.writeShort(this.port);
            dataSerializer.writeVarInt(this.enumProtocol.getId());
            packet = JConstructorReflector.executeConstructor(getParsingClass(), new Class[]{dataSerializer.getParsingClass()}, dataSerializer.getPDS());
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

    @Override
    public String toString() {
        return "InHandshakePacket{" +
                "protocol=" + protocol +
                ", hostname='" + hostname + '\'' +
                ", port=" + port +
                ", enumProtocol=" + enumProtocol +
                '}';
    }
}
