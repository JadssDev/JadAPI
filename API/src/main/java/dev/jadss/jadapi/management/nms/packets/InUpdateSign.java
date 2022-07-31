package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Array;
import java.util.Arrays;

public class InUpdateSign extends DefinedPacket {

    public static final Class<?> updateSignPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayInUpdateSign");

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
            int x = JFieldReflector.getObjectFromUnspecificField(updateSignPacketClass, int.class, (i) -> 0, packet);
            int y = JFieldReflector.getObjectFromUnspecificField(updateSignPacketClass, int.class, (i) -> 1, packet);
            int z = JFieldReflector.getObjectFromUnspecificField(updateSignPacketClass, int.class, (i) -> 2, packet);
            this.position = new BlockPosition(x, y, z);
        } else {
            this.position = new BlockPosition();
            this.position.parse(JFieldReflector.getObjectFromUnspecificField(updateSignPacketClass, BlockPosition.blockPositionClass, packet));
        }

        if (JVersion.getServerVersion() == JVersion.v1_8) {
            //hey look I found a reason why I hate 1.8 now! literally a weirdo version! :joy:
            this.lines = new IChatBaseComponent[]{new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent()};

            Object[] array = (Object[]) JFieldReflector.getObjectFromUnspecificField(updateSignPacketClass, Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass(), packet);
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

            String[] array = JFieldReflector.getObjectFromUnspecificField(updateSignPacketClass, String[].class, packet);
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
            packet = JConstructorReflector.executeConstructor(updateSignPacketClass, new Class[]{BlockPosition.blockPositionClass, String.class, String.class, String.class, String.class},
                    position.build(), lines[0].getMessage(), lines[1].getMessage(), lines[2].getMessage(), lines[3].getMessage());
        } else {
            packet = JConstructorReflector.executeConstructor(updateSignPacketClass, new Class[]{});

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) {
                JFieldReflector.setObjectToUnspecificField(updateSignPacketClass, int.class, (i) -> 0, packet, position.getX());
                JFieldReflector.setObjectToUnspecificField(updateSignPacketClass, int.class, (i) -> 1, packet, position.getY());
                JFieldReflector.setObjectToUnspecificField(updateSignPacketClass, int.class, (i) -> 2, packet, position.getZ());
            } else {
                JFieldReflector.setObjectToUnspecificField(updateSignPacketClass, BlockPosition.blockPositionClass, packet, position.build());
            }

            if (JVersion.getServerVersion() == JVersion.v1_8) {
                Object arrayObject = Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 4);
                for (int i = 0; i < 4; i++)
                    Array.set(arrayObject, i, lines[i].build());

                JFieldReflector.setObjectToUnspecificField(updateSignPacketClass, arrayObject.getClass(), packet, arrayObject);
            } else {
                JFieldReflector.setObjectToUnspecificField(updateSignPacketClass, String[].class, packet, Arrays.stream(lines).map(IChatBaseComponent::getMessage).toArray(String[]::new));
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

    @Override
    public String toString() {
        return "InUpdateSign{" +
                "position=" + position +
                ", lines=" + Arrays.toString(lines) +
                '}';
    }
}
