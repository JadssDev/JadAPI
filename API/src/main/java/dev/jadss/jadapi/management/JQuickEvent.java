package dev.jadss.jadapi.management;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class JQuickEvent<E extends Event> {

    /**
     * To specify all events to the quick listener use this class!
     */
    public static final Class<All> ALL_EVENTS = All.class;

    /**
     * To specify always true on the checker, use this Predicate.
     */
    public static final Predicate<?> ALWAYS_TRUE = (e) -> true;
    /**
     * To specify always false on the checker, use this Predicate (no point)
     */
    public static final Predicate<?> ALWAYS_FALSE = (e) -> false;
    /**
     * To specify a random true or false on the checker, use this Predicate.
     */
    public static final Predicate<?> RANDOM_TRUE = (e) -> ThreadLocalRandom.current().nextBoolean();

    private final JadAPIPlugin registerer;

    private final Class<E> eventType;

    private final EventPriority priority;

    private int ticksToUnregister;

    private final int executionsDefault;
    private int executions;

    private final Consumer<E> eventHandler;
    private final Predicate<E> checker;

    private final String id;

    /**
     * Create a QuickEvent on JadAPI!
     * @param registerer The registerer of this QuickEvent!
     * @param eventClass The Event class this QuickEvent is going to listen for! (Use {@link JQuickEvent#ALL_EVENTS} for Every Event!
     * @param priority The Priority of this QuickEvent (on bukkit)
     * @param eventHandler The Handler of this QuickEvent when an Event is called!
     * @param ticksToUnregister The amount of ticks for this QuickEvent to unregister.
     * @param executions The amount of executions for this QuickEvent to unregister itself.
     * @param checker The checker to check if this QuickEvent should be called.
     * @param id the ID of this QuickEvent, should be unique!
     */
    public JQuickEvent(JadAPIPlugin registerer, Class<E> eventClass, EventPriority priority, Consumer<E> eventHandler, int ticksToUnregister, int executions, Predicate<E> checker, String id) {
        this.registerer = registerer;
        this.eventType = eventClass;
        this.priority = priority;

        this.ticksToUnregister = ticksToUnregister;

        this.executions = executions;
        this.executionsDefault = executions;

        this.eventHandler = eventHandler;
        this.checker = checker;

        if (id == null)
            id = JQuickEvent.generateID();

        if (getQuickEvent(id) != null)
            throw new JException(JException.Reason.OTHER);

        this.id = id;

        if (!loadHandlerList((Class<Event>) eventClass)) {
            if (JadAPI.getInstance().getDebug().doMiscDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCouldn't &b&lload &ethe &3HandlerList &eof the &bEvent &l" + eventClass.getSimpleName() + "&e!"));
        }
    }

    @Deprecated
    public JQuickEvent(JadAPIPlugin registerer, Class<E> eventClass, Consumer<E> eventHandler, EventPriority priority, int ticksToUnregister, int executionsRemaining, String id) {
        this(registerer, eventClass, priority, eventHandler, ticksToUnregister, executionsRemaining, (Predicate<E>) JQuickEvent.ALWAYS_TRUE, id);
    }

    @Deprecated
    public JQuickEvent(JadAPIPlugin registerer, Class<E> eventClass, Consumer<E> eventHandler, EventPriority priority, int ticksToUnregister, int executionsRemaining) {
        this(registerer, eventClass, priority, eventHandler, ticksToUnregister, executionsRemaining, (Predicate<E>) JQuickEvent.ALWAYS_TRUE, generateID());
    }

    /**
     * <p>Register the QuickEvent to JadEventHandler</p>
     * <p>Set this to true to start this QuickEvent, starting this QuickEvent will instantly register it and making it able to be called in a nano second later, but it will unregister itself if the executions run out or ticks to unregister reaches 0!</p>
     * <p>Set this to false to stop this QuickEvent from running.</p>
     * @param enable if this QuickEvent should be registered.
     * @return itself.
     */
    public JQuickEvent<E> register(boolean enable) {
        if (!this.registerer.isRegistered()) {
            throw new JException(JException.Reason.UNREGISTERED);
        }

        if (enable) {
            if (this.isRegistered())
                return this;

            this.setExecutions(executionsDefault);
            JadAPI.getInstance().getRegisterer().registerQuickEvent(this);
        } else {
            this.setExecutions(0);
            JadAPI.getInstance().getRegisterer().unregisterQuickEvent(this);
        }

        return this;
    }

    /**
     * Checks the predicate using the specified Event.
     * @param event The event to check.
     * @return if the predicate is true or false.
     */
    public boolean check(Event event) {
        return checker.test((E) event);
    }


    /**
     * Run the event.
     * @param event Event that it should be ran with.
     * @return itself.
     */
    public JQuickEvent<E> run(Event event) {
        if (event == null)
            throw new JException(JException.Reason.VALUE_IS_NULL);

        if (executions != -1) {
            if (executions == 0) {
                this.register(false);
                return null;
            }

            executions--;
        }

        try {
            eventHandler.accept((E) event);
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3&lJadAPI &7>> &eThis &3&lerror &eis &c&lNOT &efrom &3&lJadAPI&e, &b" + registerer.getJavaPlugin().getName() + " &3thrown this &c&lerror &eplease &b&lcontact &ethem! "));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
        }

        return this;
    }

    //Getters and Setters

    /**
     * Check if this QuickEvent is Registered.
     * @return if this QuickEvent is registered!
     */
    public boolean isRegistered() {
        return JadAPI.getInstance().getInformationManager().getQuickEvents().contains(this);
    }

    /**
     * Get the Registerer of this QuickEvent.
     * @return the Registerer of this QuickEvent.
     */
    public JadAPIPlugin getRegisterer() {
        return this.registerer;
    }

    /**
     * Get the class that this QuickEvent listens for.
     * @return the class that this QuickEvent listens for.
     */
    public Class<E> getEventType() {
        return this.eventType;
    }

    /**
     * Get the priority of this QuickEvent.
     * @return the priority of this QuickEvent.
     */
    public EventPriority getPriority() {
        return this.priority;
    }

    /**
     * Get how many ticks for this QuickEvent to unregister itself.
     * @return how many ticks for this QuickEvent to unregister itself.
     */
    public int getTicksToUnregister() {
        return this.ticksToUnregister;
    }

    /**
     * Set how many ticks for this QuickEvent to unregister itself.
     * @param ticks the ticks to set to.
     */
    public void setTicksToUnregister(int ticks) {
        this.ticksToUnregister = ticks;
    }

    /**
     * Get the default number of executions!
     * @return the default number of executions!
     */
    public int getExecutionsDefault() {
        return this.executionsDefault;
    }

    /**
     * Get the number of executions of this QuickEvent until it unregisters itself.
     * @return the number of executions of this QuickEvent until it unregisters itself.
     */
    public int getExecutions() {
        return this.executions;
    }

    /**
     * Set the number of executions of this QuickEvent until it unregisters itself.
     * @param executions the number of executions to set to.
     */
    public void setExecutions(int executions) {
        this.executions = executions;
    }

    /**
     * Get the ID of this QuickEvent.
     * @return the id of this QuickEvent!
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the provider plugin!
     * @return the provider plugin of this quick event!
     * @deprecated Please use {@link JQuickEvent#getRegisterer} instead.
     */
    @Deprecated
    public JadAPIPlugin getProviderPlugin() {
        return registerer;
    }

    //End.


    /**
     * Load the HandlerList of an Event! This is necessary for JadAPI to work correctly!
     * @param eventClass The class of the event to load.
     * @return if the HandlerList was loaded successfully!
     */
    public static boolean loadHandlerList(Class<? extends Event> eventClass) {
        try {
            eventClass.getMethod("getHandlerList").invoke(eventClass);
            return true;
        } catch(Exception ignored) {
            return false;
        }
    }

    private static final char[] ID_CHARACTERS = ("abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "!\"#$%&/(*)").toCharArray();

    /**
     * Generate an ID for your JQuickListener.
     *
     * @return A string with some randomized characters as ID.
     */
    public static String generateID() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            builder.append(ID_CHARACTERS[new Random().nextInt(ID_CHARACTERS.length)]);
        }
        return builder.toString();
    }

    /**
     * Get a QuickListener by its ID.
     * @param id the id to search for.
     * @return The JQuickEvent object OR null if not found.
     */
    public static JQuickEvent<?> getQuickEvent(String id) {
        if (id == null) {
            throw new JException(JException.Reason.VALUE_IS_NULL);
        }

        return JadAPI.getInstance().getInformationManager().getQuickEvents().stream().filter(quickEvent -> quickEvent.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Dummy event to specify every possible event.
     */
    private static abstract class All extends Event { }
}
