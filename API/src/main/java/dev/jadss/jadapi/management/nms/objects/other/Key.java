package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.utils.JReflection;

public class Key implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    private String nameSpace;
    private String key;

    public static final Class<?> keyClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "resources" : "server." + JReflection.getNMSVersion()) + ".MinecraftKey");

    public Key() { }
    public Key(String nameSpace, String key) {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) throw new NMSException("This is not available in Version 1.7");

        this.nameSpace = nameSpace;
        this.key = key;
    }

    public String getNameSpace() { return nameSpace; }
    public void setNameSpace(String nameSpace) { this.nameSpace = nameSpace; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    @Override
    public void parse(Object object) {
        if(object == null) return;
        if(!canParse(object)) throw new NMSException("Cannot parse this object.");

        String[] channelKeys = object.toString().split(":");
        if(channelKeys.length != 2) throw new NMSException("Not valid?");

        this.nameSpace = channelKeys[0];
        this.key = channelKeys[1];
    }

    @Override
    public Object build() { return JReflection.executeConstructor(keyClass, new Class[] { String.class }, this.key + ":" + this.nameSpace); }

    @Override
    public Class<?> getParsingClass() { return keyClass; }

    @Override
    public boolean canParse(Object object) { return keyClass.equals(object.getClass()); }

    @Override
    public NMSObject copy() { return new Key(this.key, this.nameSpace); }
}
