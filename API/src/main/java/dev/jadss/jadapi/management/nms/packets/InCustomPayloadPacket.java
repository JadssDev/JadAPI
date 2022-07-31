package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.network.ByteBufWorker;
import dev.jadss.jadapi.management.nms.objects.network.PacketDataSerializer;
import dev.jadss.jadapi.management.nms.objects.other.Key;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

//Note: the channel in newer versions is a Minecraft key, use ":" and ALWAYS lowercase in between the channel namespace and key for better compatibility.
public class InCustomPayloadPacket extends DefinedPacket {

    private String channel;
    private PacketDataSerializer data;

    public static final Class<?> CUSTOM_PAYLOAD = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayInCustomPayload");

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
            this.channel = JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, Key.KEY, (i) -> i, packet).toString();
        } else {
            this.channel = JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, String.class, packet);
        }

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            this.data = NMS.newPacketDataSerializer(JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, byte[].class, packet));
        } else {
            this.data = (PacketDataSerializer) new PacketDataSerializer(JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, PacketDataSerializer.DATA_SERIALIZER, packet)).copy();
        }

        ByteBufWorker byteBuf = this.data.getASByteBuf();
        byteBuf.setReaderIndex(0);
        this.data = NMS.newPacketDataSerializer(byteBuf.readBytes());
    }

    @Override
    public Object build() { //Redo this.
        Object packet;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            packet = JConstructorReflector.executeConstructor(InCustomPayloadPacket.CUSTOM_PAYLOAD, new Class[]{});
            JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, String.class, (i) -> i, packet, this.channel);
            JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, int.class, (i) -> i, packet, this.data.getASByteBuf().readableBytes());
            JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, byte[].class, (i) -> i, packet, this.data.getASByteBuf().readBytes());
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            packet = JConstructorReflector.executeConstructor(InCustomPayloadPacket.CUSTOM_PAYLOAD, new Class[]{});
            JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, String.class, packet, this.channel);
            JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, PacketDataSerializer.DATA_SERIALIZER, (i) -> i, packet, this.data.getPDS());
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JConstructorReflector.executeConstructor(InCustomPayloadPacket.CUSTOM_PAYLOAD, new Class[]{});

            String[] keys = this.channel.split(":");
            if (keys.length != 2)
                throw new IllegalArgumentException("Keys should have one : to divide the namespace from key.");
            String namespace = keys[0];
            String key = keys[1];

            JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, Key.KEY, (i) -> i, packet, new Key(namespace, key).build());

            JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, PacketDataSerializer.DATA_SERIALIZER, (i) -> i, packet, this.data.getPDS());
        } else {
            String[] keys = this.channel.split(":");
            if (keys.length != 2)
                throw new IllegalArgumentException("Keys should have one : to divide the namespace from key.");
            String namespace = keys[0];
            String key = keys[1];

            packet = JConstructorReflector.executeConstructor(InCustomPayloadPacket.CUSTOM_PAYLOAD, new Class[]{Key.KEY, PacketDataSerializer.DATA_SERIALIZER}, new Key(namespace, key).build(), this.data.getPDS());
        }

        return packet;
    }

    @Override
    public boolean canParse(Object packet) {
        return CUSTOM_PAYLOAD.equals(packet.getClass());
    }

    @Override
    public Class<?> getParsingClass() {
        return CUSTOM_PAYLOAD;
    }

    @Override
    public DefinedPacket copy() {
        return new InCustomPayloadPacket(this.channel, (PacketDataSerializer) this.data.copy());
    }

    @Override
    public String toString() {
        return "InCustomPayloadPacket{" +
                "channel='" + channel + '\'' +
                ", data=" + data +
                '}';
    }
}
