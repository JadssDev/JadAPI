package dev.jadss.jadapi.commands.sub;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.misc.JHologram;
import dev.jadss.jadapi.bukkitImpl.misc.JSender;
import dev.jadss.jadapi.management.JPacketHook;
import dev.jadss.jadapi.management.nms.CraftUtils;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.enums.*;
import dev.jadss.jadapi.management.nms.objects.attributes.AttributeInstance;
import dev.jadss.jadapi.management.nms.objects.attributes.AttributeList;
import dev.jadss.jadapi.management.nms.objects.attributes.AttributeType;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.other.GameProfile;
import dev.jadss.jadapi.management.nms.objects.other.ItemStack;
import dev.jadss.jadapi.management.nms.objects.other.ObjectPackage;
import dev.jadss.jadapi.management.nms.objects.other.ScoreboardTeam;
import dev.jadss.jadapi.management.nms.objects.world.WorldServer;
import dev.jadss.jadapi.management.nms.objects.world.block.Block;
import dev.jadss.jadapi.management.nms.objects.world.block.IBlockData;
import dev.jadss.jadapi.management.nms.objects.world.block.state.StateList;
import dev.jadss.jadapi.management.nms.objects.world.entities.EntityArmorStand;
import dev.jadss.jadapi.management.nms.objects.world.entities.EntityPlayer;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.Entity;
import dev.jadss.jadapi.management.nms.objects.world.entities.base.EntityLiving;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntity;
import dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntitySign;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.management.nms.packets.*;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestCommand {

    private static final List<Test> list = new ArrayList<>();

    public TestCommand(CommandSender s, String[] args) {
        new JSender(s).ifPlayer(player -> {
            if (AllowCommand.allow) {
                if (player.getPlayer().getName().equalsIgnoreCase("jadss")) {
                    if (args.length == 0) {
                        //List
                        player.sendMessage("&3&lJadAPI &7>> &eCurrent Tests Created: ");
                        for (Test test : list)
                            player.sendMessage("&b&m->&a " + test.testName);
                    } else {
                        //Execute
                        String arg = args[0];
                        Test testIfFound = list.stream().filter(test -> test.testName.equals(arg)).findFirst().orElse(null);
                        if (testIfFound == null) {
                            player.sendMessage("&3&lJadAPI &7>> &eCouldn't find the Test you specified!");
                            return;
                        }
                        player.sendMessage("&3&lJadAPI &7>> &eFound Test, &aStarting &b" + testIfFound.testName + "&e!");
                        Exception exception = testIfFound.run(player);
                        player.sendMessage("&3&lJadAPI &7>> &eTest Finished " + (exception != null ? "with Exception." : "without any Exceptions."));
                        if (exception != null)
                            player.sendMessage("&3&lJadAPI &7>> &cError => " + exception.getMessage());
                    }
                } else {
                    player.sendMessage("&3&lJadAPI &7>> &cYour username is not correct&e?");
                }
            } else {
                player.sendMessage("&3&lJadAPI &7>> &eThe &bConsole &ehas &cnot allowed &ethis &3&lCommand &eto be &bused&e!!");
            }
        }, sender -> {
            sender.sendMessage("&3&lJadAPI &7>> &eYou cannot use this command!");
        });
    }

    static {
        //Bukkit tests
        list.add(new Test("Block-Check", (player) -> {
            player.sendMessage("&3&lJadAPI &7>> &eThe block below you is a " + player.getPlayer().getLocation().add(0, -1, 0).getBlock().getType() + ".");
        }));

        list.add(new Test("Class-Check", (player) -> {
            List<Method> fields = Arrays.asList(Entity.ENTITY_CLASS.getDeclaredMethods());
            player.sendMessage("&3&lJadAPI &7>> &eThe Class has " + fields.size() + " Methods!");
            for (String name : fields.stream().map(Method::getName).collect(Collectors.toCollection(ArrayList::new))) {
                player.sendMessage("&3&m->&a " + name);
            }
        }));

        //JadAPI Bukkit tests.

        AtomicReference<JHologram> lastHologram = new AtomicReference<>();

        list.add(new Test("Hologram-Testing", (player) -> {
            if (lastHologram.get() != null)
                lastHologram.get().delete();

            lastHologram.set(new JHologram(player.getPlayer().getLocation(), true, "&3Test", "&3&lHologram", "&a=)"));

            player.sendMessage("&3&lJadAPI &7>> &eCreated!");
        }));

        //Mostly NMS tests

        list.add(new Test("Enum-Testing", player -> {
            checkEnum(EnumAxis.values());
            checkEnum(EnumBedPart.values());
            checkEnum(EnumChatFormat.values());
            checkEnum(EnumColor.values());
            checkEnum(EnumComparatorMode.values());
            checkEnum(EnumDirection.values());
            checkEnum(EnumDoorHalf.values());
            checkEnum(EnumDoorHinge.values());
            checkEnum(EnumHand.values());
            checkEnum(EnumInstrument.values());
            checkEnum(EnumItemSlot.values());
            checkEnum(EnumNameTagVisibility.values());
            checkEnum(EnumProtocol.values());
            checkEnum(EnumRedstoneWireConnection.values());
            checkEnum(EnumSculkSensorPhase.values());
            checkEnum(EnumStairsHalf.values());
            checkEnum(EnumStairsShape.values());
            checkEnum(EnumTallPlantHalf.values());
            checkEnum(EnumTeamPush.values());
            checkEnum(EnumTilt.values());
            checkEnum(EnumTrackPosition.values());
            checkEnum(EnumTrapdoorHalf.values());
        }));

        list.add(new Test("Block-Creation-Testing", (player) -> {
            JSender console = new JSender(Bukkit.getConsoleSender());
            Block block = NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.OAK_LEAVES));
            IBlockData data = block.createBlockData();
            WorldServer world = NMS.toWorldServer(player.getPlayer().getWorld());
            BlockPosition position = new BlockPosition(player.getPlayer().getLocation().getBlockX(),
                    player.getPlayer().getLocation().getBlockY(), player.getPlayer().getLocation().getBlockZ());
            console.sendMessage("&3&lJadAPI &7>> &eAll Variables have been initiated!");


            world.setBlockData(position, data, true);
            console.sendMessage("&3&lJadAPI &7>> &eThe BlockData has been set!");

            console.sendMessage("&3&lJadAPI &7>> &eAcquiring BlockData...");
            IBlockData blockData = world.getBlockData(position);

            console.sendMessage("&3&lJadAPI &7>> &eAttempting to get States..");
            console.sendMessage("&aStates Captured &m=>&a " + blockData.getStates());
            console.sendMessage("&aStates at start &m=>&a " + data.getStates());


            console.sendMessage("&3&lJadAPI &7>> &eCreating a TNT with Custom Block States...");
            IBlockData tntData = NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.TNT)).createBlockData();
            tntData.setState(StateList.TNT_UNSTABLE, true);
            console.sendMessage("&aStates of TNT &m=>&a " + tntData.getStates());

            position.setX(position.getX() - 2);

            world.setBlockData(position, tntData, true);
            console.sendMessage("&3&lJadAPI &7>> &eThe TNT has been set!");

            console.sendMessage("&3&lJadAPI &7>> &eCreating a sign!");
            IBlockData signData = NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.OAK_WALL_SIGN)).createBlockData();
            console.sendMessage("&3&lJadAPI &7>> &eSign Created!");

            position.setX(position.getX() - 1);

            world.setBlockData(position, signData, false);

            console.sendMessage("&3&lJadAPI &7>> &eFinished Creating blocks!");

            console.sendMessage("&3&lJadAPI &7>> &eGetting sign..");
            TileEntity entity = world.getTileEntity(position);

            if (entity instanceof dev.jadss.jadapi.management.nms.objects.world.entities.tile.TileEntitySign) {
                console.sendMessage("&3&lJadAPI &7>> &eFound Sign!");
                Function<Void, IChatBaseComponent> emptyComponentGetter = (ignore) -> new IChatBaseComponent("", false, null);
                ((TileEntitySign) entity).setLines(new IChatBaseComponent[] { new IChatBaseComponent("|  HELLO  |", false, null),
                        emptyComponentGetter.apply(null), emptyComponentGetter.apply(null), emptyComponentGetter.apply(null)});
                console.sendMessage("&3&lJadAPI &7>> &eSign Edited!");
            } else {
                console.sendMessage("&3&lJadAPI &7>> &eDid not receive TileEntitySign??");
            }
        }));

        list.add(new Test("Entity-Testing", (player) -> {
            JSender console = new JSender(Bukkit.getConsoleSender());

            console.sendMessage("&3&lJadAPI &7>> &eCreating a &aZombie&e...");

            Zombie zombie = (Zombie) player.getPlayer().getLocation().getWorld().spawnEntity(player.getPlayer().getLocation(), EntityType.ZOMBIE);
            EntityLiving entity = (EntityLiving) NMS.getEntity(zombie);

            console.sendMessage("&3&lJadAPI &7>> &eZombie transferred to NMS API!");
            AttributeInstance instance;

            instance = entity.getAttribute(AttributeList.getAttribute(AttributeType.KNOCKBACK_RESISTANCE));
            instance.setValue(instance.getValue()*50);
            instance = entity.getAttribute(AttributeList.getAttribute(AttributeType.MOVEMENT_SPEED));
            instance.setValue(instance.getValue()*100);
            instance = entity.getAttribute(AttributeList.getAttribute(AttributeType.MAX_HEALTH));
            instance.setValue(instance.getValue()*100);
            console.sendMessage("&3&lJadAPI &7>> &eZombie &aModifiers &ehave been added!");

            entity.setInvisible(true);
            console.sendMessage("&3&lJadAPI &7>> &eZombie is now &b&lInvisible&e.");

            entity.setSlot(EnumItemSlot.HEAD, new ItemStack(CraftUtils.asItem(new JItemStack(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.DIAMOND_HELMET)).getBukkitItem())), true);
            console.sendMessage("&3&lJadAPI &7>> &eAdded a Helmet to Zombie!");

            entity.setCustomNameVisible(true);
            entity.setCustomName(new IChatBaseComponent(ChatColor.translateAlternateColorCodes('&', "&a&lZombie"), false, null));
            console.sendMessage("&3&lJadAPI &7>> &eFinished creating &bZombie&e!");
        }));

        AtomicReference<JPacketHook> stopCustomPayloads = new AtomicReference<>();

        list.add(new Test("Stop-Custom-Payloads", (player) -> {
            if (stopCustomPayloads.get() == null) {
                player.sendMessage("&3&lJadAPI &7>> &eRegistered &3Packet hook&e!");

                if (stopCustomPayloads.get() != null)
                    stopCustomPayloads.get().register(false);

                stopCustomPayloads.set(new JPacketHook(JadAPI.getInstance().getJadPluginInstance(), (event) -> {
                    event.setCancelled(true);

                    player.sendMessage("&3&lJadAPI &7>> &3Custom Payload packet &ehas been &c&lcancelled&e!");
                }, (e) -> true, -1, -1, Collections.singletonList(InCustomPayloadPacket.CUSTOM_PAYLOAD), null, JPacketHook.generateID()));
                stopCustomPayloads.get().register(true);
            } else {
                player.sendMessage("&3&lJadAPI &7>> &eUnregistered &3Packet Hook&e!");

                stopCustomPayloads.get().register(false);
                stopCustomPayloads.set(null);
            }
        }));

        AtomicReference<JPacketHook> chatChecker = new AtomicReference<>();

        list.add(new Test("Chat-Signing-Checker", (player) -> {
            if (chatChecker.get() == null) {
                player.sendMessage("&3&lJadAPI &7>> &eRegistered &3Packet hook&e, waiting for &b&lChat&e!");

                if (chatChecker.get() != null)
                    chatChecker.get().register(false);

                chatChecker.set(new JPacketHook(JadAPI.getInstance().getJadPluginInstance(), (event) -> {
                    InChatPacket packet = new InChatPacket();
                    packet.parse(event.getPacket());

                    byte[] signatureArray = JFieldReflector.getObjectFromUnspecificField(packet.getEncryptionOptions().getObject().getClass(), byte[].class, packet.getEncryptionOptions().getObject());

                    player.sendMessage("&3&lJadAPI &7>> &3Chat &ereceived is " + (signatureArray != null && signatureArray.length != 0 ? "&a&lsigned" : "&a&lunsigned") + "&e!");
                }, (e) -> true, -1, -1, Collections.singletonList(InChatPacket.CHAT_PACKET), null, JPacketHook.generateID()));
                chatChecker.get().register(true);
            } else {
                player.sendMessage("&3&lJadAPI &7>> &eUnregistered &3Packet Hook&e!");

                chatChecker.get().register(false);
                chatChecker.set(null);
            }
        }));

        list.add(new Test("In-Packet-Testing", (player) -> {
            JSender console = new JSender(Bukkit.getConsoleSender());

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting InChatPacket..");
                InChatPacket packet = new InChatPacket("Text", Instant.now(), null, true);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new InChatPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting InCustomPayloadPacket..");
                byte[] data = new byte[] { 0, 1, 41, 1, 1, 15, 1, 2, 4, 66, 7, 8, 3, 1, 2, 5, 67, 7, 3, 2, 63, 1, 12, 4, 26, 1, 2, 37, 67, 3, 3, 52, 6, 33 ,1 };
                InCustomPayloadPacket packet = new InCustomPayloadPacket("jadapi:test", data);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new InCustomPayloadPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting InHandshakePacket..");
                InHandshakePacket packet = new InHandshakePacket(47, "play.jadss.dev", 25565, EnumProtocol.LOGIN);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new InHandshakePacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting InInteractAtEntityPacket.. (&c&lDisabled&e)");
                InInteractAtEntityPacket packet = new InInteractAtEntityPacket();
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new InInteractAtEntityPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting InLoginStart..");
                InLoginStart packet = new InLoginStart(new GameProfile(new UUID(0L, 0L), "jadss"));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new InLoginStart();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting InUpdateSign..");
                InUpdateSign packet = new InUpdateSign(new BlockPosition(0D, 0D, 0D), new IChatBaseComponent[] {
                        new IChatBaseComponent("Attempt 1", false, null),
                        new IChatBaseComponent("Attempt 2", false, null),
                        new IChatBaseComponent("Attempt 3", false, null),
                        new IChatBaseComponent("Attempt 4", false, null)
                });
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new InUpdateSign();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }
        }));

        list.add(new Test("Out-Packet-Testing", (player) -> {
            JSender console = new JSender(Bukkit.getConsoleSender());

            { //Fixed in 1.8
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutBlockChangePacket..");
                OutBlockChangePacket packet = new OutBlockChangePacket(NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.STONE)).createBlockData(), new BlockPosition(15D, 15D, 15D));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutBlockChangePacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutChatPacket.. (1/3)");
                OutChatPacket packet = new OutChatPacket(OutChatPacket.Type.CHAT, new IChatBaseComponent("Text", false, null),
                        new OutChatPacket.ChatSender(new UUID(25L, 25L), null, null), Instant.now(), new ObjectPackage(null));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutChatPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutChatPacket.. (2/3)");
                OutChatPacket packet = new OutChatPacket(OutChatPacket.Type.SYSTEM, new IChatBaseComponent("Text", false, null),
                        new OutChatPacket.ChatSender(new UUID(15L, 15L), null, null), Instant.now(), new ObjectPackage(null));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutChatPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutChatPacket.. (3/3)");
                OutChatPacket packet = new OutChatPacket(OutChatPacket.Type.ACTION_BAR, new IChatBaseComponent("Text", false, null),
                        new OutChatPacket.ChatSender(new UUID(15L, 15L), null, null), Instant.now(), new ObjectPackage(null));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutChatPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutTitlePacket.. (1/5)");
                OutTitlePacket packet = new OutTitlePacket(OutTitlePacket.Type.TITLE, new IChatBaseComponent("Text", false, null), -1, -2, -3);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutTitlePacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutTitlePacket.. (2/5)");
                OutTitlePacket packet = new OutTitlePacket(OutTitlePacket.Type.SUBTITLE, new IChatBaseComponent("Text", false, null), -1, -2, -3);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutTitlePacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutTitlePacket.. (3/5)");
                OutTitlePacket packet = new OutTitlePacket(OutTitlePacket.Type.TIMES, new IChatBaseComponent("Text", false, null), 20, 40, 20);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutTitlePacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutTitlePacket.. (4/5)");
                OutTitlePacket packet = new OutTitlePacket(OutTitlePacket.Type.CLEAR, new IChatBaseComponent("Text", false, null), -1, -2, -3);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutTitlePacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutTitlePacket.. (5/5)");
                OutTitlePacket packet = new OutTitlePacket(OutTitlePacket.Type.RESET, new IChatBaseComponent("Text", false, null), -1, -2, -3);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutTitlePacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutCustomPayloadPacket..");
                byte[] data = new byte[] { 0, 1, 41, 1, 1, 15, 1, 2, 4, 66, 7, 8, 3, 1, 2, 5, 67, 7, 3, 2, 63, 1, 12, 4, 26, 1, 2, 37, 67, 3, 3, 52, 6, 33 ,1 };
                OutCustomPayloadPacket packet = new OutCustomPayloadPacket("jadapi:test", data);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutCustomPayloadPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { //
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutEntityDestroy..");
                OutEntityDestroy packet = new OutEntityDestroy(new int[] { 1, 2, 3, 4, 5, 12, 67, 32, 69});
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutEntityDestroy();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            { // Not working
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutEntityMetadata..");
                OutEntityMetadata packet = new OutEntityMetadata(69, new ObjectPackage(null));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutEntityMetadata();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutOpenSignEditor..");
                OutOpenSignEditor packet = new OutOpenSignEditor(new BlockPosition(15D, 15D, 15D));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutOpenSignEditor();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutPlayerHeaderFooter..");
                OutPlayerHeaderFooter packet = new OutPlayerHeaderFooter(new IChatBaseComponent("Header", false, null),
                        new IChatBaseComponent("Footer", false, null));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutPlayerHeaderFooter();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutScoreboardTeamPacket..");
                OutScoreboardTeamPacket packet = new OutScoreboardTeamPacket(
                        new ScoreboardTeam(new ObjectPackage(null), "JadAPI-Test", (Set<String>) Collections.singleton("TestAccount"),
                                new IChatBaseComponent("JadAPI", false, null), new IChatBaseComponent("Prefix", false, null),
                                new IChatBaseComponent("suffix", false, null),
                                EnumChatFormat.GREEN, true, true, EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS, EnumNameTagVisibility.NEVER, EnumTeamPush.NEVER),
                        OutScoreboardTeamPacket.TeamAction.TEAM_ADD
                );
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutScoreboardTeamPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            stupidPacketIHaveToChange: {
                {
                    console.sendMessage("&3&lJadAPI &7>> &eTesting OutSpawnEntityLiving..");
                    Location location = player.getPlayer().getLocation();
                    EntityArmorStand armorStand = new EntityArmorStand(NMS.toWorldServer(player.getPlayer().getWorld()),
                            new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

                    OutSpawnEntityLiving packet = new OutSpawnEntityLiving(armorStand);
                    console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                    console.sendMessage("&aResult &m=>&a " + packet);
                    Object built = packet.build();
                    console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                    packet = new OutSpawnEntityLiving();
                    console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                    try {
                        packet.parse(built);
                    } catch(Exception ex) {
                        console.sendMessage("&3&lJadAPI &7>> &eCouldn't finish &b&lparsing&e.");
                        ex.printStackTrace();
                        break stupidPacketIHaveToChange;
                    }
                    console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                    console.sendMessage("&aRebuild Result &m=>&a " + packet);
                }
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutSpawnNamedEntity..");
                OutSpawnNamedEntity packet = new OutSpawnNamedEntity((EntityPlayer) NMS.getEntity(player.getPlayer()));
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutSpawnNamedEntity();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutTileEntityData..");
                console.sendMessage("&3&lJadAPI &7>> &eCreating a Sign Block in Player Position...");

                Location location = player.getPlayer().getLocation();

                WorldServer world = NMS.toWorldServer(player.getPlayer().getWorld());
                BlockPosition position = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                Block block = NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.OAK_SIGN));
                IBlockData blockData = block.createBlockData();

                world.setBlockData(position, blockData, false);

                OutTileEntityData packet = new OutTileEntityData(world.getTileEntity(position), position);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutTileEntityData();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutUpdateHealth..");
                OutUpdateHealth packet = new OutUpdateHealth(15F, 2, 21F);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutUpdateHealth();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_8)) {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutUpdateSign..");
                OutUpdateSign packet = new OutUpdateSign(new BlockPosition(15D, 15D, 15D), new IChatBaseComponent[] {
                        new IChatBaseComponent("e", false, null),
                        new IChatBaseComponent("b", false, null),
                        new IChatBaseComponent("c", false, null),
                        new IChatBaseComponent("ABC", false, null)
                });
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutUpdateSign();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            } else {
                console.sendMessage("&3&lJadAPI &7>> &eCannot test &3OutUpdateSign &esince it's &b&ldiscontinued &ein future versions!");
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutWorldBorderPacket.. (1/6)");
                OutWorldBorderPacket packet = new OutWorldBorderPacket(OutWorldBorderPacket.WorldBorderAction.INITIALIZE, OutWorldBorderPacket.DEFAULT_PORTAL_TELEPORT_BOUNDARY,
                        69D, 11D, 32D, 45D, 1000L, 75, 120);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutWorldBorderPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutWorldBorderPacket.. (2/6)");
                OutWorldBorderPacket packet = new OutWorldBorderPacket(OutWorldBorderPacket.WorldBorderAction.SET_CENTER, -1,
                        69D, 11D, -2D, -3D, -4, -5, -6);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutWorldBorderPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutWorldBorderPacket.. (3/6)");
                OutWorldBorderPacket packet = new OutWorldBorderPacket(OutWorldBorderPacket.WorldBorderAction.SET_SIZE, -1,
                        -2D, -4D, -5D, 200, -6, -7, -8);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutWorldBorderPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutWorldBorderPacket.. (4/6)");
                OutWorldBorderPacket packet = new OutWorldBorderPacket(OutWorldBorderPacket.WorldBorderAction.LERP_SIZE, -1,
                        -2D, -3D, -4D, 200, -5, -6, -7);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutWorldBorderPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutWorldBorderPacket.. (5/6)");
                OutWorldBorderPacket packet = new OutWorldBorderPacket(OutWorldBorderPacket.WorldBorderAction.SET_WARNING_TIME, -1,
                        -2D, -3D, -4D, -5D, -6, 6969, -8);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutWorldBorderPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }

            {
                console.sendMessage("&3&lJadAPI &7>> &eTesting OutWorldBorderPacket.. (6/6)");
                OutWorldBorderPacket packet = new OutWorldBorderPacket(OutWorldBorderPacket.WorldBorderAction.SET_WARNING_BLOCKS, -1,
                        -2D, -3D, -4D, -5D, -6, -7, 123456);
                console.sendMessage("&3&lJadAPI &7>> &eCreated &bPacket&e.");
                console.sendMessage("&aResult &m=>&a " + packet);
                Object built = packet.build();
                console.sendMessage("&3&lJadAPI &7>> &aBuilt &bPacket&e!");
                packet = new OutWorldBorderPacket();
                console.sendMessage("&3&lJadAPI &7>> &bRe-parsing &apacket &eback to &b&lreadable &eform.");
                packet.parse(built);
                console.sendMessage("&3&lJadAPI &7>> &eFinished &bPacket &aChecking&e!");
                console.sendMessage("&aRebuild Result &m=>&a " + packet);
            }
        }));

        list.add(new Test("Test", (player) -> {
            JSender console = new JSender(Bukkit.getConsoleSender());

            IBlockData blockData = NMS.createBlock(JMaterial.getRegistryMaterials().find(JMaterial.MaterialEnum.OAK_SIGN)).createBlockData();

            Location location = player.getPlayer().getLocation();
            BlockPosition position = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

            WorldServer world = NMS.toWorldServer(player.getPlayer().getWorld());

            world.setBlockData(position, blockData, false);

//            Location location = player.getPlayer().getLocation();
//            TileEntity entity = NMS.toWorldServer(player.getPlayer().getWorld()).getTileEntity(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
//
//            Class<?> clazz = JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8) ? Array.newInstance(IChatBaseComponent.iChatBaseComponentClass, 0).getClass() : String[].class;
//
//            Object[] objects = (Object[]) JFieldReflector.getObjectFromUnspecificField(TileEntitySign.TILE_ENTITY_SIGN, clazz, (i) -> 1, entity.getHandle());
//            objects[0] = new IChatBaseComponent("Test", false, null).build();

            console.sendMessage("finished!");
        }));
    }

    private static void checkEnum(NMSEnum[] enums) {
        JSender console = new JSender(Bukkit.getConsoleSender());
        console.sendMessage("&3&lJadAPI &7>> &eChecking enum " + enums[0].getClass().getSimpleName() + "! Length -> " + enums.length);
        console.sendMessage("&3&lJadAPI &7>> &eJadAPI Enums &m=>&a " + Arrays.toString(enums));
        try {
            Object[] parsed = Arrays.stream(enums).map(NMSEnum::getNMSObject).toArray();
            console.sendMessage("&3&lJadAPI &7>> &eNMS Enums &m=>&a " + Arrays.toString(parsed));
            console.sendMessage("&3&lJadAPI &7>> &eConverting Back...");

            Class<? extends NMSEnum> clazz = enums[0].getClass();
            NMSEnum[] conversionBack = Arrays.stream(parsed).map(object -> NMSEnum.getEnum(clazz, object)).toArray(NMSEnum[]::new);
            console.sendMessage("&3&lJadAPI &7>> &eConverted JadAPI Enum &m=>&a " + Arrays.toString(conversionBack));
        } catch (Exception ex) {
            console.sendMessage("&3&lJadAPI &7>> &eCouldn't check Enum.");
            console.sendMessage("&aReason &m=>&c " + ex.getMessage());
        }
    }

    public static class Test {

        private final String testName;
        private final Consumer<JPlayer> runnable;

        public Test(String name, Consumer<JPlayer> runnable) {
            this.testName = name;
            this.runnable = runnable;
        }

        public Exception run(JPlayer player) {
            try {
                this.runnable.accept(player);
                return null;
            } catch(Exception ex) {
                ex.printStackTrace();
                return ex;
            }
        }
    }
}
