package dev.jadss.jadapi.management.labymod.displays;

import dev.jadss.jadapi.management.labymod.LabyModPacket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Makes it possible to display the economy or a number in the upper right corner of the screen in the LabyMod client.
 */
@LabyModPacket.SentWhen(value = LabyModPacket.SentType.WHENEVER)
@LabyModPacket.WikiPage(value = "https://docs.labymod.net/pages/server/displays/economy")
public class EconomyPacket extends LabyModPacket {

    /**
     * This map is unmodifiable, do not attempt to do so! Doing so will result in a {@link UnsupportedOperationException}.
     */
    public final Map<String, EconomyType> map;

    public EconomyPacket() { //Note: cannot use this(null), due to NPE.
        this.map = null;
    }

    public EconomyPacket(Map<String, EconomyType> map) {
        this.map = Collections.unmodifiableMap(map);
    }

    @Override
    public String getMessageKey() {
        return "ECONOMY";
    }

    @Override
    public String buildString() {
        try {
            return MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCouldn't build the JSON String for the LabyModPacket!"));
            e.printStackTrace();
            return "{}"; //Compatibility.
        }
    }

    public static EconomyPacket parse(String json) {
        try {
            Map<String, EconomyType> parsedMap = new HashMap<>();

            Map<String, Map<String, Object>> map = MAPPER.readValue(json, Map.class);
            for (String key : map.keySet()) {
                Map<String, Object> decimalMap = (Map<String, Object>) map.get(key).get("decimal");

                parsedMap.put(key, new EconomyType((boolean) map.get(key).get("visible"), (int) map.get(key).get("balance"), (String) map.get(key).get("icon"),
                        (decimalMap != null ? new EconomyType.Decimal((String) decimalMap.get("format"), (int) decimalMap.get("divisor")) : null)));
            }

            return new EconomyPacket(parsedMap);
        } catch(Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eCouldn't parse the JSON String for the LabyModPacket!"));
            e.printStackTrace();
            return new EconomyPacket(new HashMap<>());
        }
    }

    @Override
    public LabyModPacket copy() {
        return new EconomyPacket(Collections.unmodifiableMap(new HashMap<>(this.map)));
    }

    /**
     * Represents an economy.
     */
    public static class EconomyType {

        public final boolean visible;
        public final int balance;

        /**
         * Url to image.
         */
        public final String icon;

        public final Decimal decimal;

        public EconomyType() {
            this(false, 0, null, null);
        }

        public EconomyType(boolean visible, int balance, String iconURL) {
            this(visible, balance, iconURL, null);
        }

        public EconomyType(boolean visible, int balance, String iconURL, Decimal decimal) {
            this.visible = visible;
            this.balance = balance;
            this.icon = iconURL;
            this.decimal = decimal;
        }

        public static class Decimal {

            /**
             * Format - ##.## (example)
             */
            public final String format;
            /**
             * Devisor - 5000/100 = 50.00
             */
            public final int divisor;

            public Decimal() {
                this(null, 0);
            }

            public Decimal(String format, int divisor) {
                this.format = format;
                this.divisor = divisor;
            }
        }
    }
}
