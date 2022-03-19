package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.utils.JReflection;

public class OutPlayerHeaderFooter extends DefinedPacket {

    public static final Class<?> playerHeaderFooterPacketClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + JReflection.getNMSVersion()) + ".PacketPlayOutPlayerListHeaderFooter");

    private IChatBaseComponent headerComponent;
    private IChatBaseComponent footerComponent;

    public OutPlayerHeaderFooter() {
    }

    public OutPlayerHeaderFooter(IChatBaseComponent headerComponent, IChatBaseComponent footerComponent) {
        this.headerComponent = headerComponent;
        this.footerComponent = footerComponent;
    }

    public IChatBaseComponent getHeaderComponent() {
        return headerComponent;
    }

    public void setHeaderComponent(IChatBaseComponent component) {
        this.headerComponent = component;
    }

    public IChatBaseComponent getFooterComponent() {
        return footerComponent;
    }

    public void setFooterComponent(IChatBaseComponent component) {
        this.footerComponent = component;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        this.headerComponent = new IChatBaseComponent();
        this.headerComponent.parse(JReflection.getUnspecificFieldObject(playerHeaderFooterPacketClass, IChatBaseComponent.iChatBaseComponentClass, 0, packet));

        this.footerComponent = new IChatBaseComponent();
        this.footerComponent.parse(JReflection.getUnspecificFieldObject(playerHeaderFooterPacketClass, IChatBaseComponent.iChatBaseComponentClass, 1, packet));
    }

    @Override
    public Object build() {
        Object packet;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JReflection.executeConstructor(playerHeaderFooterPacketClass, new Class[]{});

            JReflection.setUnspecificField(playerHeaderFooterPacketClass, IChatBaseComponent.iChatBaseComponentClass, 0, packet, this.headerComponent.build());
            JReflection.setUnspecificField(playerHeaderFooterPacketClass, IChatBaseComponent.iChatBaseComponentClass, 1, packet, this.footerComponent.build());
        } else {
            packet = JReflection.executeConstructor(playerHeaderFooterPacketClass, new Class[]{IChatBaseComponent.iChatBaseComponentClass, IChatBaseComponent.iChatBaseComponentClass}, this.headerComponent.build(), this.footerComponent.build());
        }
        return packet;
    }

    @Override
    public boolean canParse(Object packet) {
        return playerHeaderFooterPacketClass.equals(packet.getClass());
    }

    @Override
    public Class<?> getParsingClass() {
        return playerHeaderFooterPacketClass;
    }

    @Override
    public DefinedPacket copy() {
        return new OutPlayerHeaderFooter((IChatBaseComponent) this.headerComponent.copy(), (IChatBaseComponent) this.footerComponent.copy());
    }
}
