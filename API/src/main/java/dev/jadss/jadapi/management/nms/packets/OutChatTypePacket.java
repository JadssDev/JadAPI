package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.interfaces.MultipleDefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.utils.JReflection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OutChatTypePacket extends MultipleDefinedPacket {

    public static Class<?> getChatPacket() {
        return JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutChat");
    }

    public static Class<?> getTitlePacket() {
        return JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutTitle");
    }

    public static Class<?> getClientboundTitlePacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket");
    }

    public static Class<?> getClientboundSubtitlePacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket");
    }

    public static Class<?> getClientboundTitleAnimationPacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket");
    }

    public static Class<?> getClientboundActionBarPacket() {
        return JReflection.getReflectionClass("net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket");
    }

    //ChatType (may be title, subtitle, actionbar, chat....)
    private ChatType type;

    //Used by the client to identify a message from a specific user, and the client chooses if it should show it or not, set to 0L, 0L (most and least significant bits) to show all messages.
    private UUID sender;
    //Da message!
    private IChatBaseComponent component;

    //Title Timings.
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public OutChatTypePacket() {
    }

    public OutChatTypePacket(ChatType type, UUID sender, IChatBaseComponent component, int fadeIn, int stay, int fadeOut) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            if (type == TitleMessageType.TITLE || type == TitleMessageType.SUBTITLE || type == TitleMessageType.TIMINGS || type == ChatMessageType.ACTION_BAR)
                throw new NMSException("This packet is not available to 1.7 or lower, at least the arguments given!");

        this.type = type;
        this.sender = sender;
        this.component = component;

        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public ChatType getMessageType() {
        return type;
    }

    public void setMessageType(ChatType messageType) {
        this.type = messageType;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public IChatBaseComponent getComponent() {
        return component;
    }

    public void setComponent(IChatBaseComponent component) {
        this.component = component;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (getChatPacket().equals(packet.getClass())) {
            this.component = new IChatBaseComponent();
            this.component.parse(JReflection.getFieldObject(getChatPacket(), IChatBaseComponent.iChatBaseComponentClass, packet));

            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_16)) {
                this.sender = JReflection.getFieldObject(getChatPacket(), UUID.class, packet);
            } else this.sender = new UUID(0L, 0L);

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                this.type = ChatMessageType.CHAT;
            } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
                this.type = ChatMessageType.customParse(JReflection.getFieldObject(getChatPacket(), byte.class, packet));
            } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_18)) {
                this.type = NMSEnum.getEnum(ChatMessageType.class, (Enum<?>) JReflection.getFieldObject(getChatPacket(), ChatMessageType.chatMessageTypeEnumClass, packet));
            }
        } else if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            //new Title packets.
            if (getClientboundActionBarPacket().equals(packet)) {
                this.type = ChatMessageType.ACTION_BAR;
                this.component = new IChatBaseComponent();
                this.component.parse(JReflection.getFieldObject(getClientboundActionBarPacket(), IChatBaseComponent.iChatBaseComponentClass, packet));
            } else if (getClientboundTitlePacket().equals(packet)) {
                this.type = TitleMessageType.TITLE;
                this.component = new IChatBaseComponent();
                this.component.parse(JReflection.getFieldObject(getClientboundTitlePacket(), IChatBaseComponent.iChatBaseComponentClass, packet));
            } else if (getClientboundSubtitlePacket().equals(packet)) {
                this.type = TitleMessageType.SUBTITLE;
                this.component = new IChatBaseComponent();
                this.component.parse(JReflection.getFieldObject(getClientboundSubtitlePacket(), IChatBaseComponent.iChatBaseComponentClass, packet));
            } else if (getClientboundTitleAnimationPacket().equals(packet)) {
                this.type = TitleMessageType.TIMINGS;
                this.component = new IChatBaseComponent("", false, "");
                this.fadeIn = JReflection.getFieldObject(getClientboundTitleAnimationPacket(), int.class, packet, (i) -> 0);
                this.stay = JReflection.getFieldObject(getClientboundTitleAnimationPacket(), int.class,  packet, (i) -> 1);
                this.fadeOut = JReflection.getFieldObject(getClientboundTitleAnimationPacket(), int.class,  packet, (i) -> 2);
            }
        } else {
            //old Title packets.
            this.component = new IChatBaseComponent();
            this.component.parse(JReflection.getFieldObject(getTitlePacket(), IChatBaseComponent.iChatBaseComponentClass, packet));

            //Type
            this.type = NMSEnum.getEnum(TitleMessageType.class, JReflection.getFieldObject(getTitlePacket(), TitleMessageType.titleMessageTypeEnumClass, packet));

            this.fadeIn = JReflection.getFieldObject(getTitlePacket(), int.class, packet, (i) -> 0);
            this.stay = JReflection.getFieldObject(getTitlePacket(), int.class, packet, (i) -> 1);
            this.fadeOut = JReflection.getFieldObject(getTitlePacket(), int.class, packet, (i) -> 2);
        }
    }

    @Override
    public Object build() {
        Object packet;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
                throw new NMSException("Titles are not supported in 1.7");
            if (type == TitleMessageType.TITLE || type == TitleMessageType.SUBTITLE || type == TitleMessageType.TIMINGS || type == TitleMessageType.RESET || type == TitleMessageType.CLEAR) {
                packet = JReflection.executeConstructor(getTitlePacket(), new Class[]{});
                JReflection.setFieldObject(getTitlePacket(), TitleMessageType.titleMessageTypeEnumClass, packet, type.getNMSObject());

                if (type == TitleMessageType.TITLE || type == TitleMessageType.SUBTITLE) {
                    JReflection.setFieldObject(getTitlePacket(), IChatBaseComponent.iChatBaseComponentClass, packet, component.build());
                } else if (type == TitleMessageType.TIMINGS) {
                    JReflection.setFieldObject(getTitlePacket(), int.class, packet, fadeIn, (i) -> 0);
                    JReflection.setFieldObject(getTitlePacket(), int.class, packet, stay, (i) -> 1);
                    JReflection.setFieldObject(getTitlePacket(), int.class, packet, fadeOut, (i) -> 2);
                }
            } else {
                packet = JReflection.executeConstructor(getChatPacket(), new Class[]{});
                JReflection.setFieldObject(getChatPacket(), IChatBaseComponent.iChatBaseComponentClass, packet, this.component.build());

                if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                    //IDk why but this boolean is not needed, but it is set by default, so we just set it to true anyway, everything should work just fine!
                    JReflection.setFieldObject(getChatPacket(), boolean.class, packet, true);
                } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11)) {
                    JReflection.setFieldObject(getChatPacket(), byte.class, packet, ((ChatMessageType) type).getByte());
                } else {
                    JReflection.setFieldObject(getChatPacket(), ChatMessageType.chatMessageTypeEnumClass, packet, type.getNMSObject());
                }
            }
        } else {
            if (type == TitleMessageType.TITLE) {
                packet = JReflection.executeConstructor(getClientboundTitlePacket(), new Class[]{this.component.getParsingClass()}, this.component.build());
            } else if (type == TitleMessageType.SUBTITLE) {
                packet = JReflection.executeConstructor(getClientboundSubtitlePacket(), new Class[]{this.component.getParsingClass()}, this.component.build());
            } else if (type == TitleMessageType.TIMINGS) {
                packet = JReflection.executeConstructor(getClientboundTitleAnimationPacket(), new Class[]{int.class, int.class, int.class}, this.fadeIn, this.stay, this.fadeOut);
            } else if (type == ChatMessageType.ACTION_BAR) {
                packet = JReflection.executeConstructor(getClientboundActionBarPacket(), new Class[]{this.component.getParsingClass()}, this.component.build());
            } else {
                packet = JReflection.executeConstructor(getChatPacket(), new Class[]{IChatBaseComponent.iChatBaseComponentClass, ChatMessageType.chatMessageTypeEnumClass, UUID.class}, this.component.build(), type.getNMSObject(), (this.sender == null ? new UUID(0L, 0L) : this.sender));
            }
        }

        return packet;
    }

    @Override
    public List<Class<?>> getParsingClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(getChatPacket());
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            list.add(getClientboundTitlePacket());
            list.add(getClientboundSubtitlePacket());
            list.add(getClientboundTitleAnimationPacket());
            list.add(getClientboundActionBarPacket());
        } else {
            list.add(getTitlePacket());
        }
        return list;
    }

    @Override
    public boolean canParse(Object packet) {
        for (Class<?> classy : getParsingClasses())
            if (classy.equals(packet.getClass()))
                return true;

        return false;
    }

    @Override
    public DefinedPacket copy() {
        return new OutChatTypePacket(this.type, new UUID(this.sender.getMostSignificantBits(), this.sender.getLeastSignificantBits()), (IChatBaseComponent) this.component.copy(), this.fadeIn, this.stay, this.fadeOut);
    }

    public interface ChatType extends NMSEnum {
    }

    public enum ChatMessageType implements ChatType {
        CHAT((byte) 0),
        SYSTEM((byte) 1),
        ACTION_BAR((byte) 2);

        public static final Class<?> chatMessageTypeEnumClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.chat" : "server." + JReflection.getNMSVersion()) + ".ChatMessageType");

        private byte b;

        ChatMessageType(byte b) {
            this.b = b;
        }

        public byte getByte() {
            return this.b;
        }

        @Override
        public Object getNMSObject() {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_11))
                throw new NMSException("This enum is not available in 1.11");

            return JReflection.getEnum(this.ordinal(), chatMessageTypeEnumClass);
        }


        /**
         * Gets the enum constant of this type with the specified name.
         *
         * @param e can be an Enum or byte.
         * @return the enum constant with the specified Object.
         */
        public static ChatMessageType customParse(Object e) {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_12)) {
                if (!e.getClass().equals(chatMessageTypeEnumClass))
                    throw new NMSException("The object is not a ChatMessageType enum from nms.");

                return getByByte(JReflection.getFieldObject(chatMessageTypeEnumClass, byte.class, e));
            } else {
                return getByByte((byte) e);
            }
        }

        public static ChatMessageType getByByte(byte b) {
            for (ChatMessageType type : values())
                if (type.b == b)
                    return type;
            return null;
        }

        @Override
        public Class<?> getNMSEnumClass() {
            return chatMessageTypeEnumClass;
        }
    }

    public enum TitleMessageType implements ChatType {
        TITLE,
        SUBTITLE,
        TIMINGS,
        CLEAR,
        RESET;

        public static final Class<?> titleMessageTypeEnumClass = JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction");

        @Override
        public Object getNMSObject() {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) || JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17))
                throw new NMSException("This enum is not available in <1.8 neither >1.16");

            return JReflection.getEnum(this.ordinal(), titleMessageTypeEnumClass);
        }

        @Override
        public Class<?> getNMSEnumClass() {
            return titleMessageTypeEnumClass;
        }
    }
}
