package dev.jadss.jadapi.management.nms.objects.network;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

import java.util.Arrays;

public class PacketDataSerializer implements NMSObject, NMSManipulable, NMSCopyable {

    private Object packetDataSerializer;

    public static final Class<?> DATA_SERIALIZER = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network" : "server." + NMS.getNMSVersion()) + ".PacketDataSerializer");

    public PacketDataSerializer() {
    }

    public PacketDataSerializer(Object packetDataSerializer) {
        if (packetDataSerializer == null) {
            this.packetDataSerializer = NMS.newPacketDataSerializer();
            return;
        }

        if (!DATA_SERIALIZER.equals(packetDataSerializer.getClass())) {
            this.packetDataSerializer = JConstructorReflector.executeConstructor(DATA_SERIALIZER, new Class[]{ByteBufWorker.byteBufClass}, packetDataSerializer);
            return;
        }
        this.packetDataSerializer = packetDataSerializer;
    }

    public PacketDataSerializer(ByteBufWorker byteBuf) {
        this.packetDataSerializer = JConstructorReflector.executeConstructor(DATA_SERIALIZER, new Class[]{ByteBufWorker.byteBufClass}, byteBuf.getByteBuf());
    }

    public Object getPDS() {
        return packetDataSerializer;
    }

    public void setPDS(Object packetDataSerializer) {
        this.packetDataSerializer = packetDataSerializer;
    }

    public ByteBufWorker getASByteBuf() {
        return new ByteBufWorker(packetDataSerializer);
    }

    //Custom methods.

    public PacketDataSerializer writeVarLong(long var) {
        JMethodReflector.executeUnspecificMethod(DATA_SERIALIZER, new Class[]{long.class}, getReturnType(), packetDataSerializer, new Object[]{var});
        return this;
    }

    public PacketDataSerializer writeVarInt(int var) {
        JMethodReflector.executeUnspecificMethod(DATA_SERIALIZER, new Class[]{int.class}, getReturnType(), packetDataSerializer, new Object[]{var});
        return this;
    }

    public PacketDataSerializer writeDouble(double d) {
        JMethodReflector.executeMethod(DATA_SERIALIZER, "writeDouble", new Class[]{double.class}, packetDataSerializer, new Object[]{d});
        return this;
    }

    public PacketDataSerializer writeShort(int var) {
        JMethodReflector.executeMethod(DATA_SERIALIZER, "writeShort", new Class[]{int.class}, packetDataSerializer, new Object[]{var});
        return this;
    }

    public PacketDataSerializer writeString(String var, int i) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            JMethodReflector.executeUnspecificMethod(DATA_SERIALIZER, new Class[]{String.class}, getReturnType(), packetDataSerializer, new Object[]{var});
        } else {
            JMethodReflector.executeUnspecificMethod(DATA_SERIALIZER, new Class[]{String.class, int.class}, getReturnType(), packetDataSerializer, new Object[]{var, i});
        }
        return this;
    }

    public PacketDataSerializer writeString(String var) {
        this.writeString(var, 32767);
        return this;
    }

    public long readVarLong() {
        return JMethodReflector.executeUnspecificMethod(DATA_SERIALIZER, new Class[]{}, long.class, packetDataSerializer, new Object[]{});
    }

    public int readVarInt() {
        return JMethodReflector.executeUnspecificMethod(DATA_SERIALIZER, new Class[]{}, int.class, packetDataSerializer, new Object[]{});
    }

    public double readDouble() {
        return (double) JMethodReflector.executeMethod(DATA_SERIALIZER, "readDouble", packetDataSerializer, new Object[]{});
    }

    public int readShort() {
        return (int) JMethodReflector.executeMethod(DATA_SERIALIZER, "readUnsignedShort", packetDataSerializer, new Object[]{});
    }

    public String readString(int i) {
        return JMethodReflector.executeUnspecificMethod(DATA_SERIALIZER, new Class[]{int.class}, String.class, packetDataSerializer, new Object[]{i});
    }

    public String readString() {
        return this.readString(32767);
    }

    //Custom methods.

    public NMSObject copy() {
        ByteBufWorker buf = new ByteBufWorker(this.packetDataSerializer);
        int readerIndex = buf.getReaderIndex();
        int writerIndex = buf.getWriterIndex();

        buf = buf.copyBuffer();

        buf.setReaderIndex(readerIndex);
        buf.setWriterIndex(writerIndex);

        return new PacketDataSerializer(buf.getByteBuf());
    }

    /**
     * Depending on the version, the result of the method returns different.
     *
     * @return Depends on the version, if above 1.8 it's a packet data serializer, else it's void.
     */
    protected static Class<?> getReturnType() {
        return JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9) ? DATA_SERIALIZER : void.class;
    }

    public Class<?> getParsingClass() {
        return DATA_SERIALIZER;
    }

    @Override
    public Object getHandle() {
        return packetDataSerializer;
    }

    @Override
    public String toString() {
        return "PacketDataSerializer{" +
                "data=" + Arrays.toString(this.getASByteBuf().readBytes()) +
                '}';
    }
}
