package dev.jadss.jadapi.management.nms.packets;

import dev.jadss.jadapi.annotations.Beta;
import dev.jadss.jadapi.management.nms.interfaces.DefinedPacket;

@Beta(description = "To be done!")
public class InInteractAtEntityPacket extends DefinedPacket {
    @Override
    public DefinedPacket copy() {
        return null;
    }

    @Override
    public boolean canParse(Object object) {
        return false;
    }

    @Override
    public void parse(Object packet) {

    }

    @Override
    public Object build() {
        return null;
    }

    @Override
    public Class<?> getParsingClass() {
        return null;
    }
}
