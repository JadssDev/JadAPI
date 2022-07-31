package dev.jadss.jadapi.commands.sub.menus;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.item.ItemNBT;
import dev.jadss.jadapi.bukkitImpl.item.JInventory;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.menu.AbstractMenu;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.ClickContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.CloseContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.MainContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.OpenContext;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class InfoMenu extends AbstractMenu<InfoMenu, JInventory, JItemStack> {

    public InfoMenu() {
        super(JadAPI.getInstance().getJadPluginInstance(), new JInventory(3, "&3&lServer &b&lInformation")
                .fill(new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.CYAN_STAINED_GLASS_PANE))
                        .setDisplayName(" ")
                        .setLore(new ArrayList<>())
                        .addFlags(ItemFlag.values()))
                .setItem(8, createCookieItem(0))
                .setItem(13, new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.COMPASS))
                        .setDisplayName("&cLoading...")
                        .setLore("&eThis menu is still being &b&lloaded&e!",
                                 "&aPlease wait!"))
                .setItem(26, new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.REDSTONE_TORCH))
                        .setDisplayName("&c&lNote")
                        .setLore("&eThis menu &bauto-updates &eevery",
                                 "&a2 seconds&e!")));
    }

    private BukkitTask task;

    @Override
    protected void onRegister() {
        this.task = Bukkit.getScheduler().runTaskTimer(JadAPI.getInstance(), () -> {

            //Items
            int serverEventsIndex = 11;
            JItemStack serverEvents = new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.CHEST))
                    .addFlags(ItemFlag.values())
                    .getNBT().setBoolean("Server_Events_Item", true)
                    .getItem()
                    .setDisplayName("&3&lEvent &binformation");

            List<String> lore = new ArrayList<>();
            lore.add("&dClick on this &3&litem &dto");
            lore.add("&dprint in &bchat&d.");
            lore.add(" ");
            lore.add("&eA total of " + JadAPI.getInstance().getInformationManager().getEventsCalledTotal() + " Events were called.");
            lore.add(" ");

            for (Map.Entry<String, Long> entries : JadAPI.getInstance().getInformationManager().getEventsCalled().entrySet())
                lore.add("&3" + entries.getKey() + " &a&m->&b " + entries.getValue());

            serverEvents.setLore(lore);

            int pluginInfoIndex = 13;
            JItemStack pluginInfo = new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.COMMAND_BLOCK))
                    .addFlags(ItemFlag.values())
                    .setDisplayName("&3&lJadAPI &bInformation")
                    .setLore("&3Version &a&m->&b " + JadAPI.getInstance().getDescription().getVersion(),
                             "&0Idk, Give me ideas, please.");

            int serverDataIndex = 15;
            JItemStack serverData = new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.NETHER_STAR))
                    .addFlags(ItemFlag.values())
                    .setDisplayName("&3&lServer &bInformation")
                    .setLore("&3Uptime &a&m->&b " + JadAPI.getInstance().getInformationManager().getUptime() + "&b&ls",
                             "&3Players &a&m->&b " + "&c&lDisabled",
                             "&3Total Packets &dsent &a&m->&b" + JadAPI.getInstance().getInformationManager().getPacketsSentToPlayers().values().stream()
                                     .mapToLong(Long::longValue)
                                     .sum(),
                             "&3Total Packets &dreceived &a&m->&b " + JadAPI.getInstance().getInformationManager().getPacketsReceivedByPlayers().values().stream()
                                     .mapToLong(Long::longValue)
                                     .sum()
                    );


            for (MainContext<JInventory, JItemStack> context : this.getContexts()) {
                //Remove loading item.
                context.getInventory().setItem(13, new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.CYAN_STAINED_GLASS_PANE))
                        .setDisplayName(" ")
                        .setLore(new ArrayList<>())
                        .addFlags(ItemFlag.values()));

                //Add remaining items needed.
                context.getInventory().setItem(serverEventsIndex, serverEvents.copy());

                context.getInventory().setItem(pluginInfoIndex, pluginInfo.copy());

                context.getInventory().setItem(serverDataIndex, serverData.copy());
            }
        }, 40L, 40L);
}

    @Override
    protected void onUnregister() {
        this.task.cancel();
        this.task = null;
    }

    private static JItemStack createCookieItem(int cookies) {
        return new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.COOKIE))
                .addFlags(ItemFlag.values())
                .getNBT().setBoolean("Cookie_Simulator_Item", true)
                .getItem()
                .setDisplayName("&d&lCookie Simulator")
                .setLore("&eYou have &a" + cookies + " &dCookies&e!",
                         "&e",
                         "&eClick to &bacquire &ea &dcookie&e!");
    }

    @Override
    protected void onOpen(OpenContext<JInventory, JItemStack> context) {
        context.getPlayer().sendMessage("&3&lJadAPI &7>> &eYou have &b&lopened &ethe &3Info Menu&e!");
        context.getMainContext().getSavedContext().setVariable("cookies", 0);
    }

    private final Map<UUID, Long> cooldown = new HashMap<>();

    @Override
    protected void onClick(ClickContext<JInventory, JItemStack> context) {
        if (context.getEvent().getClickedInventory() == null)
            return;
        JItemStack item = context.getMainContext().getInventory().getItem(context.getEvent().getSlot());
        if (item == null || item.getType().getEnum() == JMaterial.MaterialEnum.AIR)
            return;

        ItemNBT<JItemStack> nbt = item.getNBT();
        //We have a readable item, I think.
        if (nbt.getBoolean("Server_Events_Item")) {
            if (!cooldown.containsKey(context.getPlayer().getPlayer().getUniqueId()) || cooldown.get(context.getPlayer().getPlayer().getUniqueId()) < System.currentTimeMillis()) {
                cooldown.put(context.getPlayer().getPlayer().getUniqueId(), System.currentTimeMillis() + 5000L);

                context.getPlayer().sendMessage("&3&lJadAPI &7>> &eA total of &3" + JadAPI.getInstance().getInformationManager().getEventsCalledTotal() + " &b&lEvents &ewere &acalled&e!");
                context.getPlayer().sendMessage("&aList:");
                Map<String, Long> events = new HashMap<>(JadAPI.getInstance().getInformationManager().getEventsCalled());
                for (Map.Entry<String, Long> entries : events.entrySet())
                    context.getPlayer().sendMessage("&3" + entries.getKey() + " &a&m->&b " + entries.getValue());

                context.getPlayer().sendMessage("&3&lJadAPI &7>> &eThis &3functionality &ehas a &b5 second delay&e!");
            } else {
                context.getPlayer().sendMessage("&3&lJadAPI &7>> &ePlease &3calm down&e!");
            }
        } else if (nbt.getBoolean("Cookie_Simulator_Item")) {
            context.getMainContext().getSavedContext().setVariable("cookies", (int) context.getMainContext().getSavedContext().getVariable("cookies") + 1);
            context.getMainContext().getInventory().setItem(8, createCookieItem((int) context.getMainContext().getSavedContext().getVariable("cookies")));
            context.getPlayer().sendMessage("&3&lJadAPI &7>> &eYou've &agained &ea &d&lCookie&e!");
        }
    }

    @Override
    protected void onClose(CloseContext<JInventory, JItemStack> context) {
        context.getPlayer().sendMessage("&3&lJadAPI &7>> &eYou have &b&lclosed &ethe &3Info Menu&e!");
    }

    @Override
    protected boolean cancelEveryClick() {
        return true;
    }

    @Override
    protected boolean reuseInventory() {
        return false;
    }
}
