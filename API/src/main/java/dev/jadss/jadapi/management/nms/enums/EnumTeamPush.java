package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public enum EnumTeamPush implements NMSEnum {
    ALWAYS,
    NEVER,
    PUSH_OTHER_TEAMS,
    PUSH_OWN_TEAM;

    public static final Class<?> enumTeamPushClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.scores" : "server." + NMS.getNMSVersion()) + ".ScoreboardTeamBase$EnumTeamPush");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8))
            throw new NMSException("EnumTeamPush is not supported in versions below 1.8");
        return JEnumReflector.getEnum(this.ordinal(), enumTeamPushClass);
    }

    public String getNetworkId() {
        return JFieldReflector.getObjectFromUnspecificField(enumTeamPushClass, String.class, getNMSObject());
    }

    public static EnumTeamPush getByNetworkId(String id) {
        for(EnumTeamPush push : values())
            if(push.getNetworkId().equals(id))
                return push;
        return null;
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumTeamPushClass;
    }
}
