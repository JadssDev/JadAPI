package dev.jadss.jadapi.bukkitImpl.misc;

import com.google.common.collect.Lists;
import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.interfaces.misc.JPlaceholderParser;
import dev.jadss.jadapi.interfaces.misc.Placeholder;
import dev.jadss.jadapi.interfaces.scoreboard.JScoreboardAbstract;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Represents a scoreboard that can be displayed to players.
 * <p>Contains Anti-FLICk!!!!</p>
 */
public final class JScoreboard implements Copyable<JScoreboard> {

    private List<String> lines = new ArrayList<>();
    private String title = "&3&lScoreboard!";
    private final List<Placeholder> placeholders = new ArrayList<>();

    /**
     * Creates a scoreboard.
     *
     * @param title the title of the scoreboard.
     * @param lines the lines of the scoreboard.
     */
    public JScoreboard(String title, List<String> lines) {
        if (title == null || lines == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        this.lines = lines;
        this.title = title;
    }

    /**
     * Creates a scoreboard.
     *
     * @param title the title of the scoreboard.
     * @param lines the lines of the scoreboard.
     */
    public JScoreboard(String title, String... lines) {
        if (title == null || lines == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        this.lines = new ArrayList<>(Arrays.asList(lines));
        this.title = title;
    }

    /**
     * Creates a scoreboard with default values.
     */
    public JScoreboard() {
        this.lines.add("This is a scoreboard");
        this.lines.add("made with JadAPI!!!");
    }

    /**
     * Create a scoreboard using an abstract class.
     *
     * @param abstractScoreboard the abstract scoreboard object to use.
     */
    public JScoreboard(JScoreboardAbstract abstractScoreboard) {
        this.title = abstractScoreboard.getTitle();
        this.lines = abstractScoreboard.getScoreboardLines();
        if (abstractScoreboard.getPlaceholders() != null)
            this.placeholders.addAll(abstractScoreboard.getPlaceholders());
    }

    /**
     * Adds a placeholder to the scoreboard.
     *
     * @param placeholder the placeholder to add.
     * @return the scoreboard.
     */
    public JScoreboard addPlaceholder(Placeholder placeholder) {
        if (placeholder == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        this.placeholders.add(placeholder);

        return this;
    }

    /**
     * Remove a placeholder from the scoreboard.
     *
     * @param placeholderClass the scoreboard placeholder to remove.
     * @param <S>              the placeholder class.
     * @return itself
     */
    public <S extends Placeholder> JScoreboard removePlaceholder(Class<S> placeholderClass) {
        this.placeholders.removeIf(placeholder -> placeholder.getClass().equals(placeholderClass));

        return this;
    }

    /**
     * Remove a placeholder from the scoreboard.
     *
     * @param placeholder the placeholder to remove.
     * @param <S>         the placeholder object.
     * @return itself.
     */
    public <S extends Placeholder> JScoreboard removePlaceholder(S placeholder) {
        this.placeholders.remove(placeholder);

        return this;
    }

    /**
     * This builds a scoreboard for the player with everything already done.
     *
     * @param player the player parameter.
     * @return the scoreboard as a bukkit scoreboard.
     */
    public Scoreboard buildScoreboard(JPlayer player) {
        if (this.title.toCharArray().length + 1 > 16) throw new JException(JException.Reason.SCOREBOARD_TITLE_TOO_LONG);

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = createObjective(board, ChatColor.translateAlternateColorCodes('&', this.title));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> linesReversed = Lists.reverse(this.lines);

        for (int i = 0; i < linesReversed.size(); i++) {
            for (Placeholder placeholder : this.placeholders)
                if (placeholder instanceof JPlaceholderParser)
                    linesReversed.set(i, ((JPlaceholderParser) placeholder).replaceLine(linesReversed.get(i), player));

            objective.getScore(ChatColor.translateAlternateColorCodes('&', linesReversed.get(i))).setScore(i);
        }

        return board;
    }

    private static Objective createObjective(Scoreboard scoreboard, String title) {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            return (Objective) JReflection.executeMethod(Scoreboard.class, "registerNewObjective", scoreboard, new Class[]{String.class, String.class, String.class}, title, "dummy", title);
        } else {
            return (Objective) JReflection.executeMethod(Scoreboard.class, "registerNewObjective", scoreboard, new Class[]{String.class, String.class}, title, "dummy");
        }
    }

    /**
     * This updates the player Scoreboard.
     *
     * @param player the player to update it for.
     */
    public void updatePlayer(JPlayer player) {
        if (this.title.toCharArray().length + 1 > 16) throw new JException(JException.Reason.SCOREBOARD_TITLE_TOO_LONG);

        Scoreboard board = player.getPlayer().getScoreboard();

        if (board == null || player.getPlayer().getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()))
            board = Bukkit.getScoreboardManager().getNewScoreboard();

        Set<Objective> objectives = board.getObjectivesByCriteria("dummy");
        Objective objective = null;

        for (Objective obj : objectives) {
            if (obj.getDisplaySlot() == DisplaySlot.SIDEBAR) {
                objective = obj;
                break;
            }
        }

        if (objective == null) {
            objective = createObjective(board, ChatColor.translateAlternateColorCodes('&', this.title));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        if (!objective.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', this.title)))
            objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.title));


        List<String> linesReversed = new ArrayList<>(Lists.reverse(this.lines));

        for (int i = 0; i < linesReversed.size(); i++) {
            for (Placeholder placeholder : this.placeholders)
                if (placeholder instanceof JPlaceholderParser)
                    linesReversed.set(i, ChatColor.translateAlternateColorCodes('&', ((JPlaceholderParser) placeholder).replaceLine(linesReversed.get(i), player)));

            String currentScoreReset = getScore(objective, i);

            if (currentScoreReset != null) {
                if (currentScoreReset.equals(linesReversed.get(i)))
                    continue;
                if (JadAPI.getInstance().getDebug().doMiscDebug())
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cRemoving &e\"&f" + currentScoreReset + "&e\" Score from &3&lScoreboard &eof &b" + player.getPlayer().getName() + "&e!"));
                board.resetScores(currentScoreReset);
            }

            if (JadAPI.getInstance().getDebug().doMiscDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eSetting &3&lscore &eto &b\"&e" + linesReversed.get(i) + "&b\" &eof &b" + player.getPlayer().getName() + "&e!"));
            objective.getScore(ChatColor.translateAlternateColorCodes('&', linesReversed.get(i))).setScore(i);
        }

        for (String entry : board.getEntries()) {
            if (JadAPI.getInstance().getDebug().doMiscDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eChecking if &e\"" + entry + "&e\" exists in &a" + linesReversed));
            if (!linesReversed.contains(entry)) {

                board.resetScores(entry);
                if (JadAPI.getInstance().getDebug().doMiscDebug())
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &cRemoving &e\"&f" + entry + "&e\", was not found in the lines, from &3&lScoreboard &eof &b" + player.getPlayer().getName() + "&e!"));
            }
        }

        player.getPlayer().setScoreboard(board);
    }

    private static String getScore(Objective objective, int score) {
        for (String s : objective.getScoreboard().getEntries()) {
            if (objective.getScore(s).getScore() == score)
                return objective.getScore(s).getEntry();
        }
        return null;
    }

    /**
     * Update the lines of the scoreboard.
     *
     * @param lines the lines to update to.
     * @return itself.
     */
    public JScoreboard updateLines(List<String> lines) {
        if (lines == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        this.lines = lines;

        return this;
    }

    /**
     * Update the title of the scoreboard.
     *
     * @param title the title to update to.
     * @return itself.
     */
    public JScoreboard updateTitle(String title) {
        if (title == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        this.title = title;

        return this;
    }

    /**
     * Get the title of the scoreboard.
     *
     * @return the title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the lines of the scoreboard.
     *
     * @return the lines.
     */
    public List<String> getLines() {
        return this.lines;
    }

    public JScoreboard copy() {
        JScoreboard board = new JScoreboard(this.title, this.lines);
        for (Placeholder placeholder : this.placeholders)
            board.addPlaceholder(placeholder);
        return board;
    }
}
