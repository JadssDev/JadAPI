package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.world.WorldServer;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Array;
import java.util.Arrays;

public class OutUpdateSign extends DefinedPacket {

    public static final Class<?> updateSignClass = JReflection.getReflectionClass("net.minecraft.server." + JReflection.getNMSVersion() + ".PacketPlayOutUpdateSign");

    private BlockPosition position;
    private IChatBaseComponent[] lines;

    public OutUpdateSign(BlockPosition position, IChatBaseComponent[] lines) {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9))
            throw new NMSException("Unsupported in this version! Only available in 1.8 and below!");
        this.position = position;
        this.lines = lines;
    }

    public OutUpdateSign() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_9))
            throw new NMSException("Unsupported in this version! Only available in 1.8 and below!");
    }

    public BlockPosition getPosition() {
        return position;
    }

    public void setPosition(BlockPosition position) {
        this.position = position;
    }

    public IChatBaseComponent[] getLines() {
        return lines;
    }

    public void setLines(IChatBaseComponent[] lines) {
        this.lines = lines;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        this.lines = new IChatBaseComponent[4];
        for (int i = 0; i < 4; i++)
            lines[i] = new IChatBaseComponent();
        if (JVersion.getServerVersion() == JVersion.v1_8) {
            this.position = new BlockPosition();
            this.position.parse(JReflection.getFieldObject(updateSignClass, BlockPosition.blockPositionClass, packet));

            Object[] lines = (Object[]) JReflection.getFieldObject(updateSignClass, Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass(), packet);

            try {
                for (int i = 0; i < 4; i++)
                    this.lines[i].parse(lines[i]);
            } catch (ArrayIndexOutOfBoundsException ex) {
                if (JadAPI.getInstance().getDebug().doMiscDebug()) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eReached &3final &eof &b&lArray&e! &b&lCatched &3OutOfBoundsException&e!!"));
                    ex.printStackTrace();
                }
            }
        } else {
            int x = JReflection.getFieldObject(updateSignClass, int.class, packet, (i) -> 0);
            int y = JReflection.getFieldObject(updateSignClass, int.class, packet, (i) -> 1);
            int z = JReflection.getFieldObject(updateSignClass, int.class, packet, (i) -> 2);
            this.position = new BlockPosition(x, y, z);

            String[] lines = JReflection.getFieldObject(updateSignClass, String[].class, packet);

            try {
                for (int i = 0; i < 4; i++)
                    this.lines[i].setMessage(lines[i]);
            } catch (ArrayIndexOutOfBoundsException ex) {
                if (JadAPI.getInstance().getDebug().doMiscDebug()) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eReached &3final &eof &b&lArray&e! &b&lCatched &3OutOfBoundsException&e!!"));
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            return JReflection.executeConstructor(updateSignClass,
                    new Class[]{int.class, int.class, int.class, String[].class},
                    position.getX(), position.getY(), position.getZ(), Arrays.stream(lines).map(IChatBaseComponent::getMessage).toArray(String[]::new));
        } else {
            Object[] array = (Object[]) Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 4);
            for (int i = 0; i < 4; i++)
                array[i] = lines[i].build();
            return JReflection.executeConstructor(updateSignClass,
                    new Class[]{WorldServer.worldClass, BlockPosition.blockPositionClass, Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass()},
                    null, position.build(), array);
        }
    }

    @Override
    public boolean canParse(Object packet) {
        return updateSignClass.equals(packet.getClass());
    }

    @Override
    public Class<?> getParsingClass() {
        return updateSignClass;
    }

    @Override
    public DefinedPacket copy() {
        return new OutUpdateSign((BlockPosition) position.copy(), Arrays.stream(this.lines)
                .map(component -> (IChatBaseComponent) component.copy())
                .toArray(IChatBaseComponent[]::new));
    }
}
