package dev.jadss.jadapi.management.nms.objects.chat;

import com.google.gson.Gson;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.ChatColor;

/**
 * Represents a message in NMS.
 * <p><b>Usage:</b></p>
 * <p>JSON Mode is instead of using text, and using the default format, you can use a json itself in this component to build it!</p>
 */
public class IChatBaseComponent implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    public static final Class<?> iChatBaseComponentClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.chat" : "server." + JReflection.getNMSVersion()) + ".IChatBaseComponent");
    public static final Class<?> chatSerializerClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.chat" : "server." + JReflection.getNMSVersion()) + ".IChatBaseComponent$ChatSerializer");
    public static final String defaultSimpleFormat = "{\"text\":\"" + "%text%" + "\"}";

    private static final Gson g = new Gson();

    private String message;

    private boolean jsonMode = false;
    private String jsonMessage;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public boolean isJsonMode() {
        return jsonMode;
    }
    public void setJsonMode(boolean jsonMode) {
        this.jsonMode = jsonMode;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }
    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = ChatColor.translateAlternateColorCodes('&', jsonMessage);
    }

    public IChatBaseComponent() { }
    public IChatBaseComponent(String message, boolean jsonMode, String jsonMessage) {
        this.jsonMode = jsonMode;
        if(jsonMode)
            this.jsonMessage = ChatColor.translateAlternateColorCodes('&', jsonMessage);
        else
            this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public Object build() {
        return JReflection.executeUnspecificMethod(chatSerializerClass, new Class[] { String.class }, null, iChatBaseComponentClass, jsonMode ? jsonMessage : defaultSimpleFormat.replace("%text%", message));
    }

    @Override
    public void parse(Object object) {
        if(object == null) return;
        if(!canParse(object)) throw new NMSException("Cannot parse this object.");

        this.message = JReflection.executeUnspecificMethod(iChatBaseComponentClass, new Class[] { }, object, String.class);
        this.jsonMessage = JReflection.executeUnspecificMethod(chatSerializerClass, new Class[] { iChatBaseComponentClass }, null, String.class, object);
    }

    @Override
    public boolean canParse(Object object) {
        return iChatBaseComponentClass.isAssignableFrom(object.getClass());
    }

    @Override
    public Class<?> getParsingClass() { return iChatBaseComponentClass; }

    @Override
    public NMSObject copy() { return new IChatBaseComponent(this.message, this.jsonMode, this.jsonMessage); }
}
