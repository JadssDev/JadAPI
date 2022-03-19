package dev.jadss.jadapi.interfaces.scoreboard;

import dev.jadss.jadapi.interfaces.misc.JPlaceholderParser;

import java.util.List;

/**
 * A Scoreboard to be created in a <b>abstract</b> way.
 */
public abstract class JScoreboardAbstract {

    /**
     * Get the title of the Scoreboard.
     * @return the Title as a {@link String}!
     */
    public abstract String getTitle();

    /**
     * The lines to show on the scoreboard.
     * @return a {@link List} containing these.
     */
    public abstract List<String> getScoreboardLines();

    /**
     * The Placeholders this Scoreboard contains!
     * @return As a {@link List}!
     */
    public abstract List<JPlaceholderParser> getPlaceholders();

}
