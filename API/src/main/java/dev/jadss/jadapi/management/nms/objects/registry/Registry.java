package dev.jadss.jadapi.management.nms.objects.registry;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.objects.other.Key;
import dev.jadss.jadapi.management.nms.objects.other.ResourceKey;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntity;
import dev.jadss.jadapi.management.nms.objects.world.entities.types.EntityTypesInstance;
import dev.jadss.jadapi.utils.reflection.JMappings;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.util.Map;

public class Registry implements NMSObject, NMSManipulable, NMSCopyable {

    public static final Class<?> I_REGISTRY = JClassReflector.getClass("net.minecraft. " + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + NMS.getNMSVersion()) + ".IRegistry");

    //1.7 to 1.12
    public static final Class<?> REGISTRY_SIMPLE = JClassReflector.getClass("net.minecraft.server." + JVersion.getServerVersion() + ".RegistrySimple");

    //1.14 to 1.19
    public static final Class<?> WRITABLE_REGISTRY = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + NMS.getNMSVersion()) + ".IRegistryWritable");

    //All versions
    public static final Class<?> REGISTRY_MATERIALS = JClassReflector.getClass("net.minecraft." + ((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) ? "core" : "server." + NMS.getNMSVersion()) + ".RegistryMaterials");
    public static final Class<?> REGISTRY_BLOCKS = JClassReflector.getClass("net.minecraft." + ((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) ? "core" : "server." + NMS.getNMSVersion()) + ".RegistryBlocks");

    private final RegistryAdapter adapter;

    public Registry(RegistryAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public NMSObject copy() {
        return new Registry((RegistryAdapter) adapter.copy());
    }

    public Object get(Key key) {
        if (adapter.isOutdated()) {
            return adapter.get(key.getKey());
        } else {
            return adapter.get(key.build());
        }
    }

    public void set(Key key, Object value) {
        if (adapter.isOutdated()) {
            adapter.set(key.getKey(), value);
        } else {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_16))
                throw new NMSException("Cannot use this method due to version changes, please use Registry#set(ResourceKey, Object) instead!");
            adapter.set(key.build(), value);
        }
    }

    public void set(ResourceKey key, Object value) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15))
            throw new NMSException("Cannot use this method due to not being the correct version, please use Registry#set(Key, Object) instead!");

        adapter.set(key.build(), value);
    }

    public RegistryAdapter getAdapter() {
        return adapter;
    }

    @Override
    public Object getHandle() {
        return adapter.getHandle();
    }

    //Statics.

    public static Registry getRegistry(Type type) {
        return new Registry(type.getter.apply(null));
    }

    public static Registry getRegistry(RegistryGetter getter) {
        return new Registry(getter.apply(null));
    }

    public static final JMappings TILE_ENTITY_REGISTRY = JMappings.create(Registry.I_REGISTRY)
            .add(JVersion.v1_13, "BLOCK_ENTITY_TYPE")
            .add(JVersion.v1_16, "p")
            .add(JVersion.v1_18, "n")
            .finish();

    public static final JMappings ENTITY_REGISTRY = JMappings.create(Registry.I_REGISTRY)
            .add(JVersion.v1_13, "ENTITY_TYPE")
            .add(JVersion.v1_16, "l")
            .add(JVersion.v1_18, "j")
            .finish();

    public enum Type {
        TILE_ENTITY((ignore) -> {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
                //IRegistry class. (MinecraftKey, TileEntityTypes)
                return new RegistryAdapter(JFieldReflector.getObjectFromField(Registry.I_REGISTRY, TILE_ENTITY_REGISTRY.get(), null));
            } else if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_11)) {
                //Semi-newer IRegistry class. (String, TileEntity class)
                return new RegistryAdapter(JFieldReflector.getObjectFromUnspecificField(TileEntity.TILE_ENTITY, Registry.REGISTRY_MATERIALS, null));
            } else {
                //Legacy.
                return new RegistryAdapter((Map<String, Object>) JFieldReflector.getObjectFromUnspecificField(TileEntity.TILE_ENTITY, Map.class, null));
            }
        }),

        ENTITY((ignore) -> {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
                //IRegistry class. (MinecraftKey, EntityTypes)
                return new RegistryAdapter(JFieldReflector.getObjectFromField(Registry.I_REGISTRY, ENTITY_REGISTRY.get(), null));
            } else if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_11)) {
                //Semi-newer IRegistry class. (String, Entity class)
                return new RegistryAdapter(JFieldReflector.getObjectFromUnspecificField(EntityTypesInstance.ENTITY_TYPE, Registry.REGISTRY_MATERIALS, null));
            } else {
                //Legacy.
                return new RegistryAdapter((Map<String, Object>) JFieldReflector.getObjectFromUnspecificField(EntityTypesInstance.ENTITY_TYPE, Map.class, null));
            }
        });

        private final RegistryGetter getter;

        Type(RegistryGetter getter) {
            this.getter = getter;
        }
    }
}
