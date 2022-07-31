package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JConstructorReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JFieldReflector;

public class OutUpdateHealth extends DefinedPacket {

    private float health;
    private int food;
    private float saturation;

    private static final Class<?> updateHealthPacketClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.protocol.game" : "server." + NMS.getNMSVersion()) + ".PacketPlayOutUpdateHealth");

    public OutUpdateHealth() {
    }

    public OutUpdateHealth(float health, int food, float saturation) {
        this.health = health;
        this.food = food;
        this.saturation = saturation;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }


    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }


    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    @Override
    public void parse(Object packet) {
        if (packet == null)
            return;
        if (!canParse(packet))
            throw new NMSException("The packet specified is not parsable by this class.");

        this.health = JFieldReflector.getObjectFromUnspecificField(updateHealthPacketClass, float.class, (i) -> i - 1, packet);
        this.food = JFieldReflector.getObjectFromUnspecificField(updateHealthPacketClass, int.class, packet);
        this.saturation = JFieldReflector.getObjectFromUnspecificField(updateHealthPacketClass, float.class, (i) -> i, packet);
    }

    @Override
    public Object build() {
        return JConstructorReflector.executeConstructor(updateHealthPacketClass, new Class[]{float.class, int.class, float.class}, this.health, this.food, this.saturation);
    }

    @Override
    public boolean canParse(Object object) {
        return updateHealthPacketClass.equals(object.getClass());
    }

    @Override
    public Class<?> getParsingClass() {
        return updateHealthPacketClass;
    }

    @Override
    public DefinedPacket copy() {
        return new OutUpdateHealth(this.health, this.food, this.saturation);
    }

    @Override
    public String toString() {
        return "OutUpdateHealth{" +
                "health=" + health +
                ", food=" + food +
                ", saturation=" + saturation +
                '}';
    }
}
