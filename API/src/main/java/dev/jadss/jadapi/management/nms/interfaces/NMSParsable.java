package dev.jadss.jadapi.management.nms.interfaces;

/**
 * Means the object can be Parsable using an NMS Object.
 */
public interface NMSParsable {

    /**
     * The Class that is parsed here.
     * @return the Class.
     */
    Class<?> getParsingClass();
    /**
     * Parse the object in NMS to this Object.
     * @param object the Object.
     */
    void parse(Object object);
    /**
     * Check if we can parse the object.
     * @param object the object to test.
     * @return if we can as a {@link Boolean}!
     */
    boolean canParse(Object object);
}
