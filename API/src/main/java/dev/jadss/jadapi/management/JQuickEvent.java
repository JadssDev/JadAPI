package dev.jadss.jadapi.management;

import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.Random;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class JQuickEvent {

    private final JadAPIPlugin plugin;
    private final Class<? extends Event> eventType;
    private final EventPriority priority;
    private final Consumer consumer;
    public int ticksToUnregister;
    private int executions;
    private final int executionsDefault;
    private String ID;

    /**
     * Create a QuickEvent
     *
     * @param plugin              Your plugin instance.
     * @param eventClass          The event class.
     * @param consumer            the Runnable that should be executed.
     * @param priority            Priority, works just like in Bukkit.
     * @param ticksToUnregister   Ticks to unregister this {@link JQuickEvent}
     * @param executionsRemaining how many times should your event be executed.
     * @param ID                  It's id, which can be get from "JQuickEvent.generateID();"
     * @param <E>                 Event
     */
    public <E extends Event> JQuickEvent(JadAPIPlugin plugin, Class<E> eventClass, Consumer<E> consumer, EventPriority priority, int ticksToUnregister, int executionsRemaining, String ID) {
        this.plugin = plugin;
        this.eventType = eventClass;
        this.priority = priority;
        this.consumer = consumer;
        this.ticksToUnregister = ticksToUnregister;
        this.executions = 0;
        this.executionsDefault = executionsRemaining;
        this.ID = ID;
    }

    /**
     * Create a QuickEvent
     *
     * @param plugin              Your plugin instance.
     * @param eventClass          The event class.
     * @param consumer            the Runnable that should be executed.
     * @param priority            Priority, works just like in Bukkit.
     * @param ticksToUnregister   Ticks to unregister this {@link JQuickEvent}
     * @param executionsRemaining how many times should your event be executed.
     * @param <E>                 Event
     */
    public <E extends Event> JQuickEvent(JadAPIPlugin plugin, Class<E> eventClass, Consumer<E> consumer, EventPriority priority, int ticksToUnregister, int executionsRemaining) {
        this.plugin = plugin;
        this.eventType = eventClass;
        this.priority = priority;
        this.consumer = consumer;
        this.ticksToUnregister = ticksToUnregister;
        this.executions = 0;
        this.executionsDefault = executionsRemaining;
        this.ID = generateID();
    }

    /**
     * Register your QuickListener to the JadEventHandler.<p>
     * if you set to true, it will be registered and will run as normal, but if it already reached the ticks to unregister value, it will not work.<P>
     * if you set to false, it will be unregisterd and will not run more, but you can register when you want, unless ticks to unregister = 0.
     *
     * @param registerORunregister if the event should be registered or unregistered.
     * @return itselfffff.
     */
    public JQuickEvent register(boolean registerORunregister) {
        if (!this.plugin.isRegistered()) throw new JException(JException.Reason.UNREGISTERED);
        if (registerORunregister) {
            this.executions = this.executionsDefault;
            JadAPI.getInstance().getInformationManager().getQuickEvents().add(this);
            this.plugin.getQuickEvents().add(this);
            if (JadAPI.getInstance().getDebug().doQuickEventsDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eRegistered a &3&lQuickEvent&e! &bEvent &e&m->&a " + this.eventType.getSimpleName() + "&e; &bRegisterer &e&m->&a " + this.plugin.getJavaPlugin().getName()));
        } else {
            this.executions = 0;
            JadAPI.getInstance().getInformationManager().getQuickEvents().remove(this);
            this.plugin.getQuickEvents().remove(this);
            if (JadAPI.getInstance().getDebug().doQuickEventsDebug())
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eUnregistered a &3&lQuickEvent&e! &bEvent &e&m->&a " + this.eventType.getSimpleName() + "&e; &bRegisterer &e&m->&a " + this.plugin.getJavaPlugin().getName()));
        }
        return this;
    }

    /**
     * Run the event.
     *
     * @param event Event that it should be ran with.
     * @return itself.
     */
    public JQuickEvent run(Event event) {
        if (event == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        if (executions != -1) {
            if (executions == 0) {
                this.register(false);
                return null;
            }

            executions--;
        }

        try {
            this.runConsumer(event);
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3&lJadAPI &7>> &eThis &3&lerror &eis &c&lNOT &efrom &3&lJadAPI&e, &b" + plugin.getJavaPlugin().getName() + " &3thrown this &c&lerror &eplease &b&lcontact &ethem! "));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m--------------------------------------"));
        }

        return this;
    }

    protected void runConsumer(Event event) throws Exception {
        consumer.accept(event);
        return;
    }

    /**
     * Generate an ID for your JQuickListener.
     *
     * @return A string with some randomized characters as ID.
     */
    public static String generateID() {
        char[] chars = ("abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789").toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 25; i++) builder.append(chars[new Random().nextInt(chars.length)]);
        return builder.toString();
    }

    public JadAPIPlugin getProviderPlugin() {
        return plugin;
    }

    public int getExecutionsLeft() {
        return this.executions;
    }

    public Class<? extends Event> getEventType() {
        return this.eventType;
    }

    public String getID() {
        return this.ID;
    }

    public EventPriority getPriority() {
        return this.priority;
    }

    /**
     * Get a QuickListener by it's ID.
     *
     * @param ID the id to search for.
     * @return The JQuickEvent object OR null if not found.
     */
    public static JQuickEvent getQuickEvent(String ID) {
        if (ID == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        for (JQuickEvent quickEvent : JadAPI.getInstance().getInformationManager().getQuickEvents()) {
            if (quickEvent.getID().equals(ID)) return quickEvent;
        }
        return null;
    }
}
