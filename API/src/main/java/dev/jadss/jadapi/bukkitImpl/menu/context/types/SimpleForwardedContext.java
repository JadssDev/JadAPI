package dev.jadss.jadapi.bukkitImpl.menu.context.types;

import dev.jadss.jadapi.bukkitImpl.menu.context.ForwardedContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Forwarded context.
 */
public class SimpleForwardedContext implements ForwardedContext {

    private final Map<String, Object> variables = new HashMap<>();

    /**
     * Gets a variable from the saved context.
     * @param variableName the variable name.
     * @return the object, or null if none were found.
     */
    public Object getVariable(String variableName) {
        return variables.get(variableName);
    }

    /**
     * Sets a variable to the saved context.
     * @param variableName the variable name
     * @param object the object to set to the variable.
     */
    public void setVariable(String variableName, Object object) {
        variables.put(variableName, object);
    }

    /**
     * Get the entries of the saved context.
     * @return the entries.
     */
    public Map<String, Object> entries() {
        return new HashMap<>(variables);
    }

}
