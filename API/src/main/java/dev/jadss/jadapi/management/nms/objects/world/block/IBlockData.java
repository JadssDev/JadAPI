package dev.jadss.jadapi.management.nms.objects.world.block;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.management.nms.objects.world.block.state.StateList;
import dev.jadss.jadapi.management.nms.objects.world.block.state.StateType;
import dev.jadss.jadapi.utils.JReflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is basically IBlockState!
 */
public class IBlockData implements NMSObject, NMSParsable, NMSBuildable, NMSCopyable, SimpleIBlockData {

    //Present all versions.
    public static final Class<?> iBlockDataClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state" : "server." + JReflection.getNMSVersion()) + ".IBlockData");

    //Present +1.13
    public static final Class<?> iBlockDataHolderClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_18) ? "world.level.block.state" : "server." + JReflection.getNMSVersion()) + ".IBlockDataHolder");

    //Present +1.16
    public static final Class<?> blockDataClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state" : "server." + JReflection.getNMSVersion()) + ".BlockBase$BlockData");

    //the map class.
    public static final Class<?> immutableMapClass = JReflection.getReflectionClass("com.google.common.collect.ImmutableMap");

    private Block block;
    private Map<StateType<?>, Object> blockStates = new HashMap<>();

    public IBlockData() { }

    public IBlockData(Block block) {
        this.block = block;
    }

    public IBlockData(Block block, Map<StateType<?>, Object> blockStates) {
        this.block = block;
        this.blockStates = blockStates;
    }

    public Block getBlock() {
        return block;
    }
    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public Class<?> getParsingClass() { return iBlockDataClass; }

    @Override
    public void parse(Object object) {
        if(object == null) return;
        if(!canParse(object)) throw new NMSException("Cannot parse this object.");

        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            this.block = new Block(object, (byte) -1);
        } else if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            Block block = new Block(JReflection.executeMethod(iBlockDataClass, "getBlock", object, new Class[] { }), (byte) -1);
            this.block = block.createNewWithData(object);
        } else if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            this.block = new Block(JReflection.executeMethod(iBlockDataClass, "getBlock", object, new Class[] { }), (byte) 0);
        } else {
            this.block = new Block(JReflection.executeMethod(blockDataClass, "getBlock", object, new Class[] { }), (byte) 0);
        }

        Map<Object, Object> states;

        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
            states = (Map<Object, Object>) JReflection.executeUnspecificMethod(iBlockDataClass, new Class[] {}, object, immutableMapClass);
        } else {
            states = (Map<Object, Object>) JReflection.getUnspecificFieldObject(iBlockDataHolderClass, immutableMapClass, object);
        }

        Map<StateType<?>, Object> statesMap = new HashMap<>();
        for(Map.Entry<Object, Object> entry : states.entrySet()) {
            StateType<?> stateType = StateList.getStateTypeByName(new IBlockState(entry.getKey()).getId());
            if(stateType == null) continue;
            statesMap.put(stateType, entry.getValue());
        }

        this.blockStates = statesMap;
    }

    @Override
    public boolean canParse(Object object) {
        return iBlockDataClass.isAssignableFrom(object.getClass()) || (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) && object.getClass().isAssignableFrom(Block.blockClass));
    }

    @Override
    public Object build() {
        if(block == null) throw new NMSException("Block may not be null.");
        Object blockData = block.getNMSBlockData();

        //Apply block states...
        for(Map.Entry<StateType<?>, Object> entry : blockStates.entrySet()) {
            System.out.println(entry.getKey().getNMSObject().toString() + " -> " + entry.getValue());
            if(JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_16)) {
                blockData = JReflection.executeUnspecificMethod(iBlockDataHolderClass, new Class[] { IBlockState.iBlockStateClass, Comparable.class }, blockData, IBlockData.iBlockDataClass, entry.getKey().getNMSObject(), entry.getValue());
            } else {
                blockData = JReflection.executeUnspecificMethod(iBlockDataClass, new Class[] { IBlockState.iBlockStateClass, Comparable.class }, blockData, IBlockData.iBlockDataClass, entry.getKey().getNMSObject(), entry.getValue());
            }
        }

        return blockData;
    }

    @Override
    public NMSObject copy() {
        Map<StateType<?>, Object> states = new HashMap<>();
        this.blockStates.entrySet().parallelStream().forEach(entry -> states.put(entry.getKey(), entry.getValue()));
        return new IBlockData(block, states);
    }

    @Override
    public JMaterial getMaterial() {
        return block.getMaterial();
    }

    @Override
    public List<StateType<?>> getStates() {
        return new ArrayList<>(blockStates.keySet());
    }

    @Override
    public <R> void setState(StateType<R> type, R value) {
        blockStates.put(type, value);
    }

    @Override
    public void removeState(StateType<?> stateType) {
        blockStates.remove(stateType);
    }
}
