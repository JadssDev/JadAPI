package dev.jadss.jadapi.management.nms.objects.registry;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.objects.other.Key;
import dev.jadss.jadapi.utils.JReflection;

import java.util.Map;

public class RegistryAdapter implements NMSObject, NMSManipulable, NMSCopyable {

    private final boolean outdated;
    private final Map<Object, Object> registryMap;
    private final Object registryMaterials;

    public RegistryAdapter(Map<Object, Object> registryMap) {
        this.outdated = true;
        this.registryMap = registryMap;
        this.registryMaterials = null;
    }

    public RegistryAdapter(Object registryMaterials) {
        if (!registryMaterials.getClass().isInstance(Registry.I_REGISTRY))
            throw new NMSException("The RegistryAdapter could not understand the received registry.");

        this.outdated = false;
        this.registryMap = null;
        this.registryMaterials = registryMaterials;
    }

    public Object get(Object key) {
        if (outdated) {
            for (Object keyString : registryMap.keySet())
                if (keyString.equals(key) || (keyString instanceof String && key instanceof String) && ((String) keyString).equalsIgnoreCase((String) key))
                    return registryMap.get(keyString);
            return null;
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            //get using an Object, returns object back.
            return JReflection.executeMethod(Registry.REGISTRY_SIMPLE, new Class[] { Object.class }, registryMaterials, Object.class, (i) -> i, key);
        } else {
            //get using a MinecraftKey, returns object back.
            return JReflection.executeMethod(Registry.I_REGISTRY, new Class[] { Key.keyClass }, registryMaterials, Object.class, (i) -> i, ((Key) key).build());
        }
    }

    public void set(Object key, Object value) {
        if (outdated) {
            throw new NMSException("Adding values to a Map is unsupported due to the limitations!");
//            if (registryMap.containsKey(key))
//                throw new NMSException("You may not set a key that already exists in the registry.");
//            else
//                registryMap.put(key, value);
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            //set using an Object and Object.
            JReflection.executeMethod(Registry.REGISTRY_SIMPLE, new Class[] { Object.class, Object.class }, registryMaterials, null, (i) -> 0, key, value);
        } else {
            //set using a MinecraftKey and object.
            JReflection.executeMethod(Registry.I_REGISTRY, new Class[] { Key.keyClass, Object.class }, registryMaterials, Object.class, (i) -> 0, ((Key) key).build());
        }
    }

    public boolean isOutdated() {
        return outdated;
    }

    @Override
    public Object getHandle() {
        return outdated ? registryMap : registryMaterials;
    }

    @Override
    public NMSObject copy() {
        return new RegistryAdapter(outdated ? registryMap : registryMaterials);
    }
}
