package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.management.nms.objects.other.Key;
import dev.jadss.jadapi.utils.JReflection;

//Note: the channel in newer versions is a Minecraft key, use ":" in between the channel namespace and key for better compatibility.
public class InCustomPayloadPacket extends DefinedPacket {

    private String channel;
    private PacketDataSerializer data;

    public static final Class<?> customPayloadPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayInCustomPayload");

    public InCustomPayloadPacket() {
    }

    public InCustomPayloadPacket(String channel, byte[] bytes) {
        this.channel = channel;
        this.data = NMS.newPacketDataSerializer(bytes);
    }

    public InCustomPayloadPacket(String channel, PacketDataSerializer data) {
        this.channel = channel;
        this.data = data;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public PacketDataSerializer getData() {
        return data;
    }

    public void setData(PacketDataSerializer data) {
        this.data = data;
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
        Object packet;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            packet = JReflection.executeConstructor(InCustomPayloadPacket.customPayloadPacketClass, new Class[]{});
            JReflection.setFieldObject(customPayloadPacketClass, String.class, packet, this.channel, (i) -> i);
            JReflection.setFieldObject(customPayloadPacketClass, int.class, packet, this.data.getASByteBuf().readableBytes(), (i) -> i);
            JReflection.setFieldObject(customPayloadPacketClass, byte[].class, packet, this.data.getASByteBuf().readBytes(), (i) -> i);
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            packet = JReflection.executeConstructor(InCustomPayloadPacket.customPayloadPacketClass, new Class[]{});
            JReflection.setFieldObject(customPayloadPacketClass, String.class, packet, this.channel);
            JReflection.setFieldObject(customPayloadPacketClass, PacketDataSerializer.packetDataSerializerClass, packet, this.data.getPDS(), (i) -> i);
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JReflection.executeConstructor(InCustomPayloadPacket.customPayloadPacketClass, new Class[]{});
            JReflection.setFieldObject(customPayloadPacketClass, Key.keyClass, packet, this.channel, (i) -> i);
            JReflection.setFieldObject(customPayloadPacketClass, PacketDataSerializer.packetDataSerializerClass, packet, this.data.getPDS(), (i) -> i);
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_18)) {
            String keys[] = this.channel.split(":");
            String namespace = keys[0];
            String key = keys[1];

            packet = JReflection.executeConstructor(InCustomPayloadPacket.customPayloadPacketClass, new Class[]{Key.keyClass, PacketDataSerializer.packetDataSerializerClass}, new Key(namespace, key).build(), this.data.getPDS());
        } else
            throw new NMSException("Uhh, not gonna lie, Your version is stupidly outdated, or something wrong happened! Sorry!");

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
        return new InCustomPayloadPacket(this.channel, (PacketDataSerializer) this.data.copy());
    }
}
