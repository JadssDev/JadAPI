package dev.jadss.jadapi.management.nms.objects.other;

import dev.jadss.jadapi.management.nms.interfaces.NMSObject;

public class ObjectPackage implements NMSObject {

    private Object object;

    public Object getObject() { return object; }
    public void setObject(Object object) { this.object = object;}

    public ObjectPackage() {}
    public ObjectPackage(Object object) { this.object = object; }

    @Override
    public String toString() {
        return "ObjectPackage{" +
                "object=" + object +
                '}';
    }
}
