package dev.jadss.jadapi.management.nms.objects.chat;

import com.google.gson.Gson;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.management.nms.NMS;
import dev.jadss.jadapi.management.nms.NMSException;
import dev.jadss.jadapi.management.nms.interfaces.NMSBuildable;
import dev.jadss.jadapi.management.nms.interfaces.NMSCopyable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;
import dev.jadss.jadapi.management.nms.interfaces.NMSParsable;
import dev.jadss.jadapi.utils.reflection.JMappings;
import dev.jadss.jadapi.utils.reflection.reflectors.JClassReflector;
import dev.jadss.jadapi.utils.reflection.reflectors.JMethodReflector;
import org.bukkit.ChatColor;

/**
 * Represents a message in NMS.
 * <p><b>Usage:</b></p>
 * <p>JSON Mode is instead of using text, and using the default format, you can use a json itself in this component to build it!</p>
 */
public class IChatBaseComponent implements NMSObject, NMSBuildable, NMSParsable, NMSCopyable {

    public static final Class<?> iChatBaseComponentClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.chat" : "server." + NMS.getNMSVersion()) + ".IChatBaseComponent");
    public static final Class<?> iChatBaseMutableComponentClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.chat" : "server." + NMS.getNMSVersion()) + ".IChatMutableComponent");
    public static final Class<?> chatSerializerClass = JClassReflector.getClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "network.chat" : "server." + NMS.getNMSVersion()) + "." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_8) ? "IChatBaseComponent$" : "") + "ChatSerializer");
    public static final String defaultSimpleFormat = "{\"text\":\"" + "%text%" + "\"}";

    private static final Gson gsonInstance = new Gson();

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

    public IChatBaseComponent() {
        this.message = "";
        this.jsonMode = false;
        this.jsonMessage = "";
    }

    public IChatBaseComponent(String message, boolean jsonMode, String jsonMessage) {
        this.jsonMode = jsonMode;
        if (jsonMode)
            this.jsonMessage = ChatColor.translateAlternateColorCodes('&', jsonMessage);
        else
            this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public Object build() {
        return JMethodReflector.executeMethod(chatSerializerClass, "a", new Class[]{String.class}, null, new Object[]{jsonMode ? jsonMessage : defaultSimpleFormat.replace("%text%", message)});
    }

    public static final JMappings GET_TEXT_METHOD = JMappings.create(iChatBaseComponentClass)
            .add(JVersion.v1_7, "e")
            .add(JVersion.v1_8, "getText")
            .add(JVersion.v1_18, "a")
            .add(JVersion.v1_19, "getString") //They removed getText method so this is the alternative...
            .finish();

    public static final JMappings JSON_METHOD = JMappings.create(iChatBaseComponentClass)
            .add(JVersion.v1_7, "a")
            .finish();

    @Override
    public void parse(Object object) {
        if (object == null) return;
        if (!canParse(object)) throw new NMSException("Cannot parse this object.");

        this.message = (String) JMethodReflector.executeMethod(iChatBaseComponentClass, GET_TEXT_METHOD.get(), new Class[]{}, object, null);
        this.jsonMessage = (String) JMethodReflector.executeMethod(chatSerializerClass, JSON_METHOD.get(), new Class[]{IChatBaseComponent.iChatBaseComponentClass}, null, new Object[]{object});
    }

    @Override
    public boolean canParse(Object object) {
        return iChatBaseComponentClass.isAssignableFrom(object.getClass());
    }

    @Override
    public Class<?> getParsingClass() {
        return iChatBaseComponentClass;
    }

    @Override
    public NMSObject copy() {
        return new IChatBaseComponent(this.message, this.jsonMode, this.jsonMessage);
    }

    @Override
    public String toString() {
        return "IChatBaseComponent{" +
                "message='" + message + '\'' +
                ", jsonMode=" + jsonMode +
                ", jsonMessage='" + jsonMessage + '\'' +
                '}';
    }
}
