package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.world.WorldServer;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Array;
import java.util.Arrays;

public class OutUpdateSign extends DefinedPacket {

    public static final Class<?> updateSignClass = JClassReflector.getClass("net.minecraft.server." + NMS.getNMSVersion() + ".PacketPlayOutUpdateSign");

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

        this.lines = new IChatBaseComponent[] { new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent(), new IChatBaseComponent() };

        if (JVersion.getServerVersion() == JVersion.v1_8) {
            this.position = new BlockPosition();
            this.position.parse(JFieldReflector.getObjectFromUnspecificField(updateSignClass, BlockPosition.blockPositionClass, packet));

            Object[] lines = (Object[]) JFieldReflector.getObjectFromUnspecificField(updateSignClass, Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass(), packet);

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
            int x = JFieldReflector.getObjectFromUnspecificField(updateSignClass, int.class, (i) -> i - 2, packet);
            int y = JFieldReflector.getObjectFromUnspecificField(updateSignClass, int.class, (i) -> i - 1, packet);
            int z = JFieldReflector.getObjectFromUnspecificField(updateSignClass, int.class, (i) -> i - 0, packet);
            this.position = new BlockPosition(x, y, z);

            String[] lines = JFieldReflector.getObjectFromUnspecificField(updateSignClass, String[].class, packet);

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
        switch (JVersion.getServerVersion()) {
            case v1_7: {
                return JConstructorReflector.executeConstructor(updateSignClass,
                        new Class[]{int.class, int.class, int.class, String[].class},
                        position.getX(), position.getY(), position.getZ(), Arrays.stream(lines).map(IChatBaseComponent::getMessage).toArray(String[]::new));
            }
            case v1_8: {
                Object[] array = (Object[]) Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 4);
                for (int i = 0; i < 4; i++)
                    array[i] = lines[i].build();
                return JConstructorReflector.executeConstructor(updateSignClass,
                        new Class[]{WorldServer.worldClass, BlockPosition.blockPositionClass, Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass()},
                        null, position.build(), array);
            }
            default: {
                throw new NMSException("Cannot build this packet on new versions!");
            }
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

    @Override
    public String toString() {
        return "OutUpdateSign{" +
                "position=" + position +
                ", lines=" + Arrays.toString(lines) +
                '}';
    }
}
