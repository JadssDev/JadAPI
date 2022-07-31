package dev.jadss.jadapi.commands.sub;


import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand {

    public InfoCommand(CommandSender sender) {
        if (sender instanceof Player) {
            JadAPI.getInstance().getInfoMenu().openMenu(new JPlayer((Player) sender));
        } else {
            new JSender(sender).sendMessage("&3&lJadAPI &7>> &eYou cannot &3execute &ethis!");
        }
    }
//        if(sender instanceof Player) {
//            JPlayer player = new JPlayer((Player) sender);
//            JInventory inventory = new JInventory(3, "&3&lServer &b&lInformation").fill(new JItemStack(JMaterial.getRegistryMaterials().find("CYAN_STAINED_GLASS_PANE")).setDisplayName("&3"));
//
//            //TODO: change info command to be more reliable!
//
//            long totalPacketsSentToPlayers = -1L;
//            long totalPacketsReceivedByPlayers = -1L;
//
//            for(Long l : JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().values()) totalPacketsSentToPlayers = totalPacketsSentToPlayers+l;
//            for(Long l : JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().values()) totalPacketsReceivedByPlayers = totalPacketsReceivedByPlayers+l;
//
//            inventory.setItem(13, new JItemStack(JMaterial.getRegistryMaterials().find("NETHER_STAR")).setDisplayName("&3&lServer")
//                    .setLore("&eUptime &m->&3 " + JadAPI.getInstance().getInformationManager().getUptime() + "&b&ls",
//                            "&ePlayers &m->&3 " + JadAPI.getInstance().getServer().getOnlinePlayers().size(),
//                            "&eTotal Packets &bsent&e &m->&3 " + totalPacketsSentToPlayers,
//                            "&eTotal Packets &breceived &m->&3 " + totalPacketsReceivedByPlayers));
//
//            List<String> eventsLore = new ArrayList<>();
//            eventsLore.add("&bCalled &3" + JadAPI.getInstance().getInformationManager().getEventsCalledTotal() + " &b&lEvents&e!");
//            eventsLore.add("");
//            eventsLore.add("&eList");
//            eventsLore.add("");
//            for(String s : JadAPI.getInstance().getInformationManager().getEventsCalled().keySet())
//                eventsLore.add(s + " -> " + JadAPI.getInstance().getInformationManager().getEventsCalled().get(s));
//            inventory.setItem(13-9, new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.CHEST)).setDisplayName("&3&lServer &bEvents")
//                    .setLore(eventsLore));
//
//            List<String> lore = new ArrayList<>();
//            lore.add("&ePackets sent to you &m->&3 " + JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().get(((Player) sender).getUniqueId()));
//            lore.add("&ePackets received from you &m->&3 " + JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().get(((Player) sender).getUniqueId()));
//            lore.add("&eVersion you're playing &m->&3 " + player.getPlayerVersion());
//
//            JSkin skin = JSkin.getSkin(sender.getName());
//
//            inventory.setItem(13+9, (skin != null ? new JHead(skin.getTextureBase64()).buildHead() : new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.PLAYER_HEAD))).setDisplayName("&bAbout &3&lYou").setLore(lore));
//
//            BukkitTask task = Bukkit.getScheduler().runTaskTimer(JadAPI.getInstance(), () -> {
//                long packetsSent = -1L;
//                long packetsReceived = -1L;
//
//                for(Long l : JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().values()) packetsSent = packetsSent+l;
//                for(Long l : JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().values()) packetsReceived = packetsReceived+l;
//
//                inventory.setItem(13, new JItemStack(JMaterial.getRegistryMaterials().find("NETHER_STAR")).setDisplayName("&3&lServer")
//                        .setLore("&eUptime &m->&3 " + JadAPI.getInstance().getInformationManager().getUptime() + "&b&ls",
//                                "&ePlayers &m->&3 " + JadAPI.getInstance().getServer().getOnlinePlayers().size(),
//                                "&eTotal Packets &bsent&e &m->&3 " + packetsSent,
//                                "&eTotal Packets &breceived &m->&3 " + packetsReceived));
//
//                List<String> eventsLore2 = new ArrayList<>();
//                eventsLore2.add("&bCalled &3" + JadAPI.getInstance().getInformationManager().getEventsCalledTotal() + " &b&lEvents&e!");
//                eventsLore2.add("");
//                eventsLore2.add("&3List &e->");
//                eventsLore2.add("");
//                for(String s : JadAPI.getInstance().getInformationManager().getEventsCalled().keySet())
//                    eventsLore2.add("&3" + s + " &e&m->&b " + JadAPI.getInstance().getInformationManager().getEventsCalled().get(s));
//                inventory.setItem(13-9, new JItemStack(JMaterial.getRegistryMaterials().find("CHEST")).setDisplayName("&3&lServer &bEvents").setLore(eventsLore2));
//
//                List<String> loreNew = new ArrayList<>();
//                loreNew.add("&ePackets sent to you &m->&3 " + JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().get(((Player) sender).getUniqueId()));
//                loreNew.add("&ePackets received from you &m->&3 " + JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().get(((Player) sender).getUniqueId()));
//                loreNew.add("&eVersion you're playing &m->&3 " + player.getPlayerVersion());
//                loreNew.add(" ");
//                loreNew.add("&3&lMods");
//
//                JSkin skin_v2 = JSkin.getSkin(sender.getName());
//
//                inventory.setItem(13+9, (skin_v2 != null ? new JHead(skin_v2.getTextureBase64()).buildHead() : new JItemStack(JMaterial.getRegistryMaterials().find("PLAYER_HEAD"))).setDisplayName("&bAbout &3&lYou").setLore(lore));
//            }, 5L, 5L);
//
//            String ID1 = JQuickEvent.generateID();
//            String ID2 = JQuickEvent.generateID();
//            String ID3 = JQuickEvent.generateID();
//
//            new JPlayer((Player) sender).openInventory(inventory);
//
//            new JQuickEvent<>(JadAPI.getInstance().getJadPluginInstance(), InventoryClickEvent.class, EventPriority.NORMAL, event -> {
//                event.setCancelled(true);
//            }, -1, -1, e -> e.getWhoClicked().getUniqueId().equals(player.getPlayer().getUniqueId()), ID1).register(true);
//
//            new JQuickEvent<>(JadAPI.getInstance().getJadPluginInstance(), InventoryCloseEvent.class, EventPriority.NORMAL, event -> {
//                JQuickEvent.getQuickEvent(ID1).register(false);
//                JQuickEvent.getQuickEvent(ID2).register(false);
//                JQuickEvent.getQuickEvent(ID3).register(false);
//                task.cancel();
//            }, -1, -1, e -> e.getPlayer().getUniqueId().equals(player.getPlayer().getUniqueId()), ID2).register(true);
//
//            new JQuickEvent<>(JadAPI.getInstance().getJadPluginInstance(), PlayerQuitEvent.class, EventPriority.LOWEST, event -> {
//                JQuickEvent.getQuickEvent(ID1).register(false);
//                JQuickEvent.getQuickEvent(ID2).register(false);
//                JQuickEvent.getQuickEvent(ID3).register(false);
//                task.cancel();
//            }, -1, -1, e -> e.getPlayer().getUniqueId().equals(player.getPlayer().getUniqueId()), ID3).register(true);
//        }
//    }
}
