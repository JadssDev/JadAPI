package dev.jadss.jadapi.management.nms.objects.world.block;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.management.nms.CraftUtils;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.utils.JReflection;

public class Block implements NMSObject, NMSManipulable, SimpleBlock {

    public static final Class<?> blockClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block" : "server." + JReflection.getNMSVersion()) + ".Block");

    private final Object block;
    private final byte data;

    public Block(Object block) {
        if(blockClass.isAssignableFrom(block.getClass())) {
            this.block = block;
            this.data = -1;
        } else throw new NMSException("Block is not a valid Block");
    }

    public Block(Object block, byte data) {
        if(blockClass.isAssignableFrom(block.getClass())) {
            this.block = block;
            this.data = data;
        } else throw new NMSException("Block is not a valid Block");
    }

    public IBlockData createBlockData() {
        return new IBlockData(this);
    }

    public Object getNMSBlockData() {
        if(isInvalid())
            throw new NMSException("Block is invalid");

        if(JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            return JReflection.executeMethod(blockClass, new Class[] {}, block, IBlockData.iBlockDataClass, (i) -> 0);
        } else {
            return JReflection.executeMethod(blockClass, new Class[] { int.class }, block, IBlockData.iBlockDataClass, (i) -> 0, data);
        }
    }

    public byte toLegacyData(Object nmsBlockData) {
        if(JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13) || JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            return 0;
        } else {
            return JReflection.executeMethod(Block.blockClass, new Class[] { IBlockData.iBlockDataClass }, this.block, int.class, (i) -> 0, nmsBlockData).byteValue();
        }
    }

    public Block createNewWithData(byte data) {
        return new Block(this.block, data);
    }

    public Block createNewWithData(Object nmsBlockData) {
        return new Block(this.block, toLegacyData(nmsBlockData));
    }

    public JMaterial getMaterial() {
        if(this.isInvalid())
            return JMaterial.getRegistryMaterials().find(CraftUtils.getMaterial(this.block));
        else
            return JMaterial.getRegistryMaterials().find(CraftUtils.getMaterial(this.block), (byte) this.data);
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
}
