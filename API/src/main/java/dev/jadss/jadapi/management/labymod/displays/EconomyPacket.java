package dev.jadss.jadapi.management.labymod.displays;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.jadss.jadapi.management.labymod.LabyModPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Shows a little image and amount of xyz for every single of the Economies in the top right corner.
 */
public class EconomyPacket extends LabyModPacket {

    public Map<String, Cash> economies;

    public EconomyPacket(Map<String, Cash> map) {
        this.economies = map;
    }

    @Override
    public String getMessageKey() {
        return "economy";
    }

    @Override
    public void parse(JsonObject object) {
        Set<Map.Entry<String, JsonElement>> entries = object.entrySet();

        Map<String, Cash> map = new HashMap<>();
        entries.forEach((entry) -> {
            JsonObject cashObject = entry.getValue().getAsJsonObject();
            Cash cash = new Cash(cashObject.get("visible").getAsBoolean(), cashObject.get("balance").getAsInt(), cashObject.get("icon").getAsString(), null);
            JsonObject decimalObject = entry.getValue().getAsJsonObject().get("decimal").getAsJsonObject();
            cash.decimal = new Cash.Decimal(decimalObject.get("format").getAsString(), decimalObject.get("divisor").getAsInt());
            map.put(entry.getKey(), cash);
        });
        this.economies = map;
    }

    @Override
    public String buildString() {
        return g.toJson(economies);
    }

    @Override
    public LabyModPacket copy() {
        return new EconomyPacket(new HashMap<>(economies));
    }

    /**
     * Represents an economy.
     */
    public static class Cash {

        public boolean visible;
        public int balance;
        /**
         * Url to image.
         */
        public String icon;
        public Decimal decimal;

        public Cash(boolean visible, int balance, String iconURL, Decimal decimal) {
            this.visible = visible;
            this.balance = balance;
            this.icon = iconURL;
            this.decimal = decimal;
        }

        public static class Decimal {
            /**
             * Format - ##.## (example)
             */
            public String format;
            /**
             * Devisor - 5000/100 = 50.00
             */
            public int divisor;

            public Decimal(String format, int divisor) {
                this.format = format;
                this.divisor = divisor;
            }
        }
    }
}
