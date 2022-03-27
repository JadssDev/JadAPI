package dev.jadss.jadapi.management.nms.objects.world.positions;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.utils.JReflection;

public class Vec3D implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    private double x;
    private double y;
    private double z;

    public static final Class<?> vec3DClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.phys" : "server." + JReflection.getNMSVersion()) + ".Vec3D");

    public Vec3D() { if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) throw new NMSException("This is not available in 1.7 or lower."); }

    public Vec3D(double x, double y, double z) {
        if(JVersion.getServerVersion().isLowerOrEqual(JVersion.v1_7)) throw new NMSException("This is not available in 1.7 or lower.");

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getZ() { return z; }
    public void setZ(double z) { this.z = z; }

    @Override
    public Object build() { return JReflection.executeConstructor(vec3DClass, new Class[] { double.class, double.class, double.class }, this.x, this.z, this.y); }

    @Override
    public void parse(Object object) {
        if(object == null) return;
        if(!canParse(object)) throw new NMSException("Cannot parse this object.");

        this.x = JReflection.getFieldObject(vec3DClass, double.class, object, (i) -> 0);
        this.y = JReflection.getFieldObject(vec3DClass, double.class, object, (i) -> 1);
        this.z = JReflection.getFieldObject(vec3DClass, double.class, object, (i) -> 2);
    }

    @Override
    public Class<?> getParsingClass() { return vec3DClass; }

    @Override
    public boolean canParse(Object object) { return object.getClass().equals(vec3DClass); }

    @Override
    public NMSObject copy() { return new Vec3D(this.x, this.y, this.z); }
}
