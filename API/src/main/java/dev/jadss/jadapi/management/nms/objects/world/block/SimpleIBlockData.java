package dev.jadss.jadapi.management.nms.objects.world.block;

import dev.jadss.jadapi.management.nms.objects.world.block.state.StateType;

import java.util.List;
import java.util.Map;

public interface SimpleIBlockData extends SimpleBlock {

    /**
     * Get the states of this IBlockData!
     * @return the states!
     */
    Map<StateType<?>, Object> getStates();

    /**
     * Get which states are set in this IBlockData!
     * @return the states.
     */
    List<StateType<?>> getSetStates();

    /**
     * Get the state of a specific state type!
     * @param stateType the state type!
     * @return the current specified state!
     * @param <R> the type of state!
     */
    <R> R getState(StateType<R> stateType);

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
