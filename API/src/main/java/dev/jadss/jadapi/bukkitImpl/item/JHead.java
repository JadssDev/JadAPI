package dev.jadss.jadapi.bukkitImpl.item;

import dev.jadss.jadapi.bukkitImpl.misc.JSkin;
import dev.jadss.jadapi.exceptions.JException;
import org.bukkit.Bukkit;

import java.util.UUID;

public final class JHead {

    private String base64Value = null;

    /**
     * Create a head with a base64 value.
     * @param value the value encoded base64 in a string.
     */
    public JHead(String value) {
        if(value == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        this.base64Value = value;
    }

    /**
     * Create a head using the player's uuid
     * @param mojangPlayerUUID the uuid of the player in mojang servers..
     */
    public JHead(UUID mojangPlayerUUID) {
        if(mojangPlayerUUID == null) throw new JException(JException.Reason.UUID_IS_NULL);

        JSkin skin = JSkin.getSkinByMojangID(mojangPlayerUUID);
        if(skin != null) base64Value = skin.getTextureBase64();
    }

    public JHead(String playerName, int idk) {
        if(playerName == null) throw new JException(JException.Reason.VALUE_IS_NULL);

        JSkin skin = JSkin.getSkin(playerName);
        if(skin != null) base64Value = skin.getTextureBase64();
    }

    /**
     * Get the JItemStack
     * @return JItemStack object.
     */
    public JItemStack buildHead() {
        JItemStack stack = new JItemStack(JMaterial.getRegistryMaterials().find("PLAYER_HEAD"));
        if (base64Value == null) return stack;
        UUID hash = new UUID(base64Value.hashCode(), base64Value.hashCode());
        return
                stack.setItemStack(Bukkit.getUnsafe().modifyItemStack(stack.buildItemStack(), "{SkullOwner:{Id:\"" + hash + "\",Properties:{textures:[{Value:\"" + base64Value + "\"}]}}}"));
    }
}
