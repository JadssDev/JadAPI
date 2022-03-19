package dev.jadss.jadapi.bukkitImpl.storage;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.item.JHead;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the storage of heads.
 */
public class JHeadStorage {

    private static final Map<String, HeadIdentification> heads = new HashMap<>();

    private JHeadStorage() {
        //Utility class
    }

    /**
     * Add a head to the JadAPI Storage, maybe to use on your plugin, or maybe do a library for other plugins =)
     *
     * @param plugin     The plugin providing it.
     * @param identifier The identifier plugins will use to get this head.
     * @param head       the Head itself. using JHead
     * @throws JException if plugin == null OR identifier == null OR head == null ORRRRR if the identifier is already registered.
     */
    public static void addToStorage(JadAPIPlugin plugin, String identifier, JHead head) throws JException {
        if (plugin == null || identifier == null || head == null) throw new JException(JException.Reason.VALUE_IS_NULL);
        if (heads.containsKey(identifier)) throw new JException(JException.Reason.ALREADY_REGISTERED);

        heads.put(identifier, new HeadIdentification(plugin, head));
    }

    /**
     * @param identifier the "identifier" to look through all heads.
     * @return A list with the heads if any are found.
     * @throws JException if identifier == null
     */
    public static List<HeadIdentification> getCloseHead(String identifier) {
        if (identifier == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        List<HeadIdentification> headIdentifications = new ArrayList<>();

        for (String ident : heads.keySet())
            if (ident.contains(identifier))
                headIdentifications.add(heads.get(ident));

        return headIdentifications;
    }

    /**
     * Get a Head from the storage. (exact identifier)
     *
     * @param identifier The Identifier.
     * @return The HeadIdentification or NULL if none.
     * @throws JException if identifier is null
     */
    public static HeadIdentification getHead(String identifier) {
        if (identifier == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        return heads.get(identifier);
    }

    /**
     * Get all heads provided by a plugin.
     *
     * @param plugin The Plugin.
     * @return The list of HeadIdentifications of this plugin BUT be careful, it may return "NULL" if the plugin DOES NOT EXIST.
     * @throws JException if plugin is null
     */
    public static List<HeadIdentification> getHeads(Plugin plugin) {
        if (plugin == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        JadAPIPlugin pl = JadAPIPlugin.get(((JavaPlugin) plugin).getClass());
        if (pl == null) return null;

        List<HeadIdentification> headIdentifications = new ArrayList<>();

        for (HeadIdentification ident : heads.values())
            if (ident.getProvider().equals(pl))
                headIdentifications.add(ident);

        return headIdentifications;
    }
}
