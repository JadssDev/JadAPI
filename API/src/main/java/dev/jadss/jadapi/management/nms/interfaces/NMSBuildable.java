package dev.jadss.jadapi.management.nms.interfaces;

/**
 * Represents an object can be built into an NMS Object.
 */
public interface NMSBuildable {

    /**
     * Build this object.
     * @return the Result in NMS.
     */
    Object build();
}
