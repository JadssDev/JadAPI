package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.annotations.Beta;
import dev.jadss.jadapi.management.nms.interfaces.NMSManipulable;
import dev.jadss.jadapi.management.nms.interfaces.NMSObject;

@Beta(description = "This class is still in development") //todo
public class Item implements NMSObject, NMSManipulable {

    private Object item;

    public Item(Object object) {
        this.item = object;
    }

    @Override
    public Object getHandle() { return item; }
}
