package dev.jadss.jadapi.management.nms.objects.world.block;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.CraftUtils;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;

public class Block implements NMSObject, NMSManipulable, SimpleBlock {

    public static final Class<?> blockClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block" : "server." + NMS.getNMSVersion()) + ".Block");

    private final Object block;
    private final byte data;

    public Block(Object block) {
        if (blockClass.isAssignableFrom(block.getClass())) {
            this.block = block;
            this.data = -1;
        } else throw new NMSException("Block is not a valid Block");
    }

    public Block(Object block, byte data) {
        if (blockClass.isAssignableFrom(block.getClass())) {
            this.block = block;
            this.data = data;
        } else throw new NMSException("Block is not a valid Block");
    }

    public IBlockData createBlockData() {
        return new IBlockData(this);
    }

    public Object getNMSBlockData() {
        if (isInvalid())
            throw new NMSException("Block is invalid");

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            return JMethodReflector.executeUnspecificMethod(blockClass, new Class[]{}, IBlockData.iBlockDataClass, block, null);
        } else { //Note: \/ this uses IBlockData fromLegacyData(int); method //Note: For some reason this stupid method just keeps getting annoying so I just use it's name instead.
            return JMethodReflector.executeMethod(blockClass, "fromLegacyData", block, new Object[]{data});
        }
    }

    public byte toLegacyData(Object nmsBlockData) {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) || JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            return 0;
        } else {
            return ((Integer) JMethodReflector.executeMethod(Block.blockClass, "toLegacyData", this.block, new Object[]{nmsBlockData})).byteValue();
        }
    }

    public Block createNewWithData(byte data) {
        return new Block(this.block, data);
    }

    public Block createNewWithData(Object nmsBlockData) {
        return new Block(this.block, toLegacyData(nmsBlockData));
    }

    public JMaterial getMaterial() {
        if (this.isInvalid())
            return JMaterial.getRegistryMaterials().find(CraftUtils.getMaterial(this.block));
        else
            return JMaterial.getRegistryMaterials().find(CraftUtils.getMaterial(this.block), this.data);
    }

    public boolean isInvalid() {
        return this.data == -1;
    }

    @Override
    public Object getHandle() {
        return block;
    }

    public int getBlockData() {
        return data;
    }

    @Override
    public String toString() {
        return "Block{" +
                "block=" + this.getMaterial() +
                ", data=" + data +
                '}';
    }
}
