package dev.jadss.jadapi.management.nms.interfaces;

import dev.jadss.jadapi.management.nms.NMSException;

import java.util.List;

/**
 * Defines a Packet that processes various types of packets.
 */
public abstract class MultipleDefinedPacket extends DefinedPacket {

    /**
     * Get the classes this <b>Multiple parser</b> parses!
     * @return A {@link List} with every class it parses.
     */
    public abstract List<Class<?>> getParsingClasses();

    /**
     * Not using this method, it's not ok in this case.
     * @return e
     */
    public Class<?> getParsingClass() { throw new NMSException("This class can parse more then 1 object, please use \"getParsingClasses\" method instead of this one!"); }

}
