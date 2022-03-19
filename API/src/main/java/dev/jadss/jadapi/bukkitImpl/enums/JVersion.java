package dev.jadss.jadapi.bukkitImpl.enums;

import dev.jadss.jadapi.utils.JReflection;

public enum JVersion {
    UNKNOWN(-1),
    v1_7(2),
    v1_8(3),
    v1_9(4),
    v1_10(5),
    v1_11(6),
    v1_12(7),
    v1_13(8),
    v1_14(9),
    v1_15(10),
    v1_16(11),
    v1_17(12),
    v1_18(13);

    private int priority;

    JVersion(int versionPriority) {
        priority = versionPriority;
    }

    public boolean isNewerOrEqual(JVersion version) {
        if(this == version) return true;

        return this.priority >= version.priority;
    }

    public boolean isLowerOrEqual(JVersion version) {
        if(this == version) return true;

        return this.priority <= version.priority;
    }

    /**
     * Parse a protocol to a version..
     * @param protocol What protocol to parse.
     * @return the {@link JVersion}.
     */
    public static JVersion parseProtocol(long protocol) {
        if(protocol <= 5) return JVersion.v1_7;
        else if (protocol <= 47) return JVersion.v1_8;
        else if (protocol <= 110) return JVersion.v1_9;
        else if (protocol <= 210) return JVersion.v1_10;
        else if (protocol <= 316) return JVersion.v1_11;
        else if (protocol <= 340) return JVersion.v1_12;
        else if (protocol <= 404) return JVersion.v1_13;
        else if (protocol <= 498) return JVersion.v1_14;
        else if (protocol <= 578) return JVersion.v1_15;
        else if (protocol <= 754) return JVersion.v1_16;
        else if (protocol <= 756) return JVersion.v1_17;
        else if (protocol <= 757) return JVersion.v1_18;
        else return JVersion.UNKNOWN;
    }

    /**
     * Get the server version.
     * @return THE JVersion!
     */
    public static JVersion getServerVersion() {
        String current = JReflection.getNMSVersion();
        if (current.contains("1_7")) return JVersion.v1_7;
        else if(current.contains("1_8")) return JVersion.v1_8;
        else if(current.contains("1_9")) return JVersion.v1_9;
        else if(current.contains("1_10")) return JVersion.v1_10;
        else if(current.contains("1_11")) return JVersion.v1_11;
        else if(current.contains("1_12")) return JVersion.v1_12;
        else if(current.contains("1_13")) return JVersion.v1_13;
        else if(current.contains("1_14")) return JVersion.v1_14;
        else if(current.contains("1_15")) return JVersion.v1_15;
        else if(current.contains("1_16")) return JVersion.v1_16;
        else if(current.contains("1_17")) return JVersion.v1_17;
        else if(current.contains("1_18")) return JVersion.v1_18;
        else return JVersion.UNKNOWN;
    }

    public int getPriority() { return priority; }
}
