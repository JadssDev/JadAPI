package dev.jadss.jadapi.management.nms.objects.world.block;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.enums.NMSEnum;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.management.nms.objects.world.block.state.EnumBlockState;
import dev.jadss.jadapi.management.nms.objects.world.block.state.StateList;
import dev.jadss.jadapi.management.nms.objects.world.block.state.StateType;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IBlockData implements NMSObject, NMSParsable, NMSBuildable, NMSCopyable, SimpleIBlockData {

    //Present all versions.
    public static final Class<?> iBlockDataClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state" : "server." + NMS.getNMSVersion()) + ".IBlockData");

    //Present +1.13
    public static final Class<?> iBlockDataHolderClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state" : "server." + NMS.getNMSVersion()) + ".IBlockDataHolder");

    //Present +1.16
    public static final Class<?> blockDataClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.state" : "server." + NMS.getNMSVersion()) + ".BlockBase$BlockData");

    //the map class.
    public static final Class<?> immutableMapClass = JClassReflector.getClass("com.google.common.collect.ImmutableMap");

    private Block block;
    private Map<StateType<?>, Object> blockStates = new HashMap<>();

    public IBlockData() {
    }

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
    public Class<?> getParsingClass() {
        return iBlockDataClass;
    }

    @Override
    public void parse(Object object) {
        if (object == null)
            return;
        if (!canParse(object))
            throw new NMSException("Cannot parse this object.");

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            this.block = new Block(object, (byte) -1);
        } else if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_15)) {
            Block block = new Block(JMethodReflector.executeMethod(iBlockDataClass, "getBlock", object, null),
                    (byte) (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12) ? -1 : 0));
            this.block = (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12) ? block.createNewWithData(object) : block);
        } else {
            this.block = new Block(JMethodReflector.executeUnspecificMethod(blockDataClass, new Class[]{}, Block.blockClass, object, null), (byte) 0);
        }

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_7)) {
            Map<Object, Object> states;

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_12)) {
                states = (Map<Object, Object>) JMethodReflector.executeUnspecificMethod(iBlockDataClass, new Class[]{}, immutableMapClass, object, null);
            } else {
                if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_16)) {
                    states = (Map<Object, Object>) JFieldReflector.getObjectFromUnspecificField(iBlockDataHolderClass, immutableMapClass, object);
                } else {
                    states = (Map<Object, Object>) JMethodReflector.executeUnspecificMethod(iBlockDataHolderClass, new Class[] {}, immutableMapClass, object, new Object[] {});
                }
            }

            Map<StateType<?>, Object> statesMap = new HashMap<>();
            if (JadAPI.getInstance().getDebug().doMiscDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eAttempting to find " + states.size() + " states..."));

            for (Map.Entry<Object, Object> entry : states.entrySet()) {
                String id = new IBlockState(entry.getKey()).getId();
                if (JadAPI.getInstance().getDebug().doMiscDebug())
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eAttempting to find &b&lstate &eby &aname &3" + id + "&e..."));
                StateType<?> stateType = StateList.getStateTypeByName(id);

                if (stateType == null) {
                    //Message removed because some states are not mapped in JadAPI.
//                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eA very &4critical error &ehas occurred, it has been &aignored &ebut please report this &b&lmessage&e! &aThank you&e!"));

                    if (JadAPI.getInstance().getDebug().doMiscDebug())
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &c&lState &eby &aname &3" + id + "&e not found."));

                    continue;
                }
                if (stateType.getType() == StateType.Type.BOOLEAN || stateType.getType() == StateType.Type.INTEGER) {
                    //No parsing needed.
                    statesMap.put(stateType, entry.getValue());
                } else if (stateType.getType() == StateType.Type.ENUM) {
                    statesMap.put(stateType, NMSEnum.getEnum(((EnumBlockState<? extends NMSEnum>) stateType).getParsedClass(), entry.getValue()));
                }
            }

            this.blockStates = statesMap;
        } else {
            this.blockStates = new HashMap<>();
        }
    }

    @Override
    public boolean canParse(Object object) {
        return iBlockDataClass.isAssignableFrom(object.getClass()) || (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7) && object.getClass().isAssignableFrom(Block.blockClass));
    }

    @Override
    public Object build() {
        if (block == null) throw new NMSException("Block may not be null.");
        Object blockData = block.getNMSBlockData();

        //Apply block states...
        for (Map.Entry<StateType<?>, Object> entry : blockStates.entrySet()) {
            if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
                blockData = JMethodReflector.executeUnspecificMethod(iBlockDataHolderClass, new Class[]{IBlockState.iBlockStateClass, Comparable.class}, Object.class, blockData, new Object[]{entry.getKey().getNMSObject(), entry.getValue()});
            } else {
                blockData = JMethodReflector.executeUnspecificMethod(iBlockDataClass, new Class[]{IBlockState.iBlockStateClass, Comparable.class}, IBlockData.iBlockDataClass, blockData, new Object[]{entry.getKey().getNMSObject(), entry.getValue()});
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
    public Map<StateType<?>, Object> getStates() {
        return new HashMap<>(blockStates);
    }

    @Override
    public List<StateType<?>> getSetStates() {
        return new ArrayList<>(blockStates.keySet());
    }

    @Override
    public <R> R getState(StateType<R> stateType) {
        return (R) blockStates.get(stateType);
    }

    @Override
    public <R> void setState(StateType<R> type, R value) {
        blockStates.put(type, value);
    }

    @Override
    public void removeState(StateType<?> stateType) {
        blockStates.remove(stateType);
    }

    @Override
    public String toString() {
        return "IBlockData{" +
                "block=" + block +
                ", blockStates=" + blockStates +
                '}';
    }
}
