package dev.jadss.jadapi.management.channel;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.sub.JSignRegister;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.management.nms.packets.InUpdateSign;
import dev.jadss.jadapi.management.nms.packets.OutBlockChangePacket;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.function.Consumer;

public final class SignChangeHandler {

    private SignChangeHandler() {
        //Utility class
    }

    public static boolean handleSignChange(InUpdateSign packet, JPlayer player) {
        if(JadAPI.getInstance().getSigns().containsKey(player.getPlayer().getUniqueId())) {
            JSignRegister register = JadAPI.getInstance().getSigns().get(player.getPlayer().getUniqueId());
            JadAPI.getInstance().getSigns().remove(player.getPlayer().getUniqueId());
            Location location = JFieldReflector.getObjectFromUnspecificField(JSignRegister.class, Location.class, register);
            BlockPosition blockPosition = new BlockPosition(location.getX(), location.getY(), location.getZ());

            player.sendPacket(new OutBlockChangePacket(NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.AIR)).createBlockData(), blockPosition).build());

            Consumer<String[]> consumer = register.getConsumer();

            try {
                consumer.accept(Arrays.stream(packet.getLines())
                        .map(IChatBaseComponent::getMessage)
                        .toArray(String[]::new));
            } catch(Exception ex) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&3&lJadAPI &7>> &eThis &3&lerror &eis &c&lNOT &efrom &3&lJadAPI&e, &b" + register.getPluginRegisterer().getJavaPlugin().getName() + " &3thrown this &c&lerror &eplease &b&lcontact &ethem! "));
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
                ex.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            }
            return true;
        } else {
            return false;
        }
    }
}
