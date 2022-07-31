package dev.jadss.jadapi.bukkitImpl.misc;

import com.google.common.collect.Lists;
import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.interfaces.other.PacketListener;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.management.nms.objects.world.entities.EntityArmorStand;
import dev.jadss.jadapi.management.nms.objects.world.positions.BlockPosition;
import dev.jadss.jadapi.management.nms.packets.InInteractAtEntityPacket;
import dev.jadss.jadapi.management.nms.packets.OutEntityDestroy;
import dev.jadss.jadapi.management.nms.packets.OutEntityMetadata;
import dev.jadss.jadapi.management.nms.packets.OutSpawnEntityLiving;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Represents an Hologram created by JadAPI <b>Using packets</b>.
 */
public final class JHologram implements PacketListener<InInteractAtEntityPacket> {

    private Location currentLocation;
    private final List<EntityArmorStand> armorStands;
    private final boolean autoAdd;
    private final List<JPlayer> playersShown = new ArrayList<>();

    /**
     * Create an hologram on the map!
     *
     * @param location The location to create at.
     * @param autoAdd  Should we add every player currently on the server and everyone that joins after?
     * @param messages the message the hologram should display.
     */
    public JHologram(Location location, boolean autoAdd, String... messages) {
        this.currentLocation = location;
        this.autoAdd = autoAdd;
        this.armorStands = generate(location.clone(), Arrays.asList(messages));

        JadAPI.getInstance().getInformationManager().getHolograms().add(this);

        if (autoAdd)
            show(JPlayer.getJPlayers().stream().filter(player -> location.getWorld().equals(player.getPlayer().getLocation().getWorld())).toArray(JPlayer[]::new));
    }

    /**
     * Create an hologram on the map!
     *
     * @param location The location to create at.
     * @param autoAdd  Should we add every player currently on the server and everyone that joins after?
     * @param messages the message the hologram should display.
     */
    public JHologram(Location location, boolean autoAdd, List<String> messages) {
        this.currentLocation = location;
        this.autoAdd = autoAdd;
        this.armorStands = generate(location.clone(), messages);

        JadAPI.getInstance().getInformationManager().getHolograms().add(this);

        if (autoAdd)
            show(JPlayer.getJPlayers().stream().filter(player -> location.getWorld().equals(player.getPlayer().getLocation().getWorld())).toArray(JPlayer[]::new));
    }

    /**
     * <b>Show</b> the Hologram to these players!
     *
     * @param players The players to show to.
     * @return itself.
     */
    public JHologram show(JPlayer... players) {
        players = Arrays.stream(players)
                .filter(player -> currentLocation.getWorld().equals(player.getPlayer().getLocation().getWorld()))
                .filter(player -> !this.playersShown.contains(player))
                .toArray(JPlayer[]::new);

        List<Object> addPackets = this.armorStands.stream().map(stand -> new OutSpawnEntityLiving(stand).build()).collect(Collectors.toList());
        List<Object> modifyPackets = this.armorStands.stream().map(stand -> new OutEntityMetadata(stand).build()).collect(Collectors.toList());

        Arrays.stream(players).forEach(p -> addPackets.forEach(p::sendPacket));
        Arrays.stream(players).forEach(p -> modifyPackets.forEach(p::sendPacket));

        playersShown.addAll(Arrays.asList(players));

        return this;
    }

    /**
     * <b>Hide</b> the Hologram to these players!
     *
     * @param players The players ro hide from.
     * @return itself.
     */
    public JHologram hide(JPlayer... players) {
        players = Arrays.stream(players)
                .filter(playersShown::contains)
                .toArray(JPlayer[]::new);
        playersShown.removeAll(Arrays.asList(players));

        List<Object> packets = this.armorStands.stream().map(stand -> new OutEntityDestroy(new int[]{stand.getEntityId()}).build()).collect(Collectors.toList());
        Arrays.stream(players).forEach(p -> packets.forEach(p::sendPacket));

        return this;
    }

