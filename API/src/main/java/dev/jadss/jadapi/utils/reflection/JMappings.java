package dev.jadss.jadapi.utils.reflection;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;

import java.util.*;
import java.util.stream.Collectors;

public class JMappings {

    private static final List<JMappings> mappings = new ArrayList<>();

    private final Class<?> mappingClass;
    private final Map<JVersion, String> maps = new HashMap<>();

    private String currentVersionValue = null;

    private boolean finished = false;

    private JMappings(Class<?> mappingClass) {
        this.mappingClass = mappingClass;
    }

    public JMappings add(JVersion version, String value) {
        if (this.finished)
            throw new IllegalStateException("Cannot add to finished Mappings!");

        this.maps.put(version, value);

        return this;
    }

    public String get() {
        if (currentVersionValue == null)
            throw new IllegalStateException("Current value is null, but IT CAN'T be null, what?");

        return currentVersionValue;
    }

    public JMappings finish() {
        this.finished = true;

        JVersion currentVersion = JVersion.getServerVersion();
        if (currentVersion == null || currentVersion == JVersion.UNKNOWN) //Fallback
            currentVersion = JVersion.v1_7;

        if (maps.containsKey(currentVersion)) {
            this.currentVersionValue = maps.get(currentVersion);
            //done
        } else {
            //Find the earliest version found on database.
            JVersion earliestVersion = null; //List where the first value is, for example, v1_7 and last is v1_18
            for (Map.Entry<JVersion, String> entry : maps.entrySet().stream().sorted(Comparator.comparingInt(jVersionStringEntry -> jVersionStringEntry.getKey().getPriority())).collect(Collectors.toList())) {
                if (earliestVersion != null) {
                    if (currentVersion.getPriority() >= entry.getKey().getPriority() && entry.getKey().getPriority() > earliestVersion.getPriority())
                        earliestVersion = entry.getKey();
                } else if (currentVersion.getPriority() > entry.getKey().getPriority()) {
                    earliestVersion = entry.getKey();
                }
            }

            if (earliestVersion == null) {
                this.currentVersionValue = null;
            } else {
                currentVersionValue = maps.get(earliestVersion);
            }
        }

        return this;
    }

    public static JMappings create(Class<?> mappingClass) {
        return new JMappings(mappingClass);
    }

}
