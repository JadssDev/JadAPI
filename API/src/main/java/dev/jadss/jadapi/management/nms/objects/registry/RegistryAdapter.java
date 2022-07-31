package dev.jadss.jadapi.management.nms.objects.registry;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.objects.other.Key;
import dev.jadss.jadapi.management.nms.objects.other.ResourceKey;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

import java.util.Map;

public class RegistryAdapter implements NMSObject, NMSManipulable, NMSCopyable {

    private final boolean outdated;
    private final Map<Object, Object> registryMap;
    private final Object registryMaterials;

    public static final Class<?> LIFE_CYCLE_CLASS = JClassReflector.getClass("com.mojang.serialization.Lifecycle");

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

    public ResourceKey getResourceKeyOfRegistry() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_16)) {
            ResourceKey key = new ResourceKey();
            key.parse(JFieldReflector.getObjectFromUnspecificField(Registry.I_REGISTRY, ResourceKey.RESOURCE_KEY_CLASS, registryMaterials));
            return key;
        } else {
            throw new NMSException("This functionality is not available in 1.15 and below");
        }
    }

    public Object get(Object key) {
        if (outdated) {
            for (Object keyString : registryMap.keySet())
                if (keyString.equals(key) || (key instanceof String && ((String) keyString).equalsIgnoreCase((String) key)))
                    return registryMap.get(keyString);

            return null;
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            //get using an Object, returns object back.
            return JMethodReflector.executeUnspecificMethod(Registry.REGISTRY_SIMPLE, new Class[]{Object.class}, Object.class, registryMaterials, new Object[]{key});
        } else {
            //get using a MinecraftKey, returns object back.
            if (JVersion.getServerVersion() == JVersion.v1_13) {
                //Special case where they added 2 methods and so we have to go specific.
                return JMethodReflector.executeMethod(Registry.I_REGISTRY, "get", registryMaterials, new Object[]{key});
            } else {
                //Every other case of an updated version.
                return JMethodReflector.executeUnspecificMethod(Registry.I_REGISTRY, new Class[]{Key.KEY}, Object.class, registryMaterials, new Object[]{key});
            }
        }
    }

    public void set(Object key, Object value) {
        if (outdated) {
            throw new NMSException("Adding values to a Map is unsupported due to limitations!");
//            if (registryMap.containsKey(key))
//                throw new NMSException("You may not set a key that already exists in the registry.");
//            else
//                registryMap.put(key, value);
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            //set using an Object and Object, return void.
            JMethodReflector.executeUnspecificMethod(Registry.REGISTRY_SIMPLE, new Class[]{Object.class, Object.class}, void.class, registryMaterials, new Object[]{key, value});
        } else if (JVersion.getServerVersion() == JVersion.v1_13) {
            //set using a MinecraftKey and object, return the object.
            JMethodReflector.executeUnspecificMethod(Registry.I_REGISTRY, new Class[]{Key.KEY, Object.class}, void.class, registryMaterials, new Object[]{key, value});
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            //set using a MinecraftKey and object, return the object.
            JMethodReflector.executeUnspecificMethod(Registry.WRITABLE_REGISTRY, new Class[]{Key.KEY, Object.class}, Object.class, registryMaterials, new Object[]{key, value});
        } else {
            //Set using a ResourceKey, object and Lifecycle, returns the object inputted.
            JMethodReflector.executeUnspecificMethod(Registry.WRITABLE_REGISTRY, new Class[]{ResourceKey.RESOURCE_KEY_CLASS, Object.class, LIFE_CYCLE_CLASS}, registryMaterials, new Object[]{key, value,
                    JMethodReflector.executeMethod(LIFE_CYCLE_CLASS, "stable", new Class[]{}, null)});
        }
    }

    public boolean isOutdated() {
        return outdated;
    }

    @Override
    public Object getHandle() {
        throw new NMSException("You may not get the handle of a Registry Object.");
//        return outdated ? registryMap : registryMaterials;
    }

    @Override
    public NMSObject copy() {
        return new RegistryAdapter(outdated ? registryMap : registryMaterials);
    }
}
