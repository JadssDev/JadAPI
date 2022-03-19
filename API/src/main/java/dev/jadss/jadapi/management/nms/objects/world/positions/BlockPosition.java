package dev.jadss.jadapi.management.nms.objects.world.positions;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Location;

public class BlockPosition implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    public static final Class<?> blockPositionClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + JReflection.getNMSVersion()) + ".BlockPosition");
    public static final Class<?> baseBlockPositionClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "core" : "server." + JReflection.getNMSVersion()) + ".BaseBlockPosition");

    private double x = 0;
    private double y = 0;
    private double z = 0;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public BlockPosition() {
    }

    public BlockPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Object build() {
        if (JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7))
            throw new NMSException("This cannot be built in the current version!");

        return JReflection.executeConstructor(blockPositionClass, new Class[]{double.class, double.class, double.class}, this.x, this.y, this.z);
    }

    @Override
    public Class<?> getParsingClass() {
        return blockPositionClass;
    }

    @Override
    public void parse(Object object) {
        if (object == null) return;
        if (!canParse(object)) throw new NMSException("Cannot parse this object.");

        this.x = JReflection.getUnspecificFieldObject(baseBlockPositionClass, int.class, 0, object);
        this.y = JReflection.getUnspecificFieldObject(baseBlockPositionClass, int.class, 1, object);
        this.z = JReflection.getUnspecificFieldObject(baseBlockPositionClass, int.class, 2, object);
    }

    @Override
    public boolean canParse(Object object) {
        return object.getClass().equals(blockPositionClass);
    }

    @Override
    public NMSObject copy() {
        return new BlockPosition(this.x, this.y, this.z);
    }
}
