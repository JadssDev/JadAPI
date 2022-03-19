package dev.jadss.jadapi.bukkitImpl.misc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.jadss.jadapi.JadAPI;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a skin in minecraft!
 */
public class JSkin {

    private UUID mojangPlayerUID;
    private String playerName;
    private String textureBase64;
    private String certificateBase64;

    public JSkin(UUID mojangPlayerUID, String playerName, String textureBase64, String certificateBase64) {
        this.mojangPlayerUID = mojangPlayerUID;
        this.playerName = playerName;
        this.textureBase64 = textureBase64;
        this.certificateBase64 = certificateBase64;
    }

    /**
     * Get the MojangUID of the owner of this skin.
     * @return the UUID.
     */
    public UUID getMojangPlayerUID() {
        return mojangPlayerUID;
    }

    /**
     * Gets the name of the player this skin is from.
     * @return the name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Get the texture base64 of this skin!
     * @return the texture base64.
     */
    public String getTextureBase64() {
        return textureBase64;
    }

    /**
     * Get the certificate base64 of this skin!
     * @return the certificate base64.
     */
    public String getCertificateBase64() {
        return certificateBase64;
    }

    /**
     * Get a skin by the player name!
     * @param playerName the player name to find the skin by!
     * @return The skin if any were found, null otherwise.
     */
    public static JSkin getSkin(String playerName) {
        for (JSkin skin : JadAPI.getInstance().getSkinsStorage())
            if (skin.playerName.equalsIgnoreCase(playerName)) return skin;
        ;
        return null;
    }

    /**
     * Get the skin by a MojangUID!
     * @param mojangUID the MojangUID to find the skin by!
     * @return The skin if any were found, null otherwise.
     */
    public static JSkin getSkinByMojangID(UUID mojangUID) {
        for (JSkin skin : JadAPI.getInstance().getSkinsStorage())
            if (skin.mojangPlayerUID.equals(mojangUID)) return skin;
        return null;
    }

    /**
     * Process a skin by the player's name!
     * <p>This will download the skin from Mojang's servers and process it!</p>
     * @param name the name of the player.
     * @return A {@link CompletableFuture} that will be completed when the skin is processed, can be null if this player was not found!
     */
    public static CompletableFuture<JSkin> processSkin(String name) {
        CompletableFuture<JSkin> completableFuture = new CompletableFuture<>();

        if (JSkin.getSkin(name) != null) {
            completableFuture.complete(JSkin.getSkin(name));
            return completableFuture;
        }

        Bukkit.getScheduler().runTaskAsynchronously(JadAPI.getInstance(), () -> {
            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);

            Gson g = new Gson();
            JsonObject obj = g.fromJson(result, JsonObject.class);
            if (obj == null || obj.get("id") == null) {
                completableFuture.complete(null);
                return;
            }

            String playerUID = obj.get("id").toString().replace("\"", "");

            String textureRequest = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + playerUID + "?unsigned=false");

            obj = g.fromJson(textureRequest, JsonObject.class).getAsJsonArray("properties").get(0).getAsJsonObject();
            String texture = obj.get("value").getAsString();
            String signature = obj.get("signature").getAsString();

            //Re-form the uuid.
            UUID uuid = UUID.fromString(playerUID.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));

            JSkin skin = new JSkin(uuid, name, texture, signature);

            JadAPI.getInstance().getSkinsStorage().add(skin);
            completableFuture.complete(skin);
        });

        return completableFuture;
    }

    private static String getURLContent(String urlStr) {
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
