package dev.jadss.jadapi.interfaces.misc;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.exceptions.JException;

import java.util.function.Consumer;

/**
 * Registry of a Sign Register to JadAPI.
 */
public interface JSignRegistry {

    /**
     * Who is registering this Sign.
     * @return the JadAPIPlugin instance.
     * @throws JException if this is null!
     */
    JadAPIPlugin getRegisterer();

    /**
     * Called when the player ends typing at the sign.
     * @return The consumer, the Strings given is the 4 lines of the sign with what the player wrote/edited.
     */
    Consumer<String[]> getConsumer();

    /**
     * Get the default sign lines when a player enters the sign.
     * @return the Sign Texts.
     */
    String[] getSignLines();
}
