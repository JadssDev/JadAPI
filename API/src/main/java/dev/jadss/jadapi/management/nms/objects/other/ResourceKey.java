package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class ResourceKey implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    private Key namespace;
    private Key path;

    public static final Class<?> RESOURCE_KEY_CLASS = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "resources" : "server." + NMS.getNMSVersion()) + ".ResourceKey");

    public ResourceKey() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15))
            throw new NMSException("ResourceKey is not available in versions below 1.16.");
    }

    public ResourceKey(Key namespace, Key path) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15))
            throw new NMSException("ResourceKey is not available in versions below 1.16.");

        this.namespace = namespace;
        this.path = path;
    }

    @Override
    public Object build() {
        return JConstructorReflector.executeConstructor(RESOURCE_KEY_CLASS, new Class[] { Key.KEY, Key.KEY}, namespace.build(), path.build());
    }

    @Override
    public boolean canParse(Object object) {
        return RESOURCE_KEY_CLASS.equals(object.getClass());
    }

    @Override
    public void parse(Object object) {
        if (object == null)
            return;
        if(!canParse(object))
            throw new NMSException("Cannot parse this object.");

        this.namespace = new Key();
        this.namespace.parse(JFieldReflector.getUnspecificField(RESOURCE_KEY_CLASS, Key.KEY, (i) -> 0));
        this.path = new Key();
        this.path.parse(JFieldReflector.getUnspecificField(RESOURCE_KEY_CLASS, Key.KEY, (i) -> 1));
    }

    @Override
    public Class<?> getParsingClass() {
        return RESOURCE_KEY_CLASS;
    }

    @Override
    public NMSObject copy() {
        return new ResourceKey(this.namespace != null ? (Key) this.namespace.copy() : null, this.path != null ? (Key) this.path.copy() : null);
    }

    @Override
    public String toString() {
        return "ResourceKey{" +
                "namespace=" + namespace +
                ", path=" + path +
                '}';
    }
}
