package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.interfaces.MultipleDefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutTitlePacket extends MultipleDefinedPacket {

    public static final Class<?> SET_TITLE_TEXT_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket");
    public static final Class<?> SET_SUBTITLE_TEXT_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket");
    public static final Class<?> SET_ANIMATION_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket");
    public static final Class<?> CLEAR_TITLE_PACKET = JClassReflector.getClass("net.minecraft.network.protocol.game.ClientboundClearTitlesPacket");

    public static final Class<?> OLD_TITLE_PACKET = JClassReflector.getClass("net.minecraft.server." + NMS.getNMSVersion() + ".PacketPlayOutTitle");

    private Type type;

    //Title & Subtitle
    private IChatBaseComponent text;

    //Times
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public OutTitlePacket() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This Packet was not introduced in 1.7 and versions below.");
    }

    public OutTitlePacket(Type type, IChatBaseComponent text, int fadeIn, int stay, int fadeOut) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This Packet was not introduced in 1.7 and versions below.");

        this.type = type;
        this.text = text;

        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
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

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            this.type = NMSEnum.getEnum(Type.class, JFieldReflector.getObjectFromUnspecificField(OLD_TITLE_PACKET, Type.ENUM_TITLE_TYPE, packet));

            this.text = new IChatBaseComponent();
            this.text.parse(JFieldReflector.getObjectFromUnspecificField(OLD_TITLE_PACKET, IChatBaseComponent.iChatBaseComponentClass, packet));

            this.fadeIn = JFieldReflector.getObjectFromUnspecificField(OLD_TITLE_PACKET, int.class, (i) -> i - 2, packet);
            this.fadeIn = JFieldReflector.getObjectFromUnspecificField(OLD_TITLE_PACKET, int.class, (i) -> i - 1, packet);
            this.fadeIn = JFieldReflector.getObjectFromUnspecificField(OLD_TITLE_PACKET, int.class, (i) -> i, packet);
        } else {
            if (SET_TITLE_TEXT_PACKET.equals(packet.getClass())) {
                this.type = Type.TITLE;

                this.text = new IChatBaseComponent();
                this.text.parse(JFieldReflector.getObjectFromUnspecificField(SET_TITLE_TEXT_PACKET, IChatBaseComponent.iChatBaseComponentClass, packet));

                this.fadeIn = -1;
                this.stay = -1;
                this.fadeOut = -1;
            } else if (SET_SUBTITLE_TEXT_PACKET.equals(packet.getClass())) {
                this.type = Type.SUBTITLE;

                this.text = new IChatBaseComponent();
                this.text.parse(JFieldReflector.getObjectFromUnspecificField(SET_SUBTITLE_TEXT_PACKET, IChatBaseComponent.iChatBaseComponentClass, packet));

                this.fadeIn = -1;
                this.stay = -1;
                this.fadeOut = -1;
            } else if (SET_ANIMATION_PACKET.equals(packet.getClass())) {
                this.type = Type.TIMES;

                this.text = null;

                this.fadeIn = JFieldReflector.getObjectFromUnspecificField(SET_ANIMATION_PACKET, int.class, (i) -> i - 2, packet);
                this.stay = JFieldReflector.getObjectFromUnspecificField(SET_ANIMATION_PACKET, int.class, (i) -> i - 1, packet);
                this.fadeOut = JFieldReflector.getObjectFromUnspecificField(SET_ANIMATION_PACKET, int.class, (i) -> i, packet);
            } else if (CLEAR_TITLE_PACKET.equals(packet.getClass())) {
                if (JFieldReflector.getObjectFromUnspecificField(CLEAR_TITLE_PACKET, boolean.class, packet))
                    this.type = Type.RESET;
                else
                    this.type = Type.CLEAR;

                this.text = null;

                this.fadeIn = -1;
                this.stay = -1;
                this.fadeOut = -1;
            }
        }
    }

    @Override
    public Object build() {
        Object object;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            object = JConstructorReflector.executeConstructor(OLD_TITLE_PACKET, new Class[] {});

            JFieldReflector.setObjectToUnspecificField(OLD_TITLE_PACKET, Type.ENUM_TITLE_TYPE, object, this.type.getNMSObject());

            switch (this.type) {
                case TITLE:
                case SUBTITLE: {
                    JFieldReflector.setObjectToUnspecificField(OLD_TITLE_PACKET, IChatBaseComponent.iChatBaseComponentClass, object, text.build());
                    break;
                }
                case TIMES: {
                    JFieldReflector.setObjectToUnspecificField(OLD_TITLE_PACKET, int.class, (i) -> i - 2, object, fadeIn);
                    JFieldReflector.setObjectToUnspecificField(OLD_TITLE_PACKET, int.class, (i) -> i - 1, object, stay);
                    JFieldReflector.setObjectToUnspecificField(OLD_TITLE_PACKET, int.class, (i) -> i, object, fadeOut);
                }
            }
        } else {
            switch (type) {
                case TITLE: {
                    object = JConstructorReflector.executeConstructor(SET_TITLE_TEXT_PACKET, new Class[] { IChatBaseComponent.iChatBaseComponentClass }, text.build());
                    break;
                }
                case SUBTITLE: {
                    object = JConstructorReflector.executeConstructor(SET_SUBTITLE_TEXT_PACKET, new Class[] { IChatBaseComponent.iChatBaseComponentClass }, text.build());
                    break;
                }
                case TIMES: {
                    object = JConstructorReflector.executeConstructor(SET_ANIMATION_PACKET, new Class[] { int.class, int.class, int.class }, this.fadeIn, this.stay, this.fadeOut);
                    break;
                }
                case RESET:
                case CLEAR: {
                    object = JConstructorReflector.executeConstructor(CLEAR_TITLE_PACKET, new Class[] { boolean.class }, this.type == Type.RESET);
                    break;
                }
                default: {
                    throw new NMSException("Could not identify.");
                }
            }
        }
        return object;
    }

    @Override
    public boolean canParse(Object object) {
        return getParsingClasses().stream()
                .anyMatch(clazz -> clazz.equals(object.getClass()));
    }

    @Override
    public List<Class<?>> getParsingClasses() {
        List<Class<?>> list = new ArrayList<>();
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            list.add(OLD_TITLE_PACKET);
        } else {
            list.add(SET_TITLE_TEXT_PACKET);
            list.add(SET_SUBTITLE_TEXT_PACKET);
            list.add(SET_ANIMATION_PACKET);
            list.add(CLEAR_TITLE_PACKET);
        }
        return list;
    }

    @Override
    public DefinedPacket copy() {
        return new OutTitlePacket(this.type, (IChatBaseComponent) this.text.copy(), this.fadeIn, this.stay, this.fadeOut);
    }

    @Override
    public String toString() {
        return "OutTitlePacket{" +
                "type=" + type +
                ", text=" + text +
                ", fadeIn=" + fadeIn +
                ", stay=" + stay +
                ", fadeOut=" + fadeOut +
                '}';
    }

    public enum Type implements NMSEnum {
        TITLE((byte) 0),
        SUBTITLE((byte) 1),
        TIMES((byte) 3),
        CLEAR((byte) 4),
        RESET((byte) 5);

        public static final Class<?> ENUM_TITLE_TYPE = JClassReflector.getClass("net.minecraft.server." + NMS.getNMSVersion() + ".PacketPlayOutTitle$EnumTitleAction");

        private final byte indexPosition;

        Type(byte index) {
            this.indexPosition = index;
        }

        public static Type customParse(Object object) {
            if (object instanceof Byte) {
                return Arrays.stream(Type.values()).filter(type -> type.indexPosition == (byte) object).findFirst().orElse(null);
            } else if (object instanceof Enum<?>) {
                return Arrays.stream(Type.values()).filter(type -> type.indexPosition == ((Enum<?>) object).ordinal()).findFirst().orElse(null);
            } else return null;
        }

        @Override
        public Object getNMSObject() {
            return JEnumReflector.getEnum(this.indexPosition, ENUM_TITLE_TYPE);
        }

        @Override
        public Class<?> getNMSEnumClass() {
            return ENUM_TITLE_TYPE;
        }
    }
}
