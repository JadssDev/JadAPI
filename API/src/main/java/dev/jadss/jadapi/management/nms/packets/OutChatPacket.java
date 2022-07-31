package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.*;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OutChatPacket extends MultipleDefinedPacket {

    public static final Class<?> OLD_CHAT_PACKET = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutChat");

    //1.19+
    public static final Class<?> CHAT_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundPlayerChatPacket");
    public static final Class<?> SYSTEM_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSystemChatPacket");

    //1.17
    public static final Class<?> ACTION_BAR_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket");

    public static final Class<?> ENCRYPTION_CLASS = JClassReflector.getClass("net.minecraft.util.MinecraftEncryption$b");

    //Global
    private Type type;
    private IChatBaseComponent text;

    //Who sent this text (in 1.16 and above (1.16, only UUID, 1.19+ needs other stuff))
    private ChatSender sender;

    //When it was sent, +1.19
    private Instant time;

    //Only in Chat on newer versions
    private ObjectPackage encryptionOptions = new ObjectPackage(null);


    public OutChatPacket() {

    }

    public OutChatPacket(Type type, IChatBaseComponent text, ChatSender sender, Instant time, ObjectPackage encryptionOptions) {
        this.type = type;
        this.text = text;
        this.sender = sender;
        this.time = time;
        this.encryptionOptions = encryptionOptions;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public IChatBaseComponent getText() {
        return text;
    }

    public void setText(IChatBaseComponent text) {
        this.text = text;
    }

    public ChatSender getSender() {
        return sender;
    }

    public void setSender(ChatSender sender) {
        this.sender = sender;
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

    @Override
    public Object build() {
        Object object;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            object = JConstructorReflector.executeConstructor(OLD_CHAT_PACKET, new Class[]{});

            JFieldReflector.setObjectToUnspecificField(OLD_CHAT_PACKET, IChatBaseComponent.iChatBaseComponentClass, object, this.text.build());

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                //int
                JFieldReflector.setObjectToUnspecificField(OLD_CHAT_PACKET, int.class, object, this.type.getId());
            } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
                //byte
                JFieldReflector.setObjectToUnspecificField(OLD_CHAT_PACKET, byte.class, object, this.type.getId());
            } else {
                //ChatMessageType
                JFieldReflector.setObjectToUnspecificField(OLD_CHAT_PACKET, Type.ENUM_CHAT_TYPE, object, this.type.getNMSObject());
            }

            if (JVersion.getServerVersion() == JVersion.v1_16) {
                JFieldReflector.setObjectToUnspecificField(OLD_CHAT_PACKET, UUID.class, object, this.sender != null ? this.sender.sender : null);
            }
        } else {
            switch (this.type) {
                case CHAT: {
                    if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19)) {
                        object = JConstructorReflector.executeConstructor(CHAT_PACKET, new Class[]{IChatBaseComponent.iChatBaseComponentClass, Optional.class, int.class, CHAT_SENDER_CLASS, Instant.class, ENCRYPTION_CLASS},
                                this.text.build(), Optional.empty(), this.type.getId(), this.sender.build(), time, this.encryptionOptions != null ? this.encryptionOptions.getObject() : null);
                    } else {
                        object = JConstructorReflector.executeConstructor(OLD_CHAT_PACKET, new Class[]{IChatBaseComponent.iChatBaseComponentClass, Type.ENUM_CHAT_TYPE, UUID.class},
                                this.text.build(), this.type.getNMSObject(), sender.sender);
                    }
                    break;
                }
                case SYSTEM: {
                    if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19)) {
                        object = JConstructorReflector.executeConstructor(SYSTEM_PACKET, new Class[]{String.class, int.class},
                                this.text.isJsonMode() ? this.text.getJsonMessage() : IChatBaseComponent.defaultSimpleFormat.replace("%text%", this.text.getMessage()), Type.SYSTEM.getId());
                    } else {
                        object = JConstructorReflector.executeConstructor(OLD_CHAT_PACKET, new Class[]{IChatBaseComponent.iChatBaseComponentClass, Type.ENUM_CHAT_TYPE, UUID.class},
                                this.text.build(), Type.SYSTEM.getNMSObject(), new UUID(0L, 0L));
                    }
                    break;
                }
                case ACTION_BAR: {
                    object = JConstructorReflector.executeConstructor(ACTION_BAR_PACKET, new Class[]{IChatBaseComponent.iChatBaseComponentClass}, this.text.build());
                    break;
                }
                default: {
                    throw new NMSException("Not identified.");
                }
            }
        }
        return object;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            this.text = new IChatBaseComponent();
            this.text.parse(JFieldReflector.getObjectFromUnspecificField(OLD_CHAT_PACKET, IChatBaseComponent.iChatBaseComponentClass, packet));

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                //int
                this.type = NMSEnum.getEnum(Type.class, JFieldReflector.getObjectFromUnspecificField(OLD_CHAT_PACKET, int.class, packet));
            } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
                //byte
                this.type = NMSEnum.getEnum(Type.class, JFieldReflector.getObjectFromUnspecificField(OLD_CHAT_PACKET, byte.class, packet));
            } else {
                //ChatMessageType
                this.type = NMSEnum.getEnum(Type.class, JFieldReflector.getObjectFromUnspecificField(OLD_CHAT_PACKET, Type.ENUM_CHAT_TYPE, packet));
            }

            if (JVersion.getServerVersion() == JVersion.v1_16) {
                this.sender = new ChatSender(JFieldReflector.getObjectFromUnspecificField(OLD_CHAT_PACKET, UUID.class, packet), null, null);
            }

        } else {
            if (ACTION_BAR_PACKET.equals(packet.getClass())) {
                this.type = Type.SYSTEM;

                this.text = new IChatBaseComponent();
                this.text.parse(JFieldReflector.getObjectFromUnspecificField(ACTION_BAR_PACKET, IChatBaseComponent.iChatBaseComponentClass, packet));
            } else {
                if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19)) {
                    this.text = new IChatBaseComponent();
                    if (CHAT_PACKET.equals(packet.getClass())) {
                        this.type = Type.CHAT;

                        this.text.parse(JFieldReflector.getObjectFromUnspecificField(CHAT_PACKET, IChatBaseComponent.iChatBaseComponentClass, packet));

                        this.time = JFieldReflector.getObjectFromUnspecificField(CHAT_PACKET, Instant.class, packet);

                        this.sender = new ChatSender();
                        this.sender.parse(JFieldReflector.getObjectFromUnspecificField(CHAT_PACKET, CHAT_SENDER_CLASS, packet));

                        this.encryptionOptions = new ObjectPackage(JFieldReflector.getObjectFromUnspecificField(CHAT_PACKET, ENCRYPTION_CLASS, packet));
                    } else if (SYSTEM_PACKET.equals(packet.getClass())) {
                        this.type = Type.SYSTEM;

                        this.text.setMessage(JFieldReflector.getObjectFromUnspecificField(SYSTEM_PACKET, String.class, packet));
                    }
                } else {
                    this.type = NMSEnum.getEnum(Type.class, JFieldReflector.getObjectFromUnspecificField(OLD_CHAT_PACKET, Type.ENUM_CHAT_TYPE, packet));

                    this.text = new IChatBaseComponent();
                    this.text.parse(JFieldReflector.getObjectFromUnspecificField(OLD_CHAT_PACKET, IChatBaseComponent.iChatBaseComponentClass, packet));

                    if (this.type == Type.SYSTEM) {
                        this.sender = new ChatSender(new UUID(0L, 0L), null, null);
                    } else {
                        this.sender = new ChatSender(JFieldReflector.getObjectFromUnspecificField(OLD_CHAT_PACKET, UUID.class, packet), null, null);
                    }
                }
            }
        }
    }

    @Override
    public boolean canParse(Object object) {
        return getParsingClasses().stream().anyMatch(clazz -> clazz.equals(object.getClass()));
    }

    @Override
    public List<Class<?>> getParsingClasses() {
        List<Class<?>> list = new ArrayList<>();

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19)) {
            list.add(CHAT_PACKET);
            list.add(SYSTEM_PACKET);
            list.add(ACTION_BAR_PACKET);
        } else {
            list.add(OLD_CHAT_PACKET);
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17))
                list.add(ACTION_BAR_PACKET);
        }
        return list;
    }

    @Override
    public DefinedPacket copy() {
        return new OutChatPacket(this.type, (IChatBaseComponent) this.text.copy(), (ChatSender) sender.copy(),
                (Instant) JConstructorReflector.executeConstructor(Instant.class, new Class[] { long.class, int.class }, this.time.getEpochSecond(), this.time.getNano()), this.encryptionOptions);
    }

    @Override
    public String toString() {
        return "OutChatPacket{" +
                "type=" + type +
                ", text=" + text +
                ", sender=" + sender +
                ", time=" + time +
                ", encryptionOptions=" + encryptionOptions +
                '}';
    }

    public static final Class<?> CHAT_SENDER_CLASS = JClassReflector.getClass("net.minecraft.network.chat.ChatSender");

    public static final class ChatSender implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

        public UUID sender;
        public IChatBaseComponent displayName;
        public IChatBaseComponent senderTeamName;

        public ChatSender() {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_18))
                throw new NMSException("This class does not exist in versions below 1.19");
        }

        public ChatSender(UUID sender, IChatBaseComponent displayName, IChatBaseComponent senderTeamName) {
            this.sender = sender;
            this.displayName = displayName;
            this.senderTeamName = senderTeamName;
        }

        @Override
        public Object build() {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_18))
                throw new NMSException("This class does not exist in versions below 1.19");

            return JConstructorReflector.executeConstructor(CHAT_SENDER_CLASS, new Class[]{UUID.class, IChatBaseComponent.iChatBaseComponentClass, IChatBaseComponent.iChatBaseComponentClass},
                    sender, displayName != null ? displayName.build() : null, senderTeamName != null ? senderTeamName.build() : null);
        }

        @Override
        public void parse(Object object) {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_18))
                throw new NMSException("This class does not exist in versions below 1.19");

            if (object == null)
                return;
            if (!canParse(object))
                throw new NMSException("The packet specified is not parsable by this class.");

            this.sender = JFieldReflector.getObjectFromUnspecificField(CHAT_SENDER_CLASS, UUID.class, object);
            this.displayName = new IChatBaseComponent();
            this.displayName.parse(JFieldReflector.getObjectFromUnspecificField(CHAT_SENDER_CLASS, IChatBaseComponent.iChatBaseComponentClass, (i) -> 0, object));

            this.senderTeamName = new IChatBaseComponent();
            this.senderTeamName.parse(JFieldReflector.getObjectFromUnspecificField(CHAT_SENDER_CLASS, IChatBaseComponent.iChatBaseComponentClass, (i) -> 1, object));
        }

        @Override
        public boolean canParse(Object object) {
            return CHAT_SENDER_CLASS.equals(object.getClass());
        }

        @Override
        public Class<?> getParsingClass() {
            return CHAT_SENDER_CLASS;
        }

        @Override
        public NMSObject copy() {
            return null;
        }

        @Override
        public String toString() {
            return "ChatSender{" +
                    "sender=" + sender +
                    ", displayName=" + displayName +
                    ", senderTeamName=" + senderTeamName +
                    '}';
        }
    }


    public enum Type implements NMSEnum {
        CHAT((byte) 0, JVersion.UNKNOWN),
        SYSTEM((byte) 1, JVersion.v1_7),
        ACTION_BAR((byte) 2, JVersion.v1_8);

        public static final Class<?> ENUM_CHAT_TYPE = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.chat" : "server." + NMS.getNMSVersion()) + ".ChatMessageType");

        private final byte id;
        private final JVersion implementedAt;

        Type(byte id, JVersion implementedAt) {
            this.id = id;
            this.implementedAt = implementedAt;
        }

        public byte getId() {
            return this.id;
        }

        public JVersion getImplementedAt() {
            return this.implementedAt;
        }

        @Override
        public Object getNMSObject() {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19))
                throw new NMSException("Unsupported operation.");

            return JEnumReflector.getEnum(this.ordinal(), ENUM_CHAT_TYPE);
        }

        public static Type customParse(Object object) {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19))
                throw new NMSException("Unsupported operation.");

            if (object.getClass().equals(ENUM_CHAT_TYPE)) {
                return values()[((Enum<?>) object).ordinal()];
            } else if (object instanceof Number) {
                return values()[Byte.parseByte(object.toString())];
            } else return null;
        }


        @Override
        public Class<?> getNMSEnumClass() {
            return ENUM_CHAT_TYPE;
        }
    }
}
