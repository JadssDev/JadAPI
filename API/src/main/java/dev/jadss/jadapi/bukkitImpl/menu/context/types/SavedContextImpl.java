package dev.jadss.jadapi.bukkitImpl.menu.context.types;

import dev.jadss.jadapi.bukkitImpl.entities.JPlayer;
import dev.jadss.jadapi.bukkitImpl.item.AbstractInventory;
import dev.jadss.jadapi.bukkitImpl.item.AbstractItemStack;
import dev.jadss.jadapi.bukkitImpl.menu.context.Saved;
import dev.jadss.jadapi.bukkitImpl.menu.context.SavedContext;

import java.util.HashMap;
import java.util.Map;

public class SavedContextImpl<T extends AbstractInventory<T, K>, K extends AbstractItemStack<K>> implements SavedContext<T, K>, Saved {

    private final MainContext<T, K> context;
    private final JPlayer player;
    private final Map<String, Object> variables = new HashMap<>();

    public SavedContextImpl(MainContext<T, K> context, JPlayer player) {
        this.context = context;
        this.player = player;
    }

    @Override
    public MainContext<T, K> getMainContext() {
        return context;
    }

    @Override
    public JPlayer getPlayer() {
        return player;
    }

    @Override
    public Object getVariable(String variableName) {
        return variables.getOrDefault(variableName, null);
    }

    @Override
    public void setVariable(String variableName, Object object) {
        variables.put(variableName, object);
    }

    @Override
    public Map<String, Object> entries() {
        return new HashMap<>(variables);
    }
}
