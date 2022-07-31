package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public enum EnumNameTagVisibility implements NMSEnum {
    ALWAYS,
    NEVER,
    HIDE_FOR_OTHER_TEAMS,
    HIDE_FOR_OWN_TEAM;

    public static final Class<?> enumNameTagVisibilityClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.scores" : "server." + NMS.getNMSVersion()) + ".ScoreboardTeamBase$EnumNameTagVisibility");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumNameTagVisibility is not supported in versions below 1.7");
        return JEnumReflector.getEnum(this.ordinal(), enumNameTagVisibilityClass);
    }

    public String getNetworkId() {
        return JFieldReflector.getObjectFromUnspecificField(enumNameTagVisibilityClass, String.class, getNMSObject());
    }

    public static EnumNameTagVisibility getByNetworkId(String id) {
        for(EnumNameTagVisibility e : values())
            if(e.getNetworkId().equals(id))
                return e;
        return null;
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumNameTagVisibilityClass;
    }
}
