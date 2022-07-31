package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

public enum EnumSculkSensorPhase implements NMSEnum{
    INACTIVE,
    ACTIVE,
    COOLDOWN;

    public static final Class<?> skulcSensorPhaseClass = JClassReflector.getClass("net.minecraft.world.level.block.state.properties.SculkSensorPhase");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16))
            throw new NMSException("SculkSensorPhase is not available in versions below 1.16");
        return JEnumReflector.getEnum(this.ordinal(), skulcSensorPhaseClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return skulcSensorPhaseClass;
    }
}
