package dev.jadss.jadapi.management.nms.objects.attributes;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.JReflection;

public class Attribute implements NMSObject, NMSManipulable {

    public static final Class<?> attributeBaseClass = JReflection.getReflectionClass("net.minecraft" + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.ai.attributes" : "server." + JReflection.getNMSVersion()) + ".AttributeBase");
    //Inexistent in >1.15
    public static final Class<?> iAttributeClass = JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".IAttribute");

    private final Object handle;
    private final AttributeType type;

    protected Attribute(Object handle, AttributeType type) {
        this.handle = handle;
        this.type = type;
    }

    public AttributeType getType() { return type; }

    public String getName() {
        return (String) JReflection.getUnspecificFieldObject(attributeBaseClass, String.class, handle);
    }

    public static String getNameFromNMS(Object nms) {
        if(attributeBaseClass.isAssignableFrom(nms.getClass()))
            return (String) JReflection.getUnspecificFieldObject(attributeBaseClass, String.class, nms);
        else
            return null;
    }

    @Override
    public Object getHandle() {
        return handle;
    }
}
