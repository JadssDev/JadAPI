package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.EnumHand;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.objects.world.positions.Vec3D;
import dev.jadss.jadapi.utils.JReflection;

import java.util.function.Function;

public class InInteractAtEntityPacket extends DefinedPacket {

    private int entityID;

    private InteractType interactionType;
    private EnumHand handType;
    private boolean sneaking;

    private Vec3D vec3D;

    public static final Class<?> interactEntityPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayInUseEntity");
    public static final Class<?> specialEntityFocusedClass = JReflection.getReflectionClass("net.minecraft.network.protocol.game.PacketPlayInUseEntity$EnumEntityUseAction");
    // /\ -> only available in 1.17+ ofc.

    public InInteractAtEntityPacket() {
        super();
    }

    public InInteractAtEntityPacket(int entityID, InteractType interactionType, EnumHand handType, boolean sneaking, Vec3D vec3D) {
        this.entityID = entityID;

        this.interactionType = interactionType;
        this.handType = handType;
        this.sneaking = sneaking;

        this.vec3D = vec3D;
    }


    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public InteractType getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(InteractType type) {
        this.interactionType = type;
    }

    public EnumHand getHandType() {
        return handType;
    }

    public void setHandType(EnumHand handType) {
        this.handType = handType;
    }

    public boolean isPlayerSneaking() {
        return sneaking;
    }

    public void setPlayerSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public Vec3D getVec3D() {
        return vec3D;
    }

    public void setVec3D(Vec3D vec3D) {
        this.vec3D = vec3D;
    }


    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        this.entityID = JReflection.getFieldObject(interactEntityPacketClass, int.class, packet);

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_16))
            this.sneaking = JReflection.getFieldObject(interactEntityPacketClass, boolean.class, packet);
        else
            this.sneaking = false;

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            Object object = JReflection.getFieldObject(interactEntityPacketClass, specialEntityFocusedClass, packet);

            this.interactionType = NMSEnum.getEnum(InteractType.class, JReflection.getFieldObject(interactEntityPacketClass, specialEntityFocusedClass, packet));

            if (interactionType == InteractType.INTERACT) {
                this.vec3D = new Vec3D(0D, 0D, 0D);
                this.handType = NMSEnum.getEnum(EnumHand.class, JReflection.getFieldObject(object.getClass(), EnumHand.enumHandClass, object));
            } else if (interactionType == InteractType.ATTACK) {
                this.vec3D = new Vec3D(0D, 0D, 0D);
                this.handType = EnumHand.MAIN_HAND;
            } else if (interactionType == InteractType.INTERACT_AT) {
                this.vec3D = new Vec3D();
                this.vec3D.parse(JReflection.getFieldObject(object.getClass(), Vec3D.vec3DClass, object));

                this.handType = NMSEnum.getEnum(EnumHand.class, JReflection.getFieldObject(object.getClass(), EnumHand.enumHandClass, object));
            }
        } else { //just parse everything..... hope nothing fails too.
            this.interactionType = NMSEnum.getEnum(InteractType.class, JReflection.getFieldObject(interactEntityPacketClass, InteractType.interactTypeClass, packet));

            this.vec3D = new Vec3D();
            this.vec3D.parse(JReflection.getFieldObject(interactEntityPacketClass, Vec3D.vec3DClass, packet));

            this.handType = NMSEnum.getEnum(EnumHand.class, JReflection.getFieldObject(interactEntityPacketClass, EnumHand.enumHandClass, packet));
        }
    }

    public Object build() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            Object specialObject = this.interactionType.build();

            if (this.interactionType == InteractType.INTERACT) {
                JReflection.setFieldObject(specialEntityFocusedClass, EnumHand.enumHandClass, specialObject, this.handType.getNMSObject());
            } else if (this.interactionType == InteractType.ATTACK) {
                //Nothing.. to set.
            } else if (this.interactionType == InteractType.INTERACT_AT) {
                JReflection.setFieldObject(specialEntityFocusedClass, EnumHand.enumHandClass, specialObject, this.handType.getNMSObject());
                JReflection.setFieldObject(specialEntityFocusedClass, Vec3D.vec3DClass, specialObject, this.vec3D.build());
            }

            return JReflection.executeConstructor(interactEntityPacketClass, new Class[]{int.class, boolean.class, specialEntityFocusedClass}, entityID, sneaking, specialObject);
        } else {
            Object packet = JReflection.executeConstructor(interactEntityPacketClass, new Class[]{});

            JReflection.setFieldObject(interactEntityPacketClass, int.class, packet, entityID);
            JReflection.setFieldObject(interactEntityPacketClass, InteractType.interactTypeClass, packet, interactionType.getNMSObject());

            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8)) {
                JReflection.setFieldObject(interactEntityPacketClass, Vec3D.vec3DClass, packet, vec3D.build());
            }

            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9)) {
                JReflection.setFieldObject(interactEntityPacketClass, EnumHand.enumHandClass, packet, handType.getNMSObject());
            }

            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_16)) {
                JReflection.setFieldObject(interactEntityPacketClass, boolean.class, packet, sneaking);
            }

            return packet;
        }
    }

    public Class<?> getParsingClass() {
        return interactEntityPacketClass;
    }

    public boolean canParse(Object parse) {
        return interactEntityPacketClass.equals(parse.getClass());
    }

    public DefinedPacket copy() {
        return new InInteractAtEntityPacket(this.entityID, this.interactionType, this.handType, this.sneaking, (Vec3D) this.vec3D.copy());
    }

    public enum InteractType implements NMSEnum, NMSBuildable {
        INTERACT,
        ATTACK,
        INTERACT_AT;

        public static final Class<?> interactTypeClass = JReflection.getReflectionClass((JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16) ?
                "net.minecraft.server." + JReflection.getNMSVersion() + "." + (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) ? "EnumEntityUseAction" : "PacketPlayInUseEntity$EnumEntityUseAction") :
                "net.minecraft.network.protocol.game.PacketPlayInUseEntity$b"));

        private static final InteractType[] VALUES = values();

        /**
         * Only works on versions above 1.17 for the new way they handle interaction types!
         *
         * @return the object that represents this enum.
         */
        public Object build() {
            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_17))
                throw new NMSException("Not supported on this version!");

            return JReflection.getFieldObject(interactTypeClass, Function.class, this.getNMSObject()).apply(null);
        }

        public static InteractType customParse(Object interactType) {
            if (interactType == null)
                return InteractType.INTERACT;

            if (specialEntityFocusedClass.isAssignableFrom(interactType.getClass())) {
                if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16))
                    throw new NMSException("Not supported on this version!");
                return customParse(JReflection.executeMethod(specialEntityFocusedClass, new Class[] {}, interactType, interactTypeClass, (i) -> 0));
            } else if (interactTypeClass.equals(interactType.getClass())) {
                Enum<?> e = (Enum<?>) interactType;
                return VALUES[e.ordinal()];
            } else {
                throw new NMSException("Invalid object! Cannot parse this!");
            }
        }

        @Override
        public Object getNMSObject() {
            return JReflection.getEnum(this.ordinal(), interactTypeClass);
        }

        @Override
        public Class<?> getNMSEnumClass() {
            return interactTypeClass;
        }
    }
}
