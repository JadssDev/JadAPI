package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.utils.JReflection;

import java.util.UUID;

public class GameProfile implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    public static final Class<?> gameProfileClass = (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8) ? com.mojang.authlib.GameProfile.class : net.minecraft.util.com.mojang.authlib.GameProfile.class);

    private String name;
    private UUID id;

    public GameProfile() { }
    public GameProfile(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Override
    public Object build() {
        return JReflection.executeConstructor(gameProfileClass, new Class[] { UUID.class, String.class }, this.id, this.name);
    }

    @Override
    public void parse(Object object) {
        if(object == null) return;
        if(!canParse(object)) throw new NMSException("Cannot parse this object.");

        this.name = (String) JReflection.executeMethod(gameProfileClass, "getName", object, new Class[] { });
        this.id = (UUID) JReflection.executeMethod(gameProfileClass, "getId", object, new Class[] { });
    }

    @Override
    public Class<?> getParsingClass() { return gameProfileClass; }

    @Override
    public boolean canParse(Object object) { return object.getClass().equals(gameProfileClass); }

    @Override
    public NMSObject copy() { return new GameProfile(this.name, this.id); }
}
