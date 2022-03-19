package dev.jadss.jadapi.management.nms.objects.network;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.JReflection;

import java.nio.charset.Charset;

/**
 * Byte buf utils and worker.
 */
public class ByteBufWorker implements NMSObject, NMSManipulable {

    private Object byteBuf;

    public static final Class<?> byteBufClass = JReflection.getReflectionClass((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8) ? "io.netty.buffer" : "net.minecraft.util.io.netty.buffer") + ".ByteBuf");

    public static final Class<?> unpooledClass = JReflection.getReflectionClass((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8) ? "io.netty.buffer" : "net.minecraft.util.io.netty.buffer") + ".Unpooled");

    public static ByteBufWorker createByteBuf() {
        return new ByteBufWorker(JReflection.executeMethod(unpooledClass, "buffer", null, new Class[]{}));
    }

    public static ByteBufWorker createByteBuf(byte[] bytes) {
        return new ByteBufWorker(JReflection.executeMethod(unpooledClass, "wrappedBuffer", null, new Class[]{byte[].class}, bytes));
    }

    public ByteBufWorker(Object byteBuf) {
        if (!byteBufClass.isAssignableFrom(byteBuf.getClass()))
            throw new NMSException("You're supposed to give a ByteBuf not a " + byteBuf.getClass() + "!");
        this.byteBuf = byteBuf;
    }

    public byte readByte() {
        return (byte) JReflection.executeMethod(byteBufClass, "readByte", byteBuf, new Class[]{});
    }

    public int readableBytes() {
        return (int) JReflection.executeMethod(byteBufClass, "readableBytes", byteBuf, new Class[]{});
    }

    public ByteBufWorker copyBuffer() {
        int buffersOriginalReaderIndex = this.getReaderIndex();
        this.setReaderIndex(0);

        Object newBuf = JReflection.executeMethod(unpooledClass, "copiedBuffer", null, new Class[]{byteBufClass}, this.byteBuf);

        this.setReaderIndex(buffersOriginalReaderIndex);
        return new ByteBufWorker(newBuf);
    }

    public byte[] readBytes() {
        byte[] bytes = new byte[this.readableBytes()];
        JReflection.executeMethod(byteBufClass, "readBytes", byteBuf, new Class[]{byte[].class}, bytes);
        return bytes;
    }

    public String getReadableString() {
        ByteBufWorker buf = copyBuffer();
        String line = buf.bufferToString(Charset.forName("UTF-8")).replace("\0", " ");

        for (char character : line.toCharArray()) {
            boolean first = character <= 31;
            boolean second = character >= 256;
            if (first || second) {
                line = line.replace(String.valueOf(character), " ");
            }
        }

        return line.trim();
    }

    public String bufferToString(Charset charset) {
        return (String) JReflection.executeMethod(byteBufClass, "toString", byteBuf, new Class[]{Charset.class}, charset);
    }

    public int getReaderIndex() {
        return (int) JReflection.executeMethod(byteBufClass, "readerIndex", byteBuf, new Class[]{});
    }

    public void setReaderIndex(int readerIndex) {
        JReflection.executeMethod(byteBufClass, "readerIndex", byteBuf, new Class[]{int.class}, readerIndex);
    }

    public int getWriterIndex() {
        return (int) JReflection.executeMethod(byteBufClass, "writerIndex", byteBuf, new Class[]{});
    }

    public void setWriterIndex(int writerIndex) {
        JReflection.executeMethod(byteBufClass, "writerIndex", byteBuf, new Class[]{int.class}, writerIndex);
    }

    public Object getByteBuf() {
        return byteBuf;
    }

    public Object getHandle() {
        return byteBuf;
    }
}