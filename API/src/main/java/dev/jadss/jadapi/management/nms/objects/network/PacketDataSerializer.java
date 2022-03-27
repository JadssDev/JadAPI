package dev.jadss.jadapi.management.nms.objects.network;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.JReflection;

public class PacketDataSerializer implements NMSObject, NMSManipulable, NMSCopyable {

    private Object packetDataSerializer;

    public static final Class<?> packetDataSerializerClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network" : "server." + JReflection.getNMSVersion()) + ".PacketDataSerializer");

    public PacketDataSerializer() {
    }

    public PacketDataSerializer(Object packetDataSerializer) {
        if (packetDataSerializer == null) {
            this.packetDataSerializer = NMS.newPacketDataSerializer();
            return;
        }

        if (!packetDataSerializerClass.equals(packetDataSerializer.getClass())) {
            this.packetDataSerializer = JReflection.executeConstructor(packetDataSerializerClass, new Class[]{ ByteBufWorker.byteBufClass }, packetDataSerializer);
            return;
        }
        this.packetDataSerializer = packetDataSerializer;
    }

    public PacketDataSerializer(ByteBufWorker byteBuf) {
        this.packetDataSerializer = JReflection.executeConstructor(packetDataSerializerClass, new Class[] { ByteBufWorker.byteBufClass }, byteBuf.getByteBuf());
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
        JReflection.executeMethod(packetDataSerializerClass, new Class[]{long.class}, packetDataSerializer, packetDataSerializerClass, (i) -> 0, var);
        return this;
    }

    public PacketDataSerializer writeVarInt(int var) {
        JReflection.executeMethod(packetDataSerializerClass, new Class[]{int.class}, packetDataSerializer, packetDataSerializerClass, (i) -> 0, var);
        return this;
    }

    public PacketDataSerializer writeDouble(double d) {
        JReflection.executeMethod(packetDataSerializerClass, "writeDouble", packetDataSerializer, new Class[]{double.class}, d);
        return this;
    }

    public PacketDataSerializer writeShort(int var) {
        JReflection.executeMethod(packetDataSerializerClass, "writeShort", packetDataSerializer, new Class[]{int.class}, var);
        return this;
    }

    public PacketDataSerializer writeString(String var, int i) {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            JReflection.executeMethod(packetDataSerializerClass, new Class[] { String.class }, packetDataSerializer, packetDataSerializerClass, (index) -> 0, var);
        } else {
            JReflection.executeMethod(packetDataSerializerClass, new Class[] { String.class, int.class }, packetDataSerializer, packetDataSerializerClass, (index) -> 0, var, i);
        }
        return this;
    }

    public PacketDataSerializer writeString(String var) {
        this.writeString(var, 32767);
        return this;
    }

    public long readVarLong() {
        return JReflection.executeMethod(packetDataSerializerClass, new Class[]{}, packetDataSerializer, long.class, (i) -> 0);
    }

    public int readVarInt() {
        return JReflection.executeMethod(packetDataSerializerClass, new Class[]{}, packetDataSerializer, int.class, (i) -> 0);
    }

    public double readDouble() {
        return (double) JReflection.executeMethod(packetDataSerializerClass, "readDouble", packetDataSerializer, new Class[]{});
    }

    public int readShort() {
        return (int) JReflection.executeMethod(packetDataSerializerClass, "readUnsignedShort", packetDataSerializer, new Class[]{});
    }

    public String readString(int i) {
        return JReflection.executeMethod(packetDataSerializerClass, new Class[]{int.class}, packetDataSerializer, String.class, (index) -> 0, i);
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

    public Class<?> getParsingClass() {
        return packetDataSerializerClass;
    }

    @Override
    public Object getHandle() {
        return packetDataSerializer;
    }
}
