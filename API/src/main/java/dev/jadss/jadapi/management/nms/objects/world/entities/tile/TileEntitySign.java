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
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public class TileEntitySign extends TileEntity {

    public static final Class<?> TILE_ENTITY_SIGN = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.level.block.entity" : "server." + NMS.getNMSVersion()) + ".TileEntitySign");

    public TileEntitySign(Object entity) {
        super(entity);

        if (!isTileEntitySignObject(entity))
            throw new NMSException("This is not an NMS TileEntitySign!");
    }

    public TileEntitySign(BlockPosition position, IBlockData blockData) {
        super(JConstructorReflector.executeConstructor(TILE_ENTITY_SIGN,
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
        boolean isChatComponent = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8);
        Class<?> clazz = isChatComponent ? Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass() : String[].class;

        return Arrays.stream(((Object[]) JFieldReflector.getObjectFromUnspecificField(TILE_ENTITY_SIGN, clazz, tileEntity)))
                .map(object -> {
                    if (isChatComponent) {
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
        boolean isChatComponent = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8);
        Class<?> clazz = isChatComponent ? Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass() : String[].class;

        Object[] messagesArray = (Object[]) JFieldReflector.getObjectFromUnspecificField(TILE_ENTITY_SIGN, clazz, (i) -> 0, tileEntity);
        assert messagesArray != null && messagesArray.length == 4;

        Consumer<Object[]> function = (array) -> {
            if (isChatComponent) {
                for (int i = 0; i < array.length; i++)
                    array[i] = lines[i].build();
            } else {
                for (int i = 0; i < array.length; i++)
                    array[i] = lines[i].getMessage();
            }
        };

        function.accept(messagesArray);
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            Object[] filteredArray = (Object[]) JFieldReflector.getObjectFromUnspecificField(TILE_ENTITY_SIGN, clazz, (i) -> 1, tileEntity);
            assert filteredArray != null && filteredArray.length == 4;
            function.accept(filteredArray);
        }
    }

    public boolean isTileEntitySignObject(Object object) {
        return TILE_ENTITY_SIGN.isAssignableFrom(object.getClass());
    }

    //Custom methods.

    public void setEditable(boolean editable) {
        JFieldReflector.setObjectToUnspecificField(TILE_ENTITY_SIGN, boolean.class, tileEntity, editable);
    }

    public boolean isEditable() {
        return JFieldReflector.getObjectFromUnspecificField(TILE_ENTITY_SIGN, boolean.class, tileEntity);
    }

    public void setEditingHuman(EntityPlayer player) {
        boolean newer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17);
        JFieldReflector.setObjectToUnspecificField(TILE_ENTITY_SIGN, (newer ? UUID.class : EntityPlayer.ENTITY_HUMAN), tileEntity, (newer ? player.getBukkitEntity().getUniqueId() : player.getHandle()));
    }

    @BukkitAsUtility
    public EntityPlayer getEditingHuman() {
        boolean newer = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17);
        Class<?> clazz = newer ? UUID.class : EntityPlayer.ENTITY_PLAYER;

        Object object = JFieldReflector.getObjectFromUnspecificField(TILE_ENTITY_SIGN, clazz, this.tileEntity);

        if (newer) {
            Entity entity = Bukkit.getServer().getEntity((UUID) object);
            return (EntityPlayer) NMS.getEntity(entity);
        } else {
            return new EntityPlayer(object);
        }
    }
}