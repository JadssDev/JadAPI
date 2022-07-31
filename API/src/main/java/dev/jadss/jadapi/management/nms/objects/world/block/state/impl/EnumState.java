package dev.jadss.jadapi.management.nms.objects.world.block.state.impl;

import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.objects.world.block.IBlockState;
import dev.jadss.jadapi.management.nms.objects.world.block.state.EnumBlockState;
import dev.jadss.jadapi.management.nms.objects.world.block.state.StateType;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

public class EnumState<N extends NMSEnum> implements EnumBlockState<N> {

    private final String id;
    private final Class<N> nmsEnumParsedClass;
    private final Class<?> nmsEnumClass;
    private final IBlockState blockState;

    private final boolean isSupported;

    public EnumState(String nmsName, Class<N> nmsParsedEnum, Class<?> nmsEnumClass) {
        this.id = nmsName;
        this.nmsEnumParsedClass = nmsParsedEnum;
        this.nmsEnumClass = nmsEnumClass;

        if (StateType.isStatesSupported()) {
            Object object = JMethodReflector.executeUnspecificMethod(EnumBlockState.blockStateEnumClass, new Class[]{String.class, Class.class}, EnumBlockState.blockStateEnumClass, null, new Object[]{nmsName, nmsEnumClass});
            if (object != null)
                this.blockState = new IBlockState(object);
            else
                this.blockState = null;
        } else {
            this.blockState = null;
        }
        this.isSupported = (blockState != null);
    }

    @Override
    public String getStateId() {
        return id;
    }

    @Override
    public Class<N> getParsedClass() {
        return nmsEnumParsedClass;
    }

    @Override
    public Object getNMSObject() {
        return (blockState != null) ? blockState.getHandle() : null;
    }

    @Override
    public IBlockState getNMSBlockState() {
        return blockState;
    }

    @Override
    public boolean isValid() {
        return isSupported;
    }
}
