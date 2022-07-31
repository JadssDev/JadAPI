package dev.jadss.jadapi.management.nms.objects.attributes;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class Attribute implements NMSObject, NMSManipulable {

    public static final Class<?> attributeBaseClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.ai.attributes" : "server." + NMS.getNMSVersion()) + ".AttributeBase");

    //Removed in 1.16 \/ for a better alternative /\
    public static final Class<?> iAttributeClass = JClassReflector.getClass("net.minecraft.server." + NMS.getNMSVersion() + ".IAttribute");

    private final Object handle;
    private final AttributeType type;

    protected Attribute(Object handle, AttributeType type) {
        this.handle = handle;
        this.type = type;
    }

    public AttributeType getType() { return type; }

    public String getName() {
        return JFieldReflector.getObjectFromUnspecificField(attributeBaseClass, String.class, handle);
    }

    public static String getNameFromObject(Object nms) {
        if(attributeBaseClass.isAssignableFrom(nms.getClass()))
            return JFieldReflector.getObjectFromUnspecificField(attributeBaseClass, String.class, nms);
        else
            return null;
    }

    @Override
    public Object getHandle() {
        return handle;
    }
}
