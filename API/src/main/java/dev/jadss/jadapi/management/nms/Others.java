package dev.jadss.jadapi.management.nms;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.reflection.JMappings;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;

public class Others {

    public static final Class<?> NBT_TAG_COMPOUND_CLASS = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "nbt" : "server." + NMS.getNMSVersion()) + ".NBTTagCompound");

    public static final Class<?> PLAYER_CONNECTION_CLASS = JClassReflector.getClass("net.minecraft.server." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network" : NMS.getNMSVersion()) + ".PlayerConnection");

    public static JMappings NBT_SAVE_METHOD = JMappings.create(Others.NBT_TAG_COMPOUND_CLASS)
            .add(JVersion.v1_8, "b")
            .add(JVersion.v1_9, "save")
            .add(JVersion.v1_18, "m")
            .finish();
}
