package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.time.Instant;
import java.util.UUID;

public class InChatPacket extends DefinedPacket {

    public static final Class<?> CHAT_PACKET = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayInChat");
    public static final Class<?> MESSAGE_SIGNATURE_CLASS = JClassReflector.getClass("net.minecraft.network.chat.MessageSignature");

    private String message;
    private Instant time;
    private ObjectPackage encryptionOptions;
    private boolean signedPreview;

    public InChatPacket() {
    }

    public InChatPacket(String message, Instant time, ObjectPackage encryptionOptions, boolean signedPreview) {
        this.message = message;
        this.time = time;
        this.encryptionOptions = encryptionOptions;
        this.signedPreview = signedPreview;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public ObjectPackage getEncryptionOptions() {
        return encryptionOptions;
    }

    public void setEncryptionOptions(ObjectPackage encryptionOptions) {
        this.encryptionOptions = encryptionOptions;
    }

    public boolean isSignedPreview() {
        return signedPreview;
    }

    public void setSignedPreview(boolean signedPreview) {
        this.signedPreview = signedPreview;
    }

    @Override
    public Object build() {
        Object object;
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19)) {
            object = JConstructorReflector.executeConstructor(CHAT_PACKET, new Class[] { String.class, MESSAGE_SIGNATURE_CLASS, boolean.class },
                    this.message, JConstructorReflector.executeConstructor(MESSAGE_SIGNATURE_CLASS, new Class[] { UUID.class, Instant.class, OutChatPacket.ENCRYPTION_CLASS },
                            new UUID(0L, 0L), this.time, (encryptionOptions != null ? encryptionOptions.getObject() : null)),
                    this.signedPreview);
        } else {
            object = JConstructorReflector.executeConstructor(CHAT_PACKET, new Class[] { String.class }, this.message);
        }
        return object;
    }

    @Override
    public void parse(Object packet) {
        this.message = JFieldReflector.getObjectFromUnspecificField(CHAT_PACKET, String.class, packet);

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19)) {
            this.time = JFieldReflector.getObjectFromUnspecificField(CHAT_PACKET, Instant.class, packet);
            this.encryptionOptions = new ObjectPackage(JFieldReflector.getObjectFromUnspecificField(CHAT_PACKET, OutChatPacket.ENCRYPTION_CLASS, packet));
            this.signedPreview = JFieldReflector.getObjectFromUnspecificField(CHAT_PACKET, boolean.class, packet);
        }
    }

    @Override
    public boolean canParse(Object object) {
        return CHAT_PACKET.equals(object.getClass());
    }

    @Override
    public Class<?> getParsingClass() {
        return CHAT_PACKET;
    }

    @Override
    public DefinedPacket copy() {
        return new InChatPacket(this.message,
                (Instant) JConstructorReflector.executeConstructor(Instant.class, new Class[] { long.class, int.class }, this.time.getEpochSecond(), this.time.getNano()),
                this.encryptionOptions,
                this.signedPreview);
    }

    @Override
    public String toString() {
        return "InChatPacket{" +
                "message='" + message + '\'' +
                ", time=" + time +
                ", encryptionOptions=" + encryptionOptions +
                ", signedPreview=" + signedPreview +
                '}';
    }
}
