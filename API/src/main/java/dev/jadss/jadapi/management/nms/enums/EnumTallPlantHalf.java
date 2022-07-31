package dev.jadss.jadapi.management.nms.enums;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JEnumReflector;

public enum EnumTallPlantHalf implements NMSEnum {
    UPPER,
    LOWER;

    public static final Class<?> enumTallPlantHalfClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state.properties" : "server." + NMS.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) ? "BlockPropertyDoubleBlockHalf" : "BlockTallPlant$EnumTallPlantHalf"));;

    @Override
    public Object getNMSObject() {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("EnumTallPlantHalf is not supported in versions below 1.7");
        return JEnumReflector.getEnum(this.ordinal(), enumTallPlantHalfClass);
    }

    @Override
    public Class<?> getNMSEnumClass() {
        return enumTallPlantHalfClass;
    }
}
