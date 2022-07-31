package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.annotations.Beta;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.other.GameProfile;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

import java.util.Optional;

@Beta(description = "This class cannot get a GameProfile after version 1.19.")
public class InLoginStart extends DefinedPacket {

    private String playerName;

    private GameProfile gameProfile;

    public static final Class<?> loginStartPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.login" : "server." + NMS.getNMSVersion()) + ".PacketLoginInStart");

    public InLoginStart() {
    }

    public InLoginStart(GameProfile gameProfile) {
        this.playerName = gameProfile.getName();
        this.gameProfile = gameProfile;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
        this.playerName = gameProfile.getName();
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19)) {
            return JConstructorReflector.executeConstructor(loginStartPacketClass, new Class[] {String.class, Optional.class}, playerName, Optional.empty());
        } else {
            return JConstructorReflector.executeConstructor(loginStartPacketClass, new Class[]{GameProfile.gameProfileClass}, gameProfile.getHandle());
        }
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_19)) {
            this.playerName = JFieldReflector.getObjectFromUnspecificField(loginStartPacketClass, String.class, packet);
        } else {
            this.gameProfile = new GameProfile(JFieldReflector.getObjectFromUnspecificField(loginStartPacketClass, GameProfile.gameProfileClass, packet));
            this.playerName = this.gameProfile.getName();
        }
    }

    @Override
    public Class<?> getParsingClass() {
        return loginStartPacketClass;
    }

    @Override
    public boolean canParse(Object object) {
        return loginStartPacketClass.equals(object.getClass());
    }

    @Override
    public DefinedPacket copy() {
        return new InLoginStart((GameProfile) this.gameProfile.copy());
    }

    @Override
    public String toString() {
        return "InLoginStart{" +
                "playerName='" + playerName + '\'' +
                ", gameProfile=" + gameProfile +
                '}';
    }
}
