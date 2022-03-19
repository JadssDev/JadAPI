package dev.jadss.jadapi.management.nms.objects.world.block;

import dev.jadss.jadapi.management.nms.objects.world.block.state.StateType;

import java.util.List;

public interface SimpleIBlockData extends SimpleBlock {

    /**
     * Get which types are in this IBlockData!
     * @return the states.
     */
    List<StateType<?>> getStates();

    /**
     * Please note you can set any state you want, but you should note it will throw an error if some state cannot be put into specific Block!
     * @param type the state to set.
     * @param value the value to set the state to according to the state type!
     * @param <R> the type of value the state accepts!
     */
    <R> void setState(StateType<R> type, R value);

    /**
     * Remove a state from the states list!
     * @param stateType the state type!
     */
    void removeState(StateType<?> stateType);
}
