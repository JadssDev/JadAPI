package dev.jadss.server;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Packet implements Cloneable, Serializable {

    public abstract long getPacketProtocol();
    public abstract Packet parsePacket(Object object);

    public static Object getFieldFromPacket(String fieldName, Object packetObject) {
        try {
            Field field = packetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(packetObject);
        } catch(NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Protocol getProtocolFromPacket(Object packetObject) {
        try {
            Method method = packetObject.getClass().getMethod("getPacketProtocol");
            method.setAccessible(true);
            return Protocol.parseProtocol((Long) method.invoke(packetObject));
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
