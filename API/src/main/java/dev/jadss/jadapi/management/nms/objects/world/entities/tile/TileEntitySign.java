package dev.jadss.jadapi.management.nms.objects.world.entities.tile;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.BukkitAsUtility;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.world.block.IBlockData;
import dev.jadss.jadapi.management.nms.objects.world.entities.EntityPlayer;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.management.nms.packets.OutTileEntityData;
import dev.jadss.jadapi.management.nms.packets.OutUpdateSign;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.UUID;

public class TileEntitySign extends TileEntity {

    public static final Class<?> tileEntitySignClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.entity" : "server." + JReflection.getNMSVersion()) + ".TileEntitySign");

    public TileEntitySign(Object nmsTileEntity) {
        super(nmsTileEntity);

        if (!isTileEntitySignObject(nmsTileEntity))
            throw new NMSException("This is not an NMS TileEntitySign!");
    }

    public TileEntitySign(BlockPosition position, IBlockData blockData) {
        super(JReflection.executeConstructor(tileEntitySignClass,
                (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? new Class[]{BlockPosition.blockPositionClass, IBlockData.iBlockDataClass} : new Class[]{}),
                (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? new Object[]{position.build(), blockData.build()} : new Object[]{})));
    }

    @Override
    public DefinedPacket getUpdatePacket(BlockPosition position) {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
            return new OutUpdateSign(position, this.getLines());
        } else {
            return new OutTileEntityData(this, position);
        }
    }

    public IChatBaseComponent[] getLines() {
        boolean isNewer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8);
        Class<?> clazz = isNewer ? Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass() : String[].class;

        return Arrays
                .stream(((Object[]) JReflection.getFieldObject(tileEntitySignClass, clazz, tileEntity)))
                .map(object -> {
                    if (isNewer) {
                        IChatBaseComponent component = new IChatBaseComponent();
                        component.parse(object);
                        return component;
                    } else {
                        String component = (String) object;
                        return new IChatBaseComponent(component, false, "");
                    }
                }).toArray(IChatBaseComponent[]::new);
    }

    public void setLines(IChatBaseComponent[] lines) {
        boolean isNewer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8);
        Class<?> clazz = isNewer ? Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass() : String[].class;
        Object[] array = (Object[]) JReflection.getFieldObject(tileEntitySignClass, clazz, tileEntity);
        assert array != null && array.length == 4;
        if (isNewer) {
            for (int i = 0; i < array.length; i++)
                array[i] = lines[i].build();
        } else {
            for (int i = 0; i < array.length; i++)
                array[i] = lines[i].getMessage();
        }
    }

    public boolean isTileEntitySignObject(Object object) {
        return tileEntitySignClass.isAssignableFrom(object.getClass());
    }

    //Custom methods.

    public void setEditable(boolean editable) {
        JReflection.setFieldObject(tileEntitySignClass, boolean.class, tileEntity, editable);
    }

    public boolean isEditable() {
        return JReflection.getFieldObject(tileEntitySignClass, boolean.class, tileEntity);
    }

    public void setEditingHuman(EntityPlayer player) {
        boolean newer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17);
        JReflection.setFieldObject(tileEntitySignClass, (newer ? UUID.class : EntityPlayer.entityHumanClass), tileEntity, (newer ? player.getBukkitEntity().getUniqueId() : player.getHandle()));
    }

    @BukkitAsUtility
    public EntityPlayer getEditingHuman() {
        boolean newer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17);
        Object object;
        if (newer)
            object = JReflection.getFieldObject(tileEntitySignClass, UUID.class, tileEntity);
        else
            object = JReflection.getFieldObject(tileEntitySignClass, EntityPlayer.entityHumanClass, tileEntity);

        if (newer) {
            Entity entity = Bukkit.getServer().getEntity((UUID) object);
            return (EntityPlayer) NMS.getEntity(entity);
        } else {
            return new EntityPlayer(object);
        }
    }
}