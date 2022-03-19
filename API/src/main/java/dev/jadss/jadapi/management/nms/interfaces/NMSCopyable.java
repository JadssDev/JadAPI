package dev.jadss.jadapi.management.nms.interfaces;

/**
 * Represents an Object in NMS that can be copied into another object.
 */
public interface NMSCopyable {

    /**
     * Copy the object into another object of NMS.
     * @return the Object in JadAPI terms is given here.
     */
    NMSObject copy();
}
