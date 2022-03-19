package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.other.GameProfile;
import dev.jadss.jadapi.utils.JReflection;

public class InLoginStart extends DefinedPacket {

    private GameProfile gameProfile;

    public static final Class<?> loginStartPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.login" : "server." + JReflection.getNMSVersion()) + ".PacketLoginInStart");

    public InLoginStart() {
    }

    public InLoginStart(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    @Override
    public Object build() {
        return JReflection.executeConstructor(loginStartPacketClass, new Class[]{gameProfile.getParsingClass()}, gameProfile.build());
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        this.gameProfile = new GameProfile();
        this.gameProfile.parse(JReflection.getUnspecificFieldObject(loginStartPacketClass, this.gameProfile.getParsingClass(), packet));
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
}
