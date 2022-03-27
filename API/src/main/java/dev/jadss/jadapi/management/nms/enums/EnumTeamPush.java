package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.JReflection;

public enum EnumTeamPush implements NMSEnum {
    ALWAYS,
    NEVER,
    PUSH_OTHER_TEAMS,
    PUSH_OWN_TEAM;

    public static final Class<?> enumTeamPushClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.scores" : "server." + JReflection.getNMSVersion()) + ".ScoreboardTeamBase$EnumTeamPush");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8))
            throw new NMSException("EnumTeamPush is not supported in versions below 1.8");
        return JReflection.getEnum(this.ordinal(), enumTeamPushClass);
    }

    public String getNetworkId() {
        return JReflection.getFieldObject(enumTeamPushClass, String.class, getNMSObject());
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
