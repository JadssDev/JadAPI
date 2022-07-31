package dev.jadss.jadapi.bukkitImpl.menu.context;

import dev.jadss.jadapi.bukkitImpl.item.AbstractInventory;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;

import java.util.Map;

/**
 * Represents information that can be accessed by the menu, but it's not saved permanently.
 */
public interface SavedContext<T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> extends Context<T, K> {

    /**
     * Gets a variable from the saved context.
     * @param variableName the variable name.
     * @return the object, or null if none were found.
     */
    Object getVariable(String variableName);

    /**
     * Sets a variable to the saved context.
     * @param variableName the variable name
     * @param object the object to set to the variable.
     */
    void setVariable(String variableName, Object object);

    /**
     * Get the entries of the saved context.
     * @return the entries.
     */
    Map<String, Object> entries();

}
