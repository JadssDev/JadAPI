package dev.jadss.jadapi.management.nms.objects.world.block.state.impl;

import dev.jadss.jadapi.management.nms.objects.world.block.IBlockState;
import dev.jadss.jadapi.management.nms.objects.world.block.state.IntegerBlockState;
import dev.jadss.jadapi.management.nms.objects.world.block.state.StateType;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

public class IntegerState implements IntegerBlockState {

    private final String id;
    private final int minValue;
    private final int maxValue;
    private final IBlockState blockState;

    private final boolean isSupported;

    public IntegerState(String nmsName, int minValue, int maxValue) {
        this.id = nmsName;
        this.minValue = minValue;
        this.maxValue = maxValue;

        if (StateType.isStatesSupported()) {
            Object object = JMethodReflector.executeUnspecificMethod(IntegerBlockState.blockStateIntegerClass, new Class[]{String.class, int.class, int.class}, IntegerBlockState.blockStateIntegerClass, null, new Object[]{nmsName, minValue, maxValue});
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
    public int getMinimum() {
        return minValue;
    }

    @Override
    public int getMaximum() {
        return maxValue;
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
