package dev.jadss.jadapi.management.nms.objects.attributes;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.JReflection;

public class AttributeInstance implements NMSObject, NMSManipulable {

    public static final Class<?> attributeModifiableClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.ai.attributes" : "server." + JReflection.getNMSVersion()) + ".AttributeModifiable");
    public static final Class<?> attributeInstanceClass = JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".AttributeInstance");
    public static final Class<?> attributeMapBaseClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.ai.attributes" : "server." + JReflection.getNMSVersion()) + ".AttributeMapBase");

    private final Object nmsObject;
    private final AttributeType type;

    public AttributeInstance(Object nmsAttributeInstance, AttributeType type) {
        if(attributeModifiableClass.equals(nmsAttributeInstance.getClass())) {
            this.nmsObject = nmsAttributeInstance;
            this.type = type;
        } else
            throw new NMSException("This is not a NMS " + "AttributeInstance" + "!");
    }

    public AttributeInstance setValue(double value) {
        JReflection.executeMethod(attributeModifiableClass, "setValue", this.nmsObject, new Class[] { double.class }, value);

        return this;
    }

    public double getValue() {
        return (double) JReflection.executeMethod(attributeModifiableClass, "getValue", this.nmsObject, new Class[] {});
    }

    public AttributeType getType() { return type; }

    @Override
    public Object getHandle() {
        return nmsObject;
    }
}
