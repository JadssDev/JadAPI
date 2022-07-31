package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.util.UUID;

public class GameProfile implements NMSObject, NMSManipulable, NMSCopyable {

    public static final Class<?> gameProfileClass = JClassReflector.getClass(JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8) ? "com.mojang.authlib.GameProfile" : "net.minecraft.util.com.mojang.authlib.GameProfile");

    private final Object handle;

    public GameProfile(UUID id, String name) {
        handle = JConstructorReflector.executeConstructor(gameProfileClass, new Class[] { UUID.class, String.class }, id, name);
    }

    public GameProfile(Object handle) {
        if (gameProfileClass.equals(handle.getClass())) {
            this.handle = handle;
        } else {
            throw new IllegalArgumentException("Not a GameProfile!");
        }
    }

    public String getName() {
        return JFieldReflector.getObjectFromUnspecificField(gameProfileClass, String.class, handle);
    }

    public UUID getId() {
        return JFieldReflector.getObjectFromUnspecificField(gameProfileClass, UUID.class, handle);
    }

    @Override
    public Object getHandle() {
        return handle;
    }

    @Override
    public NMSObject copy() {
        return new GameProfile(this.getId(), this.getName());
    }

    @Override
    public String toString() {
        return "GameProfile{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                '}';
    }
}
