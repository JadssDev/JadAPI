package dev.jadss.jadapi.management.nms;

/**
 * An exception to NMS behavior.
 */
public class NMSException extends RuntimeException {
    public NMSException(String errorMSG) { super(errorMSG); }
}

