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

public class OutCustomPayloadPacket extends DefinedPacket {

    private String channel;
    private PacketDataSerializer data;

    public static final Class<?> CUSTOM_PAYLOAD = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutCustomPayload");

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
            this.channel = JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, Key.KEY, (i) -> i, packet).toString();
            this.data = new PacketDataSerializer();
            this.data.setPDS(JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, PacketDataSerializer.DATA_SERIALIZER, (i) -> i, packet));
        } else {
            this.channel = JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, String.class, packet);
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                this.data = NMS.newPacketDataSerializer(JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, byte[].class, packet));
            } else {
                this.data = new PacketDataSerializer(JFieldReflector.getObjectFromUnspecificField(CUSTOM_PAYLOAD, PacketDataSerializer.DATA_SERIALIZER, (i) -> i, packet));
            }
        }

        ByteBufWorker byteBuf = this.data.getASByteBuf();
        byteBuf.setReaderIndex(0);
        this.data = NMS.newPacketDataSerializer(byteBuf.readBytes());
    }

    @Override
    public Object build() {
        Object object;

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            String[] keys = this.channel.split(":");
            if (keys.length != 2)
                throw new NMSException("The channel does not have a divider for namespace and key!");

            Key key = new Key(keys[0], keys[1]);
            object = JConstructorReflector.executeConstructor(CUSTOM_PAYLOAD, new Class[]{Key.KEY, PacketDataSerializer.DATA_SERIALIZER}, key.build(), data.getPDS());
        } else {
            object = JConstructorReflector.executeConstructor(CUSTOM_PAYLOAD, new Class[]{});

            JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, String.class, object, this.channel);

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, byte[].class, object, this.data.getASByteBuf().readBytes());
            } else {
                JFieldReflector.setObjectToUnspecificField(CUSTOM_PAYLOAD, PacketDataSerializer.DATA_SERIALIZER, object, this.data.getPDS());
            }
        }

        return object;
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
        return new OutCustomPayloadPacket(this.channel, this.data);
    }

    @Override
    public String toString() {
        return "OutCustomPayloadPacket{" +
                "channel='" + channel + '\'' +
                ", data=" + data +
                '}';
    }
}
