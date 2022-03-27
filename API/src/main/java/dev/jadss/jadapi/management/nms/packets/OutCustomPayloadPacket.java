package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.management.nms.objects.other.Key;
import dev.jadss.jadapi.utils.JReflection;

public class OutCustomPayloadPacket extends DefinedPacket {

    private String channel;
    private PacketDataSerializer data;

    public static final Class<?> customPayloadPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutCustomPayload");

    public OutCustomPayloadPacket() {
    }

    public OutCustomPayloadPacket(String channel, byte[] bytes) {
        this.channel = channel;
        this.data = NMS.newPacketDataSerializer(bytes);
    }

    public OutCustomPayloadPacket(String channel, PacketDataSerializer data) {
        this.channel = channel;
        this.data = data;
    }

    public String getChannel() {
        return channel;
    }

    public PacketDataSerializer getData() {
        return data;
    }

    private static String getChannel(String[] args) {
        return args[0] + ":" + args[1];
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            this.channel = JReflection.getFieldObject(customPayloadPacketClass, Key.keyClass, packet, (i) -> i).toString();
            this.data = new PacketDataSerializer();
            this.data.setPDS(JReflection.getFieldObject(customPayloadPacketClass, PacketDataSerializer.packetDataSerializerClass, packet, (i) -> i));
        } else {
            this.channel = JReflection.getFieldObject(customPayloadPacketClass, String.class, packet);
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                this.data = NMS.newPacketDataSerializer(JReflection.getFieldObject(customPayloadPacketClass, byte[].class, packet));
            } else {
                this.data = new PacketDataSerializer(JReflection.getFieldObject(customPayloadPacketClass, PacketDataSerializer.packetDataSerializerClass, packet));
            }
        }
    }

    @Override
    public Object build() {
        Object packet = JReflection.executeConstructor(customPayloadPacketClass, new Class[]{});

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            JReflection.setFieldObject(customPayloadPacketClass, String.class, packet, this.channel);
        } else {
            String[] keys = this.channel.split(":");
            if (keys.length != 2) throw new NMSException("The channel does not have a divider for namespace and key!");

            Key key = new Key(keys[0], keys[1]);
            JReflection.setFieldObject(customPayloadPacketClass, Key.keyClass, packet, key.build());
        }

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            JReflection.setFieldObject(customPayloadPacketClass, byte[].class, packet, this.data.getASByteBuf().readBytes());
        } else {
            JReflection.setFieldObject(customPayloadPacketClass, PacketDataSerializer.packetDataSerializerClass, packet, this.data.getPDS());
        }

        return packet;
    }

    @Override
    public boolean canParse(Object packet) {
        return customPayloadPacketClass.equals(packet.getClass());
    }

    @Override
    public Class<?> getParsingClass() {
        return customPayloadPacketClass;
    }

    @Override
    public DefinedPacket copy() {
        return new OutCustomPayloadPacket(this.channel, this.data);
    }
}
