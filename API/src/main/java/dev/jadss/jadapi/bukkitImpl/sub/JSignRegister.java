package dev.jadss.jadapi.bukkitImpl.sub;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.exceptions.JException;
import dev.jadss.jadapi.interfaces.Copyable;
import dev.jadss.jadapi.interfaces.misc.JSignRegistry;
import org.bukkit.Location;

import java.util.function.Consumer;

/**
 * Represents a sign registered, this is, a registerer that is used on signs that JadAPI are used, for example, to search a specific term in a menu!
 */
public final class JSignRegister implements Copyable<JSignRegister> {

    private Location location = new Location(null, 0D, 0D, 0D);

    private Consumer<String[]> runnable;
    private final JadAPIPlugin registerer;

    private final String line1;
    private final String line2;
    private final String line3;
    private final String line4;

    /**
     * Create a sign registerer to use in the openSignType method.
     *
     * @param runnable   the runnable when the sign is updated by the player.
     * @param registerer the plugin that registers this sign.
     * @param line1      the first line
     * @param line2      the second line
     * @param line3      the thirrrd line
     * @param line4      the fourht line
     */
    public JSignRegister(Consumer<String[]> runnable, JadAPIPlugin registerer, String line1, String line2, String line3, String line4) {
        if (runnable == null || registerer == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        this.runnable = runnable;
        this.registerer = registerer;

        if (line1 == null)
            this.line1 = "";
        else
            this.line1 = line1;

        if (line2 == null)
            this.line2 = "";
        else
            this.line2 = line2;

        if (line3 == null)
            this.line3 = "";
        else
            this.line3 = line3;

        if (line4 == null)
            this.line4 = "";
        else
            this.line4 = line4;
    }

    /**
     * Create a JSignRegister from a JSignRegisterAbstract Object.
     *
     * @param abstractRegistration JSignRegistererAbstract Class.
     */
    public JSignRegister(JSignRegistry abstractRegistration) {
        if (abstractRegistration.getConsumer() == null || abstractRegistration.getRegisterer() == null)
            throw new JException(JException.Reason.VALUE_IS_NULL);

        this.runnable = abstractRegistration.getConsumer();
        this.registerer = abstractRegistration.getRegisterer();

        if (abstractRegistration.getSignLines() != null) {
            if (abstractRegistration.getSignLines().length == 4) {
                this.line1 = abstractRegistration.getSignLines()[0];
                this.line2 = abstractRegistration.getSignLines()[1];
                this.line3 = abstractRegistration.getSignLines()[2];
                this.line4 = abstractRegistration.getSignLines()[3];
            } else throw new JException(JException.Reason.SIGN_LINES_LENGTH_DONT_MATCH);
        } else {
            this.line1 = "";
            this.line2 = "";
            this.line3 = "";
            this.line4 = "";
        }
    }

    public Consumer<String[]> getConsumer() {
        return runnable;
    }

    public JadAPIPlugin getPluginRegisterer() {
        return registerer;
    }

    public String getLine(int line) {
        if (line == 1) return line1;
        else if (line == 2) return line2;
        else if (line == 3) return line3;
        else if (line == 4) return line4;
        else return null;
    }

    public String[] getLines() {
        return new String[]{line1, line2, line3, line4};
    }

    @Override
    public JSignRegister copy() {
        return new JSignRegister(runnable, registerer, line1, line2, line3, line4);
    }
}
