package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

public enum EnumInstrument implements NMSEnum {
    HARP,
    BASEDRUM,
    SNARE,
    HAT,
    BASS,
    FLUTE,
    BELL,
    GUITAR,
    CHIME,
    XYLOPHONE,
    IRON_XYLOPHONE,
    COW_BELL,
    DIDGERIDOO,
    BIT,
    BANJO,
    PLING;

    public static final Class<?> enumInstrumentClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + ".BlockPropertyInstrument");

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12))
            throw new NMSException("EnumInstrument is not supported in versions below 1.12, due to how NMS handles note blocks' instrument");
        return JEnumReflector.getEnum(this.ordinal(), enumInstrumentClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumInstrumentClass;
    }
}
