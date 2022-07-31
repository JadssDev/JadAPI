package dev.jadss.jadapi.management.nms.objects.network;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

import java.nio.charset.Charset;

/**
 * Byte buf utils and worker.
 */
public class ByteBufWorker implements NMSObject, NMSManipulable {

    private Object byteBuf;

    public static final Class<?> byteBufClass = JClassReflector.getClass((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8) ? "io.netty.buffer" : "net.minecraft.util.io.netty.buffer") + ".ByteBuf");

    public static final Class<?> unpooledClass = JClassReflector.getClass((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8) ? "io.netty.buffer" : "net.minecraft.util.io.netty.buffer") + ".Unpooled");

    public static ByteBufWorker createByteBuf() {
        return new ByteBufWorker(JMethodReflector.executeMethod(unpooledClass, "buffer", new Class[]{}, null, null));
    }

    public static ByteBufWorker createByteBuf(byte[] bytes) {
        return new ByteBufWorker(JMethodReflector.executeMethod(unpooledClass, "wrappedBuffer", new Class[]{byte[].class}, null, new Object[]{bytes}));
    }

    public ByteBufWorker(Object byteBuf) {
        if (!byteBufClass.isAssignableFrom(byteBuf.getClass()))
            throw new NMSException("You're supposed to give a ByteBuf not a " + byteBuf.getClass() + "!");

        this.byteBuf = byteBuf;
    }

    public byte readByte() {
        return (byte) JMethodReflector.executeMethod(byteBufClass, "readByte", new Class[]{}, byteBuf, null);
    }

    public int readableBytes() {
        return (int) JMethodReflector.executeMethod(byteBufClass, "readableBytes", new Class[]{}, byteBuf, null);
    }

    public ByteBufWorker copyBuffer() {
        int buffersOriginalReaderIndex = this.getReaderIndex();
        this.setReaderIndex(0);

        Object newBuf = JMethodReflector.executeMethod(unpooledClass, "copiedBuffer", new Class[]{byteBufClass}, null, new Object[]{this.byteBuf});

        this.setReaderIndex(buffersOriginalReaderIndex);
        return new ByteBufWorker(newBuf);
    }

    public byte[] readBytes() {
        byte[] bytes = new byte[this.readableBytes()];
        JMethodReflector.executeMethod(byteBufClass, "readBytes", new Class[]{byte[].class}, byteBuf, new Object[]{bytes});
        return bytes;
    }

    public String getReadableStrings() {
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
        return (String) JMethodReflector.executeMethod(byteBufClass, "toString", new Class[]{Charset.class}, byteBuf, new Object[]{charset});
    }

    public int getReaderIndex() {
        return (int) JMethodReflector.executeMethod(byteBufClass, "readerIndex", new Class[]{}, byteBuf, null);
    }

    public void setReaderIndex(int readerIndex) {
        JMethodReflector.executeMethod(byteBufClass, "readerIndex", new Class[]{int.class}, byteBuf, new Object[]{readerIndex});
    }

    public int getWriterIndex() {
        return (int) JMethodReflector.executeMethod(byteBufClass, "writerIndex", new Class[]{}, byteBuf, null);
    }

    public void setWriterIndex(int writerIndex) {
        JMethodReflector.executeMethod(byteBufClass, "writerIndex", new Class[]{int.class}, byteBuf, new Object[]{writerIndex});
    }

    public Object getByteBuf() {
        return byteBuf;
    }

    public Object getHandle() {
        return byteBuf;
    }
}