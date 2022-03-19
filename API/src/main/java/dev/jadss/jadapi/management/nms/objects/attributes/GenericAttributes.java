package dev.jadss.jadapi.management.nms.objects.attributes;

import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.utils.JReflection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericAttributes {

    public static final Class<?> genericAttributesClass = JReflection.getReflectionClass("net.minecraft." + (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_17) ? "world.entity.ai.attributes" : "server." + JReflection.getNMSVersion()) + ".GenericAttributes");

    private static final Map<AttributeType, Attribute> attributes = new HashMap<>();

    public static Attribute getAttribute(AttributeType type) {
        return attributes.get(type);
    }

    static {
        List<Object> objects = JReflection.getFieldsOfType(genericAttributesClass, null, null, true);

        List<AttributeType> attributesPossible = Arrays.asList(AttributeType.values());

        for(Object object : objects) {
            String name = Attribute.getNameFromNMS(object);
            if(name == null) continue;
            AttributeType type = attributesPossible.stream().filter(attribute -> attribute.getName().equals(name)).findFirst().orElse(null);
            if(type == null) continue;
            GenericAttributes.attributes.put(type, new Attribute(object, type));
        }
    }
}
