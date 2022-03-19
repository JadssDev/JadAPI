package dev.jadss.jadapi.bukkitImpl.storage;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.item.JHead;

/**
 * Represents a head that has been registered to jadapi!
 */
public class HeadIdentification {

    private final JadAPIPlugin provider;
    private final JHead head;

    protected HeadIdentification(JadAPIPlugin provider, JHead head) {
        this.provider = provider;
        this.head = head;
    }

    /**
     * Get the head!
     * @return the head!
     */
    public JHead getHead() {
        return head;
    }

    /**
     * Get the plugin that provided this head!
     * @return the plugin that provided this head! (JadAPI is a plugin that provides some default heads)
     */
    public JadAPIPlugin getProvider() {
        return provider;
    }
}