    /**
     * <p>Edits the message of the hologram!</p>
     * <h1><b>Caviat:</b></h1>
     * <ul>
     *     <li>The message to edit for <b>must</b> have the exact number of lines then the current amount of lines!</li>
     * </ul>
     *
     * @param messages The messages to edit for.
     */
    public void edit(List<String> messages) {
        messages = Lists.reverse(messages);

        List<DefinedPacket> packets = new ArrayList<>();
        if (messages.size() == armorStands.size()) {
            for (int i = 0; i < this.armorStands.size(); i++)
                armorStands.get(i).setCustomName(new IChatBaseComponent(messages.get(i), false, ""));
            armorStands.forEach(stand -> packets.add(new OutEntityMetadata(stand)));
        } else throw new JException(JException.Reason.UNKNOWN);

        this.playersShown.forEach(player -> packets.forEach(packet -> player.sendPacket(packet.build())));
    }

    /**
     * Edits the message of the hologram!
     * <h1><b>Caviat:</b></h1>
     * <ul><li>The message to edit for <b>must</b> have exactly then same amount of lines the hologram has!</li></ul>
     *
     * @param messages The messages to edit for.
     */
    public void edit(String... messages) {
        this.edit(Arrays.asList(messages));
    }

    /**
     * Updates the location of this Hologram!
     *
     * @param location the new Location of this hologram.
     */
    public void updateLocation(Location location) { //Todo: 1.23-JadLaxyDev
        throw new UnsupportedOperationException("Not implemented yet!");
//        this.currentLocation = location;
//        List<Object> packets = new ArrayList<>();
//        this.armorStands.forEach(stand -> packets.add(new PacketPlayOutEntityTeleport(stand.getId(), (int) location.getX(), (int) location.getY(), (int) location.getZ(), (byte) location.getYaw(), (byte) location.getPitch(), false)));
//        this.playersShown.forEach(player -> packets.forEach(player::sendPacket));
    }

    /**
     * Does this Hologram have the function <b>AutoAdd</b> enabled?
     *
     * @return If the function is enabled!
     */
    public boolean doAutoAdd() {
        return autoAdd;
    }

    /**
     * Get the player that have this hologram <b>shown</b> to them!
     *
     * @return The players that have this hologram shown.
     */
    public List<JPlayer> getPlayersShown() {
        return playersShown;
    }

    /**
     * Gets the current location of the hologram.
     *
     * @return the location of the hologram!
     */
    public Location getLocation() {
        return currentLocation.clone();
    }

    /**
     * Delete this hologram.
     */
    public void delete() {
        List<Object> packets = this.armorStands.stream().map(stand -> new OutEntityDestroy(new int[]{stand.getEntityId()}).build()).collect(Collectors.toList());
        this.playersShown.forEach(players -> packets.forEach(players::sendPacket));

        JadAPI.getInstance().getInformationManager().getHolograms().remove(this);
    }

    private static List<EntityArmorStand> generate(Location location, List<String> messages) {
        List<EntityArmorStand> list = new ArrayList<>();

        for (String message : Lists.reverse(messages)) {
            location.add(0, 0.25, 0);
            EntityArmorStand stand = new EntityArmorStand(NMS.toWorldServer(location.getWorld()), new BlockPosition(location.getX(), location.getY(), location.getZ()));
            stand.setNoGravity(false);
            stand.setInvisible(true);
            stand.setCustomName(new IChatBaseComponent(message, false, ""));
            stand.setCustomNameVisible(true);
            list.add(stand);
        }

        return list;
    }

    //PacketListener

    private List<BiConsumer<JPlayer, InInteractAtEntityPacket>> listeners = new ArrayList<>();

    @Override
    public List<BiConsumer<JPlayer, InInteractAtEntityPacket>> getListeners() {
        return listeners;
    }

    @Override
    public void addListener(BiConsumer<JPlayer, InInteractAtEntityPacket> listener) {
        listeners.add(listener);
    }

    @Override
    public boolean shouldCallListeners(InInteractAtEntityPacket packet) {
        return false;
//        return this.armorStands.stream().anyMatch(entity -> entity.getEntityId() == packet.getEntityID());
    }
}
