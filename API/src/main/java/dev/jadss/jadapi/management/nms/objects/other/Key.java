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

public class Key implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    private String nameSpace;
    private String key;

    public static final Class<?> KEY = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "resources" : "server." + NMS.getNMSVersion()) + ".MinecraftKey");

    public Key() {
    }

    public Key(String nameSpace, String key) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This is not available in Version 1.7");

        this.nameSpace = nameSpace;
        this.key = key;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void parse(Object object) {
        if (object == null)
            return;
        if (!canParse(object))
            throw new NMSException("Cannot parse this object.");
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("Keys cannot be built in versions below 1.8.");

        String[] channelKeys = object.toString().split(":");
        if (channelKeys.length != 2)
            throw new NMSException("Not valid?");

        this.nameSpace = channelKeys[0];
        this.key = channelKeys[1];
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("Keys cannot be built in versions below 1.8.");
        return JConstructorReflector.executeConstructor(KEY, new Class[]{String.class}, this.nameSpace + ":" + this.key);
    }

    @Override
    public Class<?> getParsingClass() {
        return KEY;
    }

    @Override
    public boolean canParse(Object object) {
        return KEY.equals(object.getClass());
    }

    @Override
    public NMSObject copy() {
        return new Key(this.key, this.nameSpace);
    }

    @Override
    public String toString() {
        return "Key{" +
                "nameSpace='" + nameSpace + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
