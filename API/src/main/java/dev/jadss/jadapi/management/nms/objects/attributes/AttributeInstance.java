package dev.jadss.jadapi.management.nms.objects.attributes;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class AttributeInstance implements NMSObject, NMSManipulable {

    public static final Class<?> attributeModifiableClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.ai.attributes" : "server." + NMS.getNMSVersion()) + ".AttributeModifiable");
    public static final Class<?> attributeInstanceClass = JClassReflector.getClass("net.minecraft.server." + NMS.getNMSVersion() + ".AttributeInstance");
    public static final Class<?> attributeMapBaseClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.ai.attributes" : "server." + NMS.getNMSVersion()) + ".AttributeMapBase");

    private final Object nmsObject;
    private final AttributeType type;

    public AttributeInstance(Object nmsAttributeInstance, AttributeType type) {
        if(attributeModifiableClass.equals(nmsAttributeInstance.getClass())) {
            this.nmsObject = nmsAttributeInstance;
            this.type = type;
        } else {
            throw new NMSException("This is not an " + "AttributeInstance" + "!");
        }
    }

    public AttributeInstance setValue(double value) {
        JFieldReflector.setObjectToUnspecificField(attributeModifiableClass, double.class, (i) -> 0, this.nmsObject, value);
        return this;
    }

    public double getValue() {
        return JFieldReflector.getObjectFromUnspecificField(attributeModifiableClass, double.class, (i) -> 0, this.nmsObject);
    }

    public AttributeType getType() { return type; }

    @Override
    public Object getHandle() {
        return nmsObject;
    }
}
