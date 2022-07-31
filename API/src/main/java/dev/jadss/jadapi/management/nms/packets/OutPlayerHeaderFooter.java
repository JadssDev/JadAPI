package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.management.nms.objects.chat.IChatBaseComponent;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class OutPlayerHeaderFooter extends DefinedPacket {

    public static final Class<?> playerHeaderFooterPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutPlayerListHeaderFooter");

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
        this.headerComponent.parse(JFieldReflector.getObjectFromUnspecificField(playerHeaderFooterPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> i - 1, packet));

        this.footerComponent = new IChatBaseComponent();
        this.footerComponent.parse(JFieldReflector.getObjectFromUnspecificField(playerHeaderFooterPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> i, packet));
    }

    @Override
    public Object build() {
        Object packet;
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_16)) {
            packet = JConstructorReflector.executeConstructor(playerHeaderFooterPacketClass, new Class[]{});

            JFieldReflector.setObjectToUnspecificField(playerHeaderFooterPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> i - 1, packet, this.headerComponent.build());
            JFieldReflector.setObjectToUnspecificField(playerHeaderFooterPacketClass, IChatBaseComponent.iChatBaseComponentClass, (i) -> i, packet, this.footerComponent.build());
        } else {
            packet = JConstructorReflector.executeConstructor(playerHeaderFooterPacketClass, new Class[]{IChatBaseComponent.iChatBaseComponentClass, IChatBaseComponent.iChatBaseComponentClass}, this.headerComponent.build(), this.footerComponent.build());
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

    @Override
    public String toString() {
        return "OutPlayerHeaderFooter{" +
                "headerComponent=" + headerComponent +
                ", footerComponent=" + footerComponent +
                '}';
    }
}
