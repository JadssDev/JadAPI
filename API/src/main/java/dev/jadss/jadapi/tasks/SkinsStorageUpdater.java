package dev.jadss.jadapi.tasks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.jadss.jadapi.JadAPI;
import dev.jadss.jadapi.bukkitImpl.misc.JSkin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SkinsStorageUpdater extends BukkitRunnable {

    public void run() {
        if (JadAPI.getInstance().getDebug().doMiscDebug())
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lJadAPI &7>> &eExecuting &3&lSkin &bUpdater&e."));
        boolean continueLoop = true;
        while (continueLoop) {
            continueLoop = executeUpdate();
        }
    }

    public boolean executeUpdate() {
        if (JadAPI.getInstance().getSkinsToDownload().size() != 0) {

            String name = JadAPI.getInstance().getSkinsToDownload().get(0);
            if (JSkin.getSkin(name) != null) {
                JadAPI.getInstance().getSkinsToDownload().remove(name);
                return true;
            }

            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
            Gson g = new Gson();
            JsonObject obj = g.fromJson(result, JsonObject.class);
            if (obj == null || obj.get("id") == null) {
                JadAPI.getInstance().getSkinsToDownload().remove(0);
                return true;
            }

            String playerUID = obj.get("id").toString().replace("\"", "");

            String textureRequest = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + playerUID);

            obj = g.fromJson(textureRequest, JsonObject.class);
            String texture = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String signature = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("signature").getAsString();

            //Re-form the uuid.
            UUID uuid = UUID.fromString(playerUID.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));

            JadAPI.getInstance().getSkinsStorage().add(new JSkin(uuid, name, texture, signature));
            JadAPI.getInstance().getSkinsToDownload().remove(0);
            return true;
        } else {
            return false;
        }
    }

    public String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignored) {
            }
        }
        return sb.toString();
    }
}
