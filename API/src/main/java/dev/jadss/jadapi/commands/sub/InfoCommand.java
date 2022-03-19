package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.JHead;
import dev.jadss.jadapi.bukkitImpl.item.JInventory;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.misc.JSkin;
import dev.jadss.jadapi.management.JQuickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand {

    public InfoCommand(CommandSender sender) {
        if(sender instanceof Player) {
            JPlayer player = new JPlayer((Player) sender);
            JInventory inventory = new JInventory(3, "&3&lServer &b&lInformation").fill(new JItemStack(JMaterial.getRegistryMaterials().find("CYAN_STAINED_GLASS_PANE")).setDisplayName("&3"));

            long totalPacketsSentToPlayers = -1L;
            long totalPacketsReceivedByPlayers = -1L;

            for(Long l : JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().values()) totalPacketsSentToPlayers = totalPacketsSentToPlayers+l;
            for(Long l : JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().values()) totalPacketsReceivedByPlayers = totalPacketsReceivedByPlayers+l;

            inventory.setItem(13, new JItemStack(JMaterial.getRegistryMaterials().find("NETHER_STAR")).setDisplayName("&3&lServer")
                    .setLore("&eUptime &m->&3 " + JadAPI.getInstance().getInformationManager().getUptime() + "&b&ls",
                            "&ePlayers &m->&3 " + JadAPI.getInstance().getServer().getOnlinePlayers().size(),
                            "&eTotal Packets &bsent&e &m->&3 " + totalPacketsSentToPlayers,
                            "&eTotal Packets &breceived &m->&3 " + totalPacketsReceivedByPlayers));

            List<String> eventsLore = new ArrayList<>();
            eventsLore.add("&bCalled &3" + JadAPI.getInstance().getInformationManager().getEventsCalledTotal() + " &b&lEvents&e!");
            eventsLore.add("");
            eventsLore.add("&eList");
            eventsLore.add("");
            for(String s : JadAPI.getInstance().getInformationManager().getEventsCalled().keySet())
                eventsLore.add(s + " -> " + JadAPI.getInstance().getInformationManager().getEventsCalled().get(s));
            inventory.setItem(13-9, new JItemStack(JMaterial.getRegistryMaterials().find("CHEST")).setDisplayName("&3&lServer &bEvents")
                    .setLore(eventsLore));

            List<String> lore = new ArrayList<>();
            lore.add("&ePackets sent to you &m->&3 " + JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().get(((Player) sender).getUniqueId()));
            lore.add("&ePackets received from you &m->&3 " + JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().get(((Player) sender).getUniqueId()));
            lore.add("&eVersion you're playing &m->&3 " + player.getPlayerVersion());

            JSkin skin = JSkin.getSkin(sender.getName());

            inventory.setItem(13+9, (skin != null ? new JHead(skin.getTextureBase64()).buildHead() : new JItemStack(JMaterial.getRegistryMaterials().find("PLAYER_HEAD"))).setDisplayName("&bAbout &3&lYou").setLore(lore));

            String ID1 = JQuickEvent.generateID();
            String ID2 = JQuickEvent.generateID();

            new JQuickEvent(JadAPI.getInstance().getJadPlugin(), InventoryClickEvent.class, e -> {
                if(e.getWhoClicked().getUniqueId().equals(((Player) sender).getUniqueId())) e.setCancelled(true);
            }, EventPriority.NORMAL, -1, -1, ID1).register(true);

            BukkitTask task = Bukkit.getScheduler().runTaskTimer(JadAPI.getInstance(), () -> {
                long packetsSent = -1L;
                long packetsReceived = -1L;

                for(Long l : JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().values()) packetsSent = packetsSent+l;
                for(Long l : JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().values()) packetsReceived = packetsReceived+l;

                inventory.setItem(13, new JItemStack(JMaterial.getRegistryMaterials().find("NETHER_STAR")).setDisplayName("&3&lServer")
                        .setLore("&eUptime &m->&3 " + JadAPI.getInstance().getInformationManager().getUptime() + "&b&ls",
                                "&ePlayers &m->&3 " + JadAPI.getInstance().getServer().getOnlinePlayers().size(),
                                "&eTotal Packets &bsent&e &m->&3 " + packetsSent,
                                "&eTotal Packets &breceived &m->&3 " + packetsReceived));

                List<String> eventsLore2 = new ArrayList<>();
                eventsLore2.add("&bCalled &3" + JadAPI.getInstance().getInformationManager().getEventsCalledTotal() + " &b&lEvents&e!");
                eventsLore2.add("");
                eventsLore2.add("&3List &e->");
                eventsLore2.add("");
                for(String s : JadAPI.getInstance().getInformationManager().getEventsCalled().keySet())
                    eventsLore2.add("&3" + s + " &e&m->&b " + JadAPI.getInstance().getInformationManager().getEventsCalled().get(s));
                inventory.setItem(13-9, new JItemStack(JMaterial.getRegistryMaterials().find("CHEST")).setDisplayName("&3&lServer &bEvents").setLore(eventsLore2));

                List<String> loreNew = new ArrayList<>();
                loreNew.add("&ePackets sent to you &m->&3 " + JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().get(((Player) sender).getUniqueId()));
                loreNew.add("&ePackets received from you &m->&3 " + JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().get(((Player) sender).getUniqueId()));
                loreNew.add("&eVersion you're playing &m->&3 " + player.getPlayerVersion());
                loreNew.add(" ");
                loreNew.add("&3&lMods");

                JSkin skin_v2 = JSkin.getSkin(sender.getName());

                inventory.setItem(13+9, (skin_v2 != null ? new JHead(skin_v2.getTextureBase64()).buildHead() : new JItemStack(JMaterial.getRegistryMaterials().find("PLAYER_HEAD"))).setDisplayName("&bAbout &3&lYou").setLore(lore));
            }, 5L, 5L);

            new JQuickEvent(JadAPI.getInstance().getJadPlugin(), InventoryCloseEvent.class, e -> {
                if(e.getPlayer().getUniqueId().equals(((Player) sender).getUniqueId())) {
                    JQuickEvent.getQuickEvent(ID1).register(false);
                    JQuickEvent.getQuickEvent(ID2).register(false);
                    task.cancel();
                }
            }, EventPriority.NORMAL, -1, -1, ID2).register(true);

            new JPlayer((Player) sender).openInventory(inventory);
        }
    }
}
