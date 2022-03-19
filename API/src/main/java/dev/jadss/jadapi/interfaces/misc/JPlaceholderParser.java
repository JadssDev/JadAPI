package dev.jadss.jadapi.interfaces.misc;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;

/**
 * Represents a Placeholder, may overwrite more then 1 placeholder or more.
 */
public interface JPlaceholderParser extends Placeholder {

    /**
     * Replace the line with the placeholders.
     * @param text the Line of text to replace the placeholders.
     * @param player The Player to parse this placeholder with.
     * @return the Final result.
     */
    String replaceLine(String text, JPlayer player);
}
