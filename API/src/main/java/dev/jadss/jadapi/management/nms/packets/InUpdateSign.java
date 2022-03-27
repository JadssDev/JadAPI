package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Array;
import java.util.Arrays;

public class InUpdateSign extends DefinedPacket {

    public static final Class<?> updateSignPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayInUpdateSign");

    private BlockPosition position;
    private IChatBaseComponent[] lines;

    public InUpdateSign() {
    }

    public InUpdateSign(BlockPosition position, IChatBaseComponent[] lines) {
        this.position = position;
        this.lines = lines;
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

        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
            int x = JReflection.getFieldObject(updateSignPacketClass, int.class, packet, (i) -> 0);
            int y = JReflection.getFieldObject(updateSignPacketClass, int.class, packet, (i) -> 1);
            int z = JReflection.getFieldObject(updateSignPacketClass, int.class, packet, (i) -> 2);
            this.position = new BlockPosition(x, y, z);
        } else {
            this.position = new BlockPosition();
            this.position.parse(JReflection.getFieldObject(updateSignPacketClass, BlockPosition.blockPositionClass, packet));
        }

        if (JVersion.getServerVersion() == JVersion.v1_8) {
            //hey look I found a reason why I hate 1.8 now! literally a weirdo version! :joy:
            this.lines = new IChatBaseComponent[]{new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent()};

            Object[] array = (Object[]) JReflection.getFieldObject(updateSignPacketClass, Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass(), packet);
            try {
                for (int i = 0; i < 4; i++)
                    this.lines[i].parse(array[i]);
            } catch (ArrayIndexOutOfBoundsException ex) {
                if (JadAPI.getInstance().getDebug().doMiscDebug()) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eReached &3final &eof &b&lArray&e! &b&lCatched &3OutOfBoundsException&e!!"));
                    ex.printStackTrace();
                }
            }
        } else {
            this.lines = new IChatBaseComponent[]{new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent()};

            String[] array = JReflection.getFieldObject(updateSignPacketClass, String[].class, packet);
            try {
                for (int i = 0; i < 4; i++)
                    this.lines[i].setMessage(array[i]);
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
        Object packet;
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17)) {
            packet = JReflection.executeConstructor(updateSignPacketClass, new Class[]{BlockPosition.blockPositionClass, String.class, String.class, String.class, String.class},
                    position.build(), lines[0].getMessage(), lines[1].getMessage(), lines[2].getMessage(), lines[3].getMessage());
        } else {
            packet = JReflection.executeConstructor(updateSignPacketClass, new Class[]{});

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                JReflection.setFieldObject(updateSignPacketClass, int.class, packet, position.getX(), (i) -> 0);
                JReflection.setFieldObject(updateSignPacketClass, int.class, packet, position.getY(), (i) -> 1);
                JReflection.setFieldObject(updateSignPacketClass, int.class, packet, position.getZ(), (i) -> 2);
            } else {
                JReflection.setFieldObject(updateSignPacketClass, BlockPosition.blockPositionClass, packet, position.build());
            }

            if (JVersion.getServerVersion() == JVersion.v1_8) {
                JReflection.setFieldObject(updateSignPacketClass, Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass(), packet, Arrays.stream(lines).map(IChatBaseComponent::build).toArray(Object[]::new));
            } else {
                JReflection.setFieldObject(updateSignPacketClass, String[].class, packet, Arrays.stream(lines).map(IChatBaseComponent::getMessage).toArray(String[]::new));
            }
        }
        return packet;
    }

    @Override
    public boolean canParse(Object packet) {
        return updateSignPacketClass.equals(packet.getClass());
    }

    @Override
    public Class<?> getParsingClass() {
        return updateSignPacketClass;
    }

    @Override
    public DefinedPacket copy() {
        return new InUpdateSign((BlockPosition) this.position.copy(), Arrays.stream(this.lines).map(IChatBaseComponent::copy).toArray(IChatBaseComponent[]::new));
    }
}
