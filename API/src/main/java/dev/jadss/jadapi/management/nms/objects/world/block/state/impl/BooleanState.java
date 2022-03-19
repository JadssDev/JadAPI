package dev.jadss.jadapi.management.nms.objects.world.block.state.impl;

import dev.jadss.jadapi.management.nms.objects.world.block.IBlockState;
import dev.jadss.jadapi.management.nms.objects.world.block.state.BooleanBlockState;
import dev.jadss.jadapi.management.nms.objects.world.block.state.StateType;
import dev.jadss.jadapi.utils.JReflection;

public class BooleanState implements BooleanBlockState {

    private final String id;
    private final boolean defaultValue;
    private final IBlockState blockState;

    private final boolean isSupported;

    public BooleanState(String nmsName, boolean defaultValue) {
        this.id = nmsName;
        this.defaultValue = defaultValue;

        if(StateType.isStatesSupported()) {
            Object object = JReflection.executeUnspecificMethod(BooleanBlockState.blockStateBooleanClass, new Class[] { String.class }, null, BooleanBlockState.blockStateBooleanClass, nmsName);
            if(object != null)
                this.blockState = new IBlockState(object);
            else
                this.blockState = null;
        } else {
            this.blockState = null;
        }
        this.isSupported = (blockState != null); ;
    }

    @Override
    public String getStateId() {
        return id;
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

    @Override
    public Boolean getDefaultValue() {
        return defaultValue;
    }
}
