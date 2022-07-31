package dev.jadss.jadapi.bukkitImpl.menu;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.item.JInventory;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.ClickContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.CloseContext;
import dev.jadss.jadapi.bukkitImpl.menu.context.types.OpenContext;

import java.util.function.Consumer;

public final class JMenu extends AbstractMenu<JMenu, JInventory, JItemStack> {

    private final boolean reuseInventory;
    private final boolean cancelEveryClick;
    private final Consumer<OpenContext<JInventory, JItemStack>> onOpen;
    private final Consumer<ClickContext<JInventory, JItemStack>> onClick;
    private final Consumer<CloseContext<JInventory, JItemStack>> onClose;


    public JMenu(boolean reuseInventory, boolean cancelEveryClick, JadAPIPlugin registerer, JInventory initialInventory,
                 Consumer<OpenContext<JInventory, JItemStack>> onOpen, Consumer<ClickContext<JInventory, JItemStack>> onClick, Consumer<CloseContext<JInventory, JItemStack>> onClose) {
        super(registerer, initialInventory);

        this.reuseInventory = reuseInventory;
        this.cancelEveryClick = cancelEveryClick;

        this.onOpen = onOpen;
        this.onClick = onClick;
        this.onClose = onClose;
    }

    @Override
    protected void onRegister() {
    }

    @Override
    protected void onUnregister() {
    }

    @Override
    protected void onOpen(OpenContext<JInventory, JItemStack> context) {
        onOpen.accept(context);
    }

    @Override
    protected void onClick(ClickContext<JInventory, JItemStack> context) {
        this.onClick.accept(context);
    }

    @Override
    protected void onClose(CloseContext<JInventory, JItemStack> context) {
        this.onClose.accept(context);
    }

    @Override
    protected boolean cancelEveryClick() {
        return cancelEveryClick;
    }

    @Override
    protected boolean reuseInventory() {
        return reuseInventory;
    }
}
