package dev.jadss.jadapi.management.nms.objects.registry;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.objects.other.Key;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntity;
import dev.jadss.jadapi.management.nms.objects.world.entities.types.EntityTypesInstance;
import dev.jadss.jadapi.utils.JReflection;

import java.util.Map;

public class Registry implements NMSObject, NMSManipulable, NMSCopyable {

    public static final Class<?> I_REGISTRY = JReflection.getReflectionClass("net.minecraft. " + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + JReflection.getNMSVersion()) + ".IRegistry");
    //1.7 to 1.12
    public static final Class<?> REGISTRY_SIMPLE = JReflection.getReflectionClass("net.minecraft.server." + JVersion.getServerVersion() + ".RegistrySimple");
    //All versions
    public static final Class<?> REGISTRY_MATERIALS = JReflection.getReflectionClass("net.minecraft." + ((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) ? "core" : "server." + JReflection.getNMSVersion()) + ".RegistryMaterials");
    public static final Class<?> REGISTRY_BLOCKS = JReflection.getReflectionClass("net.minecraft." + ((JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) ? "core" : "server." + JReflection.getNMSVersion()) + ".RegistryBlocks");

    private final RegistryAdapter adapter;

    public Registry(RegistryAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public NMSObject copy() {
        return new Registry((RegistryAdapter) adapter.copy());
    }

    public Object get(Key key) {
        if (adapter.isOutdated())
            return adapter.get(key.getKey());
        else
            return adapter.get(key);
    }

    public void set(Key key, Object value) {
        if (adapter.isOutdated())
            adapter.set(key.getKey(), value);
        else
            adapter.set(key, value);
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

    public enum Type {
        MAIN((ignore) -> {
            //This gets the list of the adapters in 1.13 (1.14 and above don't have this)
            return new RegistryAdapter(JReflection.getFieldObject(Registry.I_REGISTRY, Registry.I_REGISTRY, null, (i) -> 0));
        }),

        TILE_ENTITY((ignore) -> {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
                //IRegistry class. (MinecraftKey, TileEntityTypes)
                Registry registry = getRegistry(Type.MAIN);
                Object object = registry.get(new Key("minecraft", "block_entity_type"));
                return new RegistryAdapter(object);
            } else if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_11)) {
                //Semi-newer IRegistry class. (String, TileEntity class)
                return new RegistryAdapter(JReflection.getFieldObject(TileEntity.tileEntityClass, Registry.REGISTRY_MATERIALS, null, (i) -> i));
            } else {
                //Legacy.
                return new RegistryAdapter(JReflection.getFieldObject(TileEntity.tileEntityClass, Map.class, null, (i) -> 0));
            }
        }),

        ENTITY((ignore) -> {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
                //IRegistry class. (MinecraftKey, EntityTypes)
                Registry registry = getRegistry(Type.MAIN);
                Object object = registry.get(new Key("minecraft", "entity_type"));
                return new RegistryAdapter(object);
            } else if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_11)) {
                //Semi-newer IRegistry class. (String, Entity class)
                return new RegistryAdapter(JReflection.getFieldObject(EntityTypesInstance.ENTITY_TYPE, Registry.REGISTRY_MATERIALS, null, (i) -> i));
            } else {
                //Legacy.
                return new RegistryAdapter(JReflection.getFieldObject(EntityTypesInstance.ENTITY_TYPE, Map.class, null, (i) -> 0));
            }
        });

        private final RegistryGetter getter;

        Type(RegistryGetter getter) {
            this.getter = getter;
        }
    }
}
